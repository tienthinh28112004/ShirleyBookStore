import React, { useState, useEffect } from 'react';

const AddressAutocomplete = ({ value, onChange, onSelect }) => {
  const [suggestions, setSuggestions] = useState([]);
  const apiKey = 'XtNqkrCtkC0WMOotMiLxoOQJATiJcYiHOg1X6XPq';
  
  useEffect(() => {
    const fetchSuggestions = async () => {
      if (value.length < 2) {
        setSuggestions([]);
        return;
      }
      try {
        const response = await fetch(
          `https://rsapi.goong.io/Place/AutoComplete?api_key=${apiKey}&input=${encodeURIComponent(value)}`
        );
        const data = await response.json();
        if (data.status === 'OK') {
          setSuggestions(data.predictions);
        } else {
          setSuggestions([]);
        }
      } catch (error) {
        console.error('Lỗi khi gọi API Goong:', error);
        setSuggestions([]);
      }
    };

    const debounceTimeout = setTimeout(fetchSuggestions, 300);
    return () => clearTimeout(debounceTimeout);
  }, [value]);

  const handleSelect = (suggestion) => {
    let formattedAddress = suggestion.description; // fallback mặc định

    //Nếu compound có dữ liệu, tạo chuỗi địa chỉ rõ ràng hơn
    if (suggestion.compound) {
      const { commune, district, province } = suggestion.compound;
      const parts = [commune, district, province].filter(Boolean);
      if (parts.length > 0) {
        formattedAddress = parts.join(', ');
      }
    }

    onChange(formattedAddress); // Gửi chuỗi rõ ràng lên
    // console.log(formattedAddress);
    

    if (suggestion.compound) {
      onSelect({
        city: suggestion.compound.province || '',
        district: suggestion.compound.district || '',
        ward: suggestion.compound.commune || '',
      });
    }
    setSuggestions([]);
  };

  return (
    <div className="position-relative">
      <input
        type="text"
        className="form-control"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder="Nhập địa chỉ"
        required
      />
      {suggestions.length > 0 && (
        <div className="suggestions position-absolute w-100 bg-white border rounded mt-1" style={{ zIndex: 1000 }}>
          {suggestions.map((s, index) => (
            <div
              key={index}
              className="suggestion-item p-2"
              onClick={() => handleSelect(s)}
              style={{ cursor: 'pointer' }}
            >
              {s.description}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default AddressAutocomplete;
