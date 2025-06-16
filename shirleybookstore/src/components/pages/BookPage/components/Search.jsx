import { useEffect, useState } from "react";
import { Select } from 'antd';
import { listAllCategory } from "../../../../service/CategoryService";
import ProductFilter from "./FilterBooks";
import { ViewBook } from "./ViewBook";

export const Search = ({ books,setSearchParams }) => {
    const [listCategory, setListCategory] = useState([]);
    const [categorySearch, setCategorySearch] = useState([]);
    const [title, setTitle] = useState('');
    const [minPrice, setMinPrice] = useState(0);
    const [maxPrice, setMaxPrice] = useState(1000000);
    const [sort, setSort] = useState('');
    const [author, setAuthor] = useState('');
 

    console.log(books);
    
    const handleSearch = () => {
        let filters = [];

        // Xây dựng điều kiện tìm kiếm
        if (title && title.trim() !== '') {
            filters.push(`title:${title}`);
        }

        if (categorySearch.length > 0) {
            filters.push(...categorySearch);
        }

        if (maxPrice !== null && maxPrice !== '') {
            filters.push(`price<${maxPrice}`);
        }
        
        if (minPrice !== null && minPrice !== '') {
            filters.push(`price>${minPrice}`);
        }

        // Cập nhật tất cả params cùng lúc
        setSearchParams({
            listSearch:filters,
            sortBy: sort || '', // Giữ nguyên sort cũ nếu không có giá trị mới
            authorName: author || '' // Giữ nguyên author cũ nếu không có giá trị mới
        });
    };

    // // Xử lý thay đổi sort ngay khi select thay đổi
    const handleSortChange = (value) => {
        setSort(value);
        setSearchParams(prev => ({
            ...prev,
            sort: value
        }));
    };

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
    // console.log(listCategory);/
    return (
        <>
             {/* <div className="container"> */}
            <div className="row gutters" style={{width:"100%",marginTop:"200px"}}>
                {/* Cột trái */}
                <div className="col-xl-3 col-lg-3 col-md-12 col-sm-12 col-12">
                    <div className="card h-100">
                        <div className="card-body">
                            <ProductFilter
                                categorySearch={categorySearch}
                                setCategorySearch={setCategorySearch}
                                listCategory={listCategory}
                                minPrice={minPrice}
                                maxPrice={maxPrice}
                                setMinPrice={setMinPrice}
                                setMaxPrice={setMaxPrice}
                            />
                        </div>
                    </div>
                </div>
                <div className="col-xl-9 col-lg-9 col-md-12 col-sm-12 col-12">
                    <div className="card h-100">
                        <div className="card-body">
                           <div className="row align-items-center" style={{marginBottom:"20px"}}>
                                <div className="col-xl-9 col-lg-9 col-md-9 col-sm-9 d-flex">
                                    <input 
                                    type="text"
                                    className="form-control search-input custom-input w-100"
                                    placeholder="Search by Title"
                                    value={title}
                                    onChange={(e) => setTitle(e.target.value)}
                                    />
                                </div>
                                <div className="col-xl-3 col-lg-3 col-md-3 col-sm-3">
                                    <button 
                                    className="w-100 custom-btn"
                                    style={{background:"#f2291b"}}
                                    onClick={handleSearch}
                                    >
                                    <i className="fa fa-search me-2" style={{borderRadius:"10px",fontSize:"20px"}}></i>
                                    Search
                                    </button>
                                </div>
                            </div>
                            <div className="col-xl-4 col-lg-4 col-md-6 col-sm-12 d-flex align-items-center mb-2">
                                <label className="me-2 text-nowrap" style={{fontSize:"20px"}}>Sắp xếp theo : </label>
                                <select
                                    className="form-control search-input custom-input"
                                    value={sort}
                                    style={{width:"800px"}}
                                    onChange={(e) => setSort(e.target.value)}
                                >
                                    <option value="">Mặc định</option>
                                    <option value="title:asc">Sort By:Name (A-Z)</option>
                                    <option value="title:desc">Sort By:Name(Z-A)</option>
                                    <option value="price:asc">Sort By:Price (Low &gt; High)</option>
                                    <option value="price:desc">Sort By:Price (Hight &gt; Low)</option>
                                    <option value="id:desc">Sort By:Truyện mới nhất</option>
                                    <option value="id:asc">Sort By:Truyện xưa nhất</option>
                                </select>
                            </div>
                            <div className="row mx-0 justify-content-center">
                                <div className="col-lg-8">
                                    <div className="section-title text-center position-relative mb-5">
                                        <h5 className="display-4" style={{fontSize:"50px"}}>Explore Our Latest Book</h5>
                                    </div>
                                </div>
                            </div>
                            <ViewBook books={books} />
                        </div>
                    </div>
                </div>
            </div>
           
        </>
    );
};