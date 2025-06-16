import { FaCommentDots, FaReply, FaTrash } from 'react-icons/fa';

export const ReviewSection = ({
    comments,
    newComment,
    editingCommentId,
    setEditingCommentId,
    setNewComment,
    handleAddComment,
    handleReplyToggle,
    handleAddReply,
    handleEditComment,
    handleDeleteComment,
    editContent,
    setEditContent,
    replyContent,
    setReplyContent
}) =>{
    return (
        <div className="comments-section mt-5">
            <h2 className="mb-4 text-secondary"><FaCommentDots /> Comments</h2>
            {/* Add Comment */}
            <div className='comment-form mb-5'>
                <textarea 
                    className='form-control mb-3'
                    rows="3"
                    placeholder="Write your comment..."
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                />
                <div className="btn-block">
                    <button className="btn px-4 mt-3" onClick={handleAddComment}>
                        Submit Comment
                    </button>
                </div>
               
            </div>

            {/* Comment List */}
            <div className="comment-list">
                {comments.map((comment,index) =>(
                    <div key={index} className="comment-item mb-4 p-3 bg-light rounded shadow-sm">
                        <div className='d-flex align-items-center justify-content-between'>
                            <div className="d-flex align-items-center">
                                <img 
                                    src={comment.avatar || 'https://bootdey.com/img/Content/avatar/avatar7.png'}
                                    alt='User Avatar'
                                    className='rounded-circle mr-2'
                                    style={{width: '40px',height: '40px'}}
                                />
                                <h6 className='m-0'>{comment.name}</h6>
                            </div>
                            <div>
                                <button
                                    className="btn btn-sm btn-outline-info mr-2"
                                    onClick={()=>setEditContent({[comment.id]:comment.content})|| setEditingCommentId(comment.id)}
                                >
                                    <i className="fa fa-comment me-2"></i>
                                </button>
                                <button className='btn btn-sm btn-outline-danger' onClick={()=>handleDeleteComment(comment.id)}>
                                    <FaTrash />
                                </button>
                            </div>
                        </div>

                        {/* Comment Content */}
                        {editingCommentId === comment.id ? (
                            <div>
                                <textarea 
                                    className="form-control mt-3"
                                    rows={3}
                                    value={editContent[comment.id] !== undefined ? editContent[comment.id] : comment.content}
                                    onChange={(e) =>setEditContent({...editContent,[comment.id]: e.target.value})}
                                />
                                <button className="btn btn-success mt-2" onClick={() =>handleEditComment(comment.id,editContent[comment.id])}>
                                    Save
                                </button>
                            </div>
                        ):(
                            <p className='mt-2'>{comment.content}</p>
                        )}

                        {/* Replies Section */}
                        {comment.replies.length > 0 && (
                            <div className='replies mt-3 pl-5'>
                                {comment.replies.map((reply,replyIndex) =>(
                                    <div key={replyIndex} className="reply-item mb-2 pl-3">
                                        <div className='d-flex align-items-center justify-content-between'>
                                            <div className='d-flex align-items-center'>
                                                <img 
                                                    src={reply.avatar || 'https://bootdey.com/img/Content/avatar/avatar7.png'}
                                                    alt="User Avatar"
                                                    className="rounded-circle mr-2"
                                                    style={{width :'30px',height:'30px'}}
                                                />
                                                <h6 className="m-0">{reply.name}</h6>
                                            </div>
                                            <div>
                                                <button
                                                    className="btn btn-sm btn-outline-info mr-2"
                                                    onClick={() => setEditContent({[reply.id]:reply.content})|| setEditingCommentId(reply.id)}
                                                >
                                                    <i className="fa fa-comments me-2"></i>
                                                </button>
                                                <button className="btn btn-sm btn-outline-danger" onClick={()=>handleDeleteComment(reply.id)}>
                                                    <FaTrash />
                                                </button>
                                            </div>
                                        </div>

                                        {/* Reply Editing Feature */}
                                        {editingCommentId === reply.id ?(
                                            <div>
                                                <textarea 
                                                    className='form-control mt-3'
                                                    rows="3"
                                                    value={editContent[reply.id] !== undefined ? editContent[reply.id] : reply.content}
                                                    onChange={(e) => setEditContent({...editContent,[reply.id]: e.target.value})}
                                                />
                                                <button className='btn btn-success mt-2' onClick={() => handleEditComment(reply.id,editContent[reply.id])}>
                                                    Save
                                                </button>
                                            </div>
                                        ):(
                                            <p className="mt-2">{reply.content}</p>
                                        )}
                                    </div>
                                ))}
                            </div>
                        )}

                        {/* Reply Form */}
                        {comment.replying && (
                            <div className="comment-reply-form mt-4">
                                <textarea 
                                    className="form-control mb-3"
                                    rows="3"
                                    placeholder="Write your reply..."
                                    value={replyContent[comment.id] || ""}
                                    onChange={(e) => setReplyContent({...replyContent,[comment.id]:e.target.value})}
                                />
                                <button className="btn btn-primary px-4" onClick={() =>handleAddReply(comment.id)}>
                                    Submit Reply
                                </button>
                            </div>
                        )}

                        {/* Reply button after replies */}
                        <div className="comment-actions d-flex justify-content-end mt-2">
                            <button className="btn btn-sm btn-outline-info" onClick={() => handleReplyToggle(comment.id)}>
                                <FaReply className="mr-1 reply-review"/> Reply
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};