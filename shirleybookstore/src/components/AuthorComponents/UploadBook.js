import { useEffect, useState } from "react";
import { FaUpload, FaBook, FaTags, FaDollarSign, FaFileAlt, FaClock, FaImage } from 'react-icons/fa';
import { listAllCategory } from "../../service/CategoryService";
import { Select } from "antd";
import { uploadBook } from "../../service/BookService";
import TinyMCE from "../../utils/TinyMCE";
export const UploadBook = () =>{
    const [listCategory, setListCategory] = useState([]);
    const [categorySearch, setCategorySearch] = useState([]);
    const [bookTitle,setBookTitle] = useState('');
    const [bookIsbn,setBookIsbn]=useState('');
    const [bookDescription,setBookDescription]= useState('');
    const [bookPrice,setBookPrice] = useState("");
    const [bookThumbnail,setBookThumbnail]=useState(null);
    const [bookPdf,setBookPdf] = useState(null);

    useEffect(() =>{
        document.title = "Create Book By Author"
    })

    useEffect(() => {
            const listCategories = async () => {
                try {
                    const data = await listAllCategory();
                    if (data.result && Array.isArray(data.result)) {
                        setListCategory(data.result);
                    }
                } catch (error) {
                    console.error("Error fetching favorite course:", error);
                }
            };
            listCategories();
        }, []);

    // Hàm reset form về giá trị mặc định
    const resetForm = () => {
        setBookPdf(null);
        setBookTitle('');
        setBookIsbn('');
        setBookDescription('');
        setBookPrice('');
        setCategorySearch([]);
        setBookThumbnail(null);
        
        // Reset file input
        const fileInput = document.getElementById('bookThumbnail');
        if (fileInput) {
            fileInput.value = '';
        }
        const fileInputBook = document.getElementById('bookPdf');
        if (fileInput) {
            fileInputBook.value = '';
        }
    };

    const handleSubmit = async (e) =>{
        e.preventDefault();
        try {
            // Prepare book data object
            const bookData = {
              title: bookTitle,
              description: bookDescription,
              isbn: bookIsbn,
              price: bookPrice,
              categoriesId: categorySearch // Assuming your API expects category IDs
            };
            
            // Call the upload function with book data and thumbnail, but no PDF
            const result = await uploadBook(bookData, bookThumbnail,bookPdf);
            
            // Reset form sau khi upload thành công
            resetForm();
            
          } catch (error) {
            console.error("Upload failed:", error);
            // Handle error - display message to user
          }
        console.log({bookTitle,bookIsbn,bookDescription,bookPrice,categorySearch,bookThumbnail})
    };
    return (
        <div className="upload-book-container" style={{marginTop:"150px"}}>
            <div className="container upload-container my-5">
                <div className="row justify-content-center">
                    <div className="col-lg-7 col-md-9">
                        <div className="card shadow-lg border-0 rounded-4 bg-light">
                            <div className="card-body p-5">
                                <h3 className="card-title text-center mb-4 text-dark fw-bold">
                                    Upload New Book
                                </h3>
                                <form onSubmit={handleSubmit}>
                                    <div className="mb-4">
                                        <label htmlFor="bookTitle" className="form-label fs-5 text-dark fw-semibold">
                                            Tên sách
                                        </label>
                                        <input 
                                            type="text"
                                            className="form-control shadow-sm"
                                            id="bookTitle"
                                            placeholder="Enter the title of the book"
                                            value={bookTitle}
                                            onChange={(e) =>setBookTitle(e.target.value)}
                                        />
                                    </div>
                                    <div className="mb-4">
                                        <label htmlFor="bookIsbn" className="form-label fs-5 text-dark fw-semibold">
                                           Mã số chuẩn quốc tế của sách
                                        </label>
                                        <input 
                                            type="text"
                                            className="form-control shadow-sm"
                                            id="bookIsbn"
                                            placeholder="Enter the isbn of the book"
                                            value={bookIsbn}
                                            onChange={(e) =>setBookIsbn(e.target.value)}
                                        />
                                    </div>
                                    <div className="mb-4">
                                        <label htmlFor="bookDescription" className="form-label fs-5 text-dark fw-semibold">
                                            Mô tả sách
                                        </label>
                                       <TinyMCE value={bookDescription} onChange={setBookDescription}/>
                                    </div>
                                    
                                    <div className="mb-4">
                                        <label htmlFor="bookCategory" className="form-label fs-5 text-dark fw-semibold">
                                            Chọn danh mục
                                        </label>
                                        <Select
                                                mode="multiple"
                                                placeholder="Select categories"
                                                value={categorySearch}
                                                onChange={(e) => setCategorySearch(e)}
                                                options={listCategory.map((category) => ({
                                                    value: `${category.id}`,
                                                    label: `${category.name}`
                                                }))}
                                                className="form-select shadow-sm"
                                                // className="w-100"
                                                // style={{ height: '100%' }}
                                                // popupMatchSelectWidth={false}
                                            />
                                    </div>
                                    <div className="mb-4">
                                        <label htmlFor="bookPrice" className="form-label fs-5 text-dark fw-semibold">
                                            Giá (VND)
                                        </label>
                                        <input 
                                            type="number"
                                            className="form-control shadow-sm"
                                            id="bookPrice"
                                            placeholder="Enter the price of the book"
                                            value={bookPrice}
                                            onChange={(e) => setBookPrice(e.target.value)}
                                            min="0"
                                        />
                                    </div>
                                    <div className="mb-4">
                                        <label htmlFor="bookThumbnail" className="form-label fs-5 text-dark fw-semibold">
                                            Bìa sách
                                        </label>
                                        <input 
                                            className="form-control shadow-sm"
                                            type="file"
                                            id="bookThumbnail"
                                            onChange={(e) => setBookThumbnail(e.target.files[0])}
                                        />
                                    </div>
                                    <div className="mb-4">
                                        <label htmlFor="bookPdf" className="form-label fs-5 text-dark fw-semibold">
                                            File sách (PDF)
                                        </label>
                                        <input 
                                            className="form-control shadow-sm"
                                            type="file"
                                            id="bookPdf"
                                            onChange={(e) => setBookPdf(e.target.files[0])}
                                        />
                                    </div>
                                    <div className="d-grid btn-block">
                                        <button type="submit" className="btn btn-lg shadow-sm upload-btn">
                                            <FaUpload className="me-2"/> Upload Book
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}