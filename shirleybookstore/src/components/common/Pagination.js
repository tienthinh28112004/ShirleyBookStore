export const Pagination = ({currentPage,totalPages,changePage}) =>{
    return (
        <div className="col-12">
            <nav aria-label="Page navigation">
                <ul className="pagination pagination-lg justify-content-center mb-0">
                    {/* Previous button */}
                    <li className={`page-item ${currentPage===1}?"disabled":""`}>
                        <button className="page-link rounded-0" onClick={()=>changePage(currentPage - 1)} aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </button>
                    </li>
                    {/* Render dynamic page numbers */}
                    {Array.from({length: totalPages},(_,index) =>(//tạo ra 1 mảng có độ dài totalPage và ánh xạ qua nó
                        <li key={index + 1} className={`page-item ${currentPage = index+1?"active":""}`}>
                            <button className="page-link" onClick={()=>changePage(index + 1)}>
                                {index + 1}
                            </button>
                        </li>
                    ))}
                    {/* Next button */}
                    <li className={`page-item ${currentPage === totalPages ? "disabled":""}`}>
                        <button className="page-link rounded-0" onClick={() =>changePage(currentPage+1)} aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </button>
                    </li>
                </ul>
            </nav>
        </div>
    );
};