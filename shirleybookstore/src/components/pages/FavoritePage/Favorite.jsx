import { useEffect, useState } from "react";
import { getFavorite, removeFavorite } from "../../../service/FavoriteService";
import { motion } from "framer-motion";
import ReactPaginate from "react-paginate";
import { ViewFavorite } from "./Component/ViewFavorite";

export const FavoriteBook = () =>{
    const accessToken = sessionStorage.getItem("accessToken");
    const [currentPage,setCurrentPage] = useState(1);
    const [totalPages,setTotalPages] = useState(0);
    const [favorites,setFavorites] = useState([]);
    const [loading,setLoading] = useState(true);
    const [error,setError] = useState(null);

    useEffect(()=>{
        document.title = "Favorite";
    });

    useEffect(()=>{
        const fetchFavorites = async() =>{
            setLoading(true);
            setError(null);

            try{
                const data = await getFavorite(currentPage,8);
                if(data.result && Array.isArray(data.result.items)){
                    setFavorites(data.result.items);
                    console.log(favorites);
                    setTotalPages(data.result.totalPages || 1);
                }else{
                    setFavorites([]);
                    setTotalPages(0);
                }
            }catch(error){
                console.error("Error fetching favorite course:",error);
                setFavorites([]);
                setTotalPages(0);
                setError("Failed to upload favorite courses");
            }finally{
                setLoading(false);
            }
        };
        fetchFavorites();
    },[accessToken,currentPage]);

    const handleDeleteFavorite = async (favoriteId) =>{
        try{
            await removeFavorite(favoriteId);
            //xóa bản ghi trùng với favoriteId và cập nhập lại danh sách favorites
            setFavorites((prevFavorites) =>
                prevFavorites.filter((favorite) => favorite.favoriteId !== favoriteId)
            );
        }catch(error){
            console.error("Error delete favorite:",error);
        }
    };
    const handlePageClick = (data) =>{
        setCurrentPage(data.selected + 1);
    }
    if(error){
        return <div>{error.message}</div>
    }

    return (
        <motion.div
            key={currentPage}
            initial={{opacity: 0,x: 50}}
            animate={{opacity: 1,x: 0}}
            exit={{opacity: 0, x:-50}}
            transition={{duration: 0.5}}
            className="content-page"
        >
            <div className="container">
                <h2 className="vip-title">Your Favorite Book</h2>
                <br />

                <ViewFavorite 
                    loading={loading}
                    favorites={favorites}
                    handleDeleteFavorite={handleDeleteFavorite}
                />
                <ReactPaginate 
                    previousLabel={"«"}
                    nextLabel={"»"}
                    breakLabel={"..."}
                    pageCount={totalPages}
                    marginPagesDisplayed={2}
                    pageRangeDisplayed={3}
                    onPageChange={handlePageClick}
                    forcePage={currentPage - 1}
                    containerClassName={"pagination pagination-lg justify-content-center"}
                    pageClassName={"page-item"}
                    pageLinkClassName={"page-link"}
                    previousClassName={"page-item"}
                    previousLinkClassName={"page-link"}
                    nextClassName={"page-item"}
                    nextLinkClassName={"page-link"}
                    breakClassName={"page-item"}
                    breakLinkClassName={"page-link"}
                    activeClassName={"active"}
                />
            </div>
        </motion.div>
    );
};

export default FavoriteBook;