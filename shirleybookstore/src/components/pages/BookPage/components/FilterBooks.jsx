import React, { useState } from 'react';

const ProductFilter = ({categorySearch,setCategorySearch,listCategory,minPrice,maxPrice,setMinPrice,setMaxPrice}) => {
//   const [selectedCategory, setSelectedCategory] = useState('');
//   const [minPrice, setMinPrice] = useState(0);
//   const [maxPrice, setMaxPrice] = useState(1000000);
  
  // Giá trị min-max của slider
  const PRICE_MIN = 0;
  const PRICE_MAX = 1000000;

//   const categories = [
//     { id: 'sach-tieng-viet', name: 'Sách Tiếng Việt', count: 3465 },
//     { id: 'foreign-books', name: 'Foreign Books', count: 2 },
//     { id: 'do-choi', name: 'Đồ Chơi', count: 1 }
//   ];

  const priceRanges = [
    { id: '0-150000', label: '0 - 150,000đ', count: 3465, min: 0, max: 150000 },
    { id: '150000-300000', label: '150,000đ - 300,000đ', count: 76, min: 150000, max: 300000 },
    { id: '300000-500000', label: '300,000đ - 500,000đ', count: 14, min: 300000, max: 500000 },
    { id: '500000-700000', label: '500,000đ - 700,000đ', count: 2, min: 500000, max: 700000 },
    { id: '700000-above', label: '700,000đ - 1,000,000', count: 1, min: 700000, max: 1000000 }
  ];

  const handleCategoryChange = (id, name) => {
    setCategorySearch(prev =>
        prev.includes(`category:${name}`)
        ? prev.filter(item => item !== `category:${name}`) // bỏ chọn nếu đã có
        : [...prev, `category:${name}`] // thêm nếu chưa có
    );
  };

  const handlePriceRangeSelect = (range) => {
    setMinPrice(range.min);
    setMaxPrice(range.max || PRICE_MAX);
  };

  const calculateSliderPosition = (value) => {
    return ((value - PRICE_MIN) / (PRICE_MAX - PRICE_MIN)) * 100;
  };

  const handleSliderChange = (values) => {
    // Sắp xếp lại giá trị để giá trị lớn hơn luôn là max
    const sortedValues = values.sort((a, b) => a - b);
    setMaxPrice(sortedValues[1]);
    setMinPrice(sortedValues[0]);
  };

  const resetPriceFilter = () => {
    setMinPrice(PRICE_MIN);
    setMaxPrice(PRICE_MAX);
  };

  return (
    <div className="p-4 border rounded-lg">
      <h2 className="text-xl font-bold mb-4 text-red-600" style={{color:"#f2291b",textAlign:'center'}}>LỌC THEO</h2>
      
      <div className="mb-4">
        <h3 className="mb-2" style={{fontSize:"25px"}}>DANH MỤC CHÍNH</h3>
        {listCategory.map(category => (
          <div key={category.id} className="flex items-center mb-1">
              <input
                  type="checkbox"
                  id={category.id}
                  checked={categorySearch.includes(`category:${category.name}`)}
                  onChange={() => handleCategoryChange(category.id, category.name)}
                  className="mr-2"
              />
              <label htmlFor={category.id} className="flex-grow">
                  {category.name}
              </label>
          </div>
        ))}
      </div>
      
      <div>
        <h3 className="mb-2" style={{fontSize:"25px"}}>GIÁ</h3>
        {priceRanges.map(range => (
          <div key={range.id} className="flex items-center mb-1">
            <input
              type="radio"
              id={range.id}
              name="price-range"
              checked={minPrice === range.min && (maxPrice === range.max || range.max === null)}
              className="mr-2"
              onChange={() => handlePriceRangeSelect(range)}
            />
            <label htmlFor={range.id} className="flex-grow">
              {range.label} ({range.count})
            </label>
          </div>
        ))}
        
       <div className="mt-4 relative">
          <h4 className="font-semibold mb-2">Hoặc chọn mức giá phù hợp</h4>
          {/* Manual Price Input */}
          <div className="flex items-center space-x-2 mt-4">
            <input
              type="number"
              value={minPrice}
              style={{width:"100px"}}
              onChange={(e) => {
                const value = Number(e.target.value);
                setMinPrice(Math.min(Math.max(value, PRICE_MIN), maxPrice - 1000));
              }}
              className="w-24 p-1 border rounded"
              placeholder="Từ"
            />
            <span> - </span>
            <input
              type="number"
              value={maxPrice}
              style={{width:"100px"}}
              onChange={(e) => {
                const value = Number(e.target.value);
                setMaxPrice(Math.max(Math.min(value, PRICE_MAX), minPrice + 1000));
              }}
              className="w-24 p-1 border rounded"
              placeholder="Đến"
            />
            <button
              onClick={resetPriceFilter}
              style={{background:"#f2291b"}}
              className="ml-2 px-2 py-1 bg-red-500 text-white rounded text-sm"
            >
              Xóa
            </button>
          </div>
        </div>

        {/* Hiển thị thông tin lọc hiện tại */}
        <div className="mt-4 text-sm text-gray-600">
            <div>
                <b>Danh mục:</b>{" "}
                {categorySearch.length > 0
                    ? categorySearch
                        .map(id => listCategory.find(c => c.id === id)?.name)
                        .filter(Boolean)
                        .join(", ")
                    : "Không có"}
            </div>
          <div>
            <b>Giá: </b>Từ {minPrice}đ - Đến {maxPrice}đ
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductFilter;