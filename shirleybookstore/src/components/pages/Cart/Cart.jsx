import React, { useEffect, useState } from 'react';
import { CartDetail } from './componnent/CartDetail';
import { addItemCart, deleteAllItemcart, deleteItemcart, detailcart } from '../../../service/CartService';
import { getAllBook } from '../../../service/BookService';
// If you're using the Font Awesome 4.7 in your project, make sure you've imported it in your main CSS or index file

const ShoppingCart = () => {
    const [carts,setCarts] = useState([]);
    const accessToken = sessionStorage.getItem("accessToken");
    const [totalMoney,setTotalMoney] = useState(0);
    const [loading,setLoading] = useState(true);
    const [listBook,setListBook]=useState([]);
    useEffect(()=>{
        document.title = "Cart";
    });

    useEffect(()=>{
        const fetchCarts = async() =>{
            setLoading(true);

            try{
                const data = await detailcart();
                if(data.result && Array.isArray(data.result.items)){
                    setTotalMoney(data.result.totalMoney);
                    console.log(totalMoney);
                    setCarts(data.result.items);
                }else{
                    setTotalMoney(0);
                    setCarts([]);
                }
            }catch(error){
                console.error("Error fetching favorite course:",error);
                setCarts([]);
            }finally{
                setLoading(false);
            }
        };
        fetchCarts();
    },[accessToken]);

    const updateCart = async(bookId, quantity) => {
        setLoading(true);
        try {
            const response = await addItemCart(bookId, quantity);
            if(response.result && Array.isArray(response.result.items)) {
                setTotalMoney(response.result.totalMoney)
                setCarts(response.result.items);
                console.log(totalMoney);
            } else {
                setTotalMoney(0)
                setCarts([]);
            }
        } catch(error) {
            console.error("Error updating cart:", error);
            setCarts([]);
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteBookcart = async (bookId) =>{
            try{
                await deleteItemcart(bookId);
                //xóa bản ghi trùng với favoriteId và cập nhập lại danh sách favorites
                setCarts((prevCarts) =>
                    prevCarts.filter((book) => book.bookId !== bookId)
                );                
            }catch(error){
                console.error("Error delete favorite:",error);
            }
        };

        const clearcart = async () =>{
            try{
                await deleteAllItemcart();
                setCarts([]);           
            }catch(error){
                console.error("Error delete favorite:",error);
            }
        };

     const fetchBooks = async ()=>{
            try{
                console.log(1);
                const result = await getAllBook(1,12);
                console.log(result);
                if(result && result.result){
                    setListBook(result.result.items);
                }else{
                    setListBook([]);
                }
            }catch (err){
                console.log(err);
            }
    };
    useEffect(() => {
        fetchBooks();
    },[]);

        return (
              <div className='py-3'>
                <CartDetail
                  deleteItem={handleDeleteBookcart}
                  updateItem={updateCart}
                  books={carts}
                  clearCart={clearcart}
                  totalMoney={totalMoney}
                  listBook={listBook}
                />
            </div>
        );
};

export default ShoppingCart;