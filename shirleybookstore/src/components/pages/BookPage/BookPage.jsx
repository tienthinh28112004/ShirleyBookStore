import { useEffect, useState } from "react"
import { SearchBook } from "../../../service/BookService";
import { motion } from 'framer-motion';
import { Search } from "./components/Search";
import { ViewBook } from "./components/ViewBook";
import ReactPaginate from "react-paginate";

export const Books = () =>{
    const [loading,setLoading] =useState(true);
    const [books,setBooks] = useState([]);
    const [currentPage,setCurrentPage] = useState(1);
    const [pageSize,setPageSize] = useState(12);
    const [totalPages,setTotalPages] =useState(0);
    // const [authorName,setAuthorName] =useState('');
    // const [sortBy,setSortBy] = useState('');
    // const [listSearch,setListSearch] = useState([]);
   const [searchParams, setSearchParams] = useState({
        listSearch: [], // điều kiện search (title, category, price...)
        sortBy: '',    // trường sắp xếp
        authorName: ''   // tác giả
    });

    useEffect(() =>{
        document.title='Books'
    })

    const fetchBooks = async ()=>{
        setLoading(true);
        try{
            const result = await SearchBook(currentPage,pageSize,searchParams.sortBy,searchParams.authorName,searchParams.listSearch);
            if(result && result.result){
                console.log(result)
                setBooks(result.result.items);
                setTotalPages(result.result.totalPages);
            }else{
                console.log(28)
                setBooks([]);
            }
        }catch (err){
            console.log(err);
        }finally{
            setLoading(false);
        }
    };
    console.log(books);
    useEffect(() =>{
        setCurrentPage(1);
        setPageSize(12);
    },[searchParams]);

    useEffect(() => {
        fetchBooks();
    // eslint-disable-next-line react-hooks/exhaustive-deps
    },[currentPage,pageSize,searchParams]);

    const handlePageClick = (data) =>{
        setCurrentPage(data.selected + 1);
    }
    return (
        <motion.div
            key={currentPage || searchParams}
            initial={{opacity: 0, x: 50}}
            animate={{opacity: 1, x: 0}}
            exit={{opacity: 0, x: -50}}
            transition={{duration: 0.5}}
        >
            <Search 
                books={books}
                setSearchParams={setSearchParams}
            />
            <div className="container-fluid">
                <div className="container py-3">
                    <ReactPaginate 
                        previousLabel={"«"}
                        nextLabel={"»"}
                        breakLabel={"..."}
                        pageCount={totalPages}
                        marginPagesDisplayed={2}
                        pageRangeDisplayed={3}
                        onPageChange={handlePageClick}
                        forcePage={currentPage - 1}
                        containerClassName={'pagination pagination-lg justify-content-center'}
                        pageClassName={'page-item'}
                        pageLinkClassName={'page-link'}
                        previousClassName={'page-item'}
                        previousLinkClassName={'page-link'}
                        nextClassName={'page-item'}
                        nextLinkClassName={'page-link'}
                        breakClassName={'page-item'}
                        breakLinkClassName={'page-link'}
                        activeClassName={'active'}
                    />
                </div>
            </div>
        </motion.div>
    )
}