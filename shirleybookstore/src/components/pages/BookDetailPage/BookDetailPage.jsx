import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getAllBook, getBookById } from "../../../service/BookService";
import { addComment, deleteComment, getCommentByBook, replyComment, updateComment } from "../../../service/CommentService";
import { toast } from "react-toastify";
import Swal from "sweetalert2";
import { addItemCart } from "../../../service/CartService";
import { ReviewSection } from "./components/ReviewSection";
import { BookFeatur } from "./components/BookFeatur";
import { ViewBook } from "../BookPage/components/ViewBook";
import { addFavorite } from "../../../service/FavoriteService";

export const BookDetail = () =>{
    const accessToken = sessionStorage.getItem('accessToken');
    const {id} = useParams();
    const [book,setBook] = useState(null);//thông tin chi tiết về khóa học
    const [loading, setLoading] = useState(true);
    const [comments ,setComments] = useState([]);//list comment
    const [newComment,setNewComment] = useState("");//add comment
    const [replyContent,setReplyContent] = useState({});
    const [editContent ,setEditContent] = useState({});//cập nhật nội dung chỉnh sửa của bình luận
    const [editingCommentId,setEditingCommentId] = useState(null);//lưu ID của bình luận đang được chỉnh sửa
    const [books,setBooks] = useState([]);
    const navigate = useNavigate();

    const fetchBooks = async ()=>{
            setLoading(true);
            try{
                const result = await getAllBook(1,12);
                if(result && result.result){
                    setBooks(result.result.items);
                }else{
                    setBooks([]);
                }
            }catch (err){
                console.log(err);
            }
    };
    console.log(books);
    useEffect(() => {
        fetchBooks();
    },[]);

    useEffect(()=>{
        document.title = 'Books Detail'
    })

    useEffect(()=>{
        getBookById(id)
            .then(data => {
                setBook(data.result);
                setLoading(false);
            }).catch(error => {
                console.log(error);
                setLoading(false);
            })
    },[id]);
    //thông tin comment
    useEffect(() =>{
        getCommentByBook(id)
            .then(data =>{
                const updatedComments = data.result.items.map(comment =>({
                    ...comment,
                    replying:false,//khó hiểu + nghi ngờ
                    replies: comment.replies || []
                }));
                setComments(updatedComments);
            }).catch(error =>console.log(error));
    },[id]);

    if(loading) return <div>Loading...</div>;

    if(!book){
        return <div>Book data is not available</div>;
    }

    //Add a new comment
    const handleAddComment = async () =>{
        const commentData = {
            content: newComment.trim(),
            parentCommentId:null,
            bookId: id
        };

        try{
            const result = await addComment(commentData);
            setComments([{
                ...result,//sao chép toàn bộ thuộc tính trong result: bình luận mới được trả về từ service
                replying:false,
                replies: []
            },...comments])//sao chép toàn bộ comment trước đó bao gồm cả comment mới được thêm vào danh sách

            setNewComment("");
        }catch (error){
            console.log("Error:",error);
        }
    };

    //Handle adding a reply
    const handleAddReply = async (commentId) =>{
        const replyText = replyContent[commentId];

        // if(!replyText.trim()){
        //     toast.error('Please enter a reply');
        //     return ;
        // }

        const replydata = {
            content: replyText.trim(),
            parentCommentId: commentId,
            bookId: id
        }

        try{
            const result = await replyComment(replydata);

            if(result){
                //Cập nhật lại danh sách comments với reply mới
                const updateComments = comments.map(comment =>{
                    if(comment.id === commentId){
                        return {
                            ...comment,
                            replies: [...comment.replies,{...result,replying:false}]
                        }
                    }
                    return comment;
                })
                setComments(updateComments);
                setReplyContent({...replyContent,[commentId]: ''});
            }
        }catch(error){
            console.log('Error in component:',error);
        }
    };

    //Edit comment
    const handleEditComment = async (commentId) =>{
        const updatedContent = (editContent[commentId] || "").trim();

        try{
            const result = await updateComment(commentId,updatedContent);

            if(result){
                setComments((prevComments) =>{
                    const updatedComments = prevComments.map(comment =>{
                        //Cập nhật comment cha nếu cần
                        if(comment.id === commentId){
                            return {...comment,content:result.content}
                        }
                        //Cập nhật replies nếu comment là trả lời
                        const updatedReplies = comment.replies.map(reply =>{
                            if(reply.id === commentId){
                                return {...reply,content: result.content};
                            }
                            return reply;
                        })
                        return {...comment,replies: updatedReplies};
                    });
                    return [...updatedComments];
                });
                setEditingCommentId(null);//thoát khỏi chế độ chỉnh sửa
            }
        }catch(error){
            console.error('Error editing comment:',error);
        }
    };

    //Delete comment
    const handleDeleteComment = async (commentId) =>{
        try{
            await deleteComment(commentId);
            //nếu thành công thì cập nhật lại danh sách comments sau khi xóa thành công
            setComments((prevComments) =>{
                const updatedComments = prevComments
                    .filter(comment =>commentId !== comment.id)//loại bỏ bình luận cha đã bị xóa
                    .map(comment=>({
                        ...comment,
                        replies: comment.replies.filter(reply => reply.id !== commentId)
                        //nếu comment là comment con của comment cha thì nó sẽ xóa comment con ấy ra khỏi danh sách của comment cha
                    }));
                return updatedComments;
            })
        }catch (error){
            console.log('Error deleting comment:',error);
        }
    }

    //Toggle reply input (bật tắt trạng thái comment của comemntId này)
    const handleReplyToggle = (commentId) =>{
        const updatedComments = comments.map(comment => {
            if(comment.id === commentId){
                return {...comment,replying: !comment.replying};
            }
            return comment;
        });
        setComments(updatedComments);
    }

    const addItemCarts = async (id,quantity) =>{
        try{
            const response = addItemCart(id,quantity);
            if(response){
                console.log("Sản phẩm đã được thêm vào giỏ hàng");
            }
        }catch(error){
            console.log("thêm sản phẩm vào giỏ hàng thất bại")
        }
    };

    const addFavorites = async (bookId) =>{
        try{
            const response = addFavorite(bookId);
            if(response){
                console.log("Sản phẩm đã được thêm vào giỏ hàng");
            }
        }catch(error){
            console.log("thêm sản phẩm vào giỏ hàng thất bại")
        }
    };

    const handlePayment = () => {
        console.log(id)
        let orderItems=[];
        orderItems.push({
            bookId: id,
            quantity: 1,
        })
        let bookList=[];
        bookList.push(book);
        console.log({id,bookList})
        console.log("danh sách sản phẩm được thanh toán:", orderItems);
        // ví dụ điều hướng sang trang thanh toán và truyền dữ liệu
        navigate("/order", { state: { items: orderItems,listBooks:bookList } });
    };



    return (
        <div className="container-fluid py-3">
            <div className="container py-5">
                <div className="row">
                    <div className="col-lg-12">
                        <div className="mb-5 mt-5">
                            <div className="row">
                                <div className="col-12">
                                    <h4 className="text-secondary text-uppercase pb-2">Book Detail</h4>
                                </div>
                            </div>
                            <div className="row">
                                {/* Cột ảnh */}
                                <div className="col-md-4 mb-4">
                                    <img className="img-fluid rounded w-100" src={book.thumbnail} alt="Book" />
                                </div>

                                {/* Cột mô tả + thông tin */}
                                <div className="col-md-8">
                                    <h4 className="mt-4">{book.title}</h4>
                                    <h6 className="mt-4">Tác giả: <strong>{book.authorName}</strong></h6>
                                    
                                    <h5 className="mt-3">Mô tả truyện:</h5>
                                    <div dangerouslySetInnerHTML={{ __html: book.description }} />
                                    
                                    <h5 className="mt-3">Danh mục: {book.category}</h5>
                                    
                                    <h6>Giá: <strong>{book.price} VND</strong></h6>

                                    <div className="mt-4 btn-block">
                                        <div className="btn" onClick={() => addItemCarts(id,1)}>
                                            <i className="fa fa-shopping-cart me-2"></i>Thêm vào giỏ hàng
                                        </div>

                                        <div className="btn" onClick={()=>addFavorites(id)}>
                                            <i className="fa fa-heart me-2"></i>Yêu thích
                                        </div>

                                        <div className="btn" onClick={()=>handlePayment()}>
                                            <i className="fa fa-credit-card me-2"></i>Thanh toán ngay
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <ReviewSection 
                            comments={comments}
                            newComment={newComment}
                            editingCommentId={editingCommentId}
                            setEditingCommentId={setEditingCommentId}
                            setNewComment={setNewComment}
                            replyContent={replyContent}
                            setReplyContent={setReplyContent}
                            editContent={editContent}
                            setEditContent={setEditContent}
                            handleAddComment={handleAddComment}
                            handleReplyToggle={handleReplyToggle}
                            handleAddReply={handleAddReply}
                            handleEditComment={handleEditComment}
                            handleDeleteComment={handleDeleteComment}
                        />
                        <h6 className="display-4" style={{textAlign:"center"}} >Related stories</h6>
                        <ViewBook books={books}/>
                    </div>
                    {/* làm thêm cái danh sách scahs vào đây */}
                    {/* <BookFeatur 
                        book={book}
                        handleEnrollNow={handleEnrollNow}
                    /> */}
                </div>
            </div>
        </div>
    );
};