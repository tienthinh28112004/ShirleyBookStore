import { Editor } from '@tinymce/tinymce-react';

const TinyMCE = ({value,onChange}) =>{
    return (
        <Editor 
            apiKey="cm51cl6xw3c00t5m2bxz14wnpjg7t099xh78agld5ouwfpdj"
            value={value}
            init={{
                height:300, //chiều cao của trình soạn thảo 300px
                menubar:false,//ẩn thanh menu(file,edit,view)
                //sforced_root_block: '', // Chặn tự động thêm thẻ <p>
                plugins:[//các plugin được bật
                    'advlist', //danh sách nâng cao (ordered/unordered list)
                    'autolink',//tự động biến URL thành liên kết
                    'lists',//Danh sách (ul/ol)
                    'link',//chèn liên kết
                    'image',//chèn ảnh
                    'charmap',//bảng ký tự đặc biệt
                    'preview',//xem trước nội dung
                    'anchor',//thêm anchor trong văn bản
                    'searchreplace',//Tìm và thay thế
                    'visualblocks',//Hiển thị khối HTML
                    'code',// Xem/sửa HTML gốc
                    'fullscreen',//Chế độ toàn màn hình
                    'insertdatetime',//Thêm ngày/giờ
                    'media',//chèn video/audio
                    'table',//chèn bảng
                    //'paste',//Tùy chọn gián nội dung
                    'help',//trợ giúp
                    'wordcount',//đếm từ
                ],
                toolbar://Các nút hiển thị trên thanh công cụ
                    'undo redo | formatselect | bold italic backcolor | '+
                    'alignleft aligncenter alignright alignjustify | '+
                    'bullist numlist outdent indent | removeformat | help',
                entity_encoding: 'raw', // Không mã hóa các ký tự đặc biệt
                content_style: 'body { margin: 0; }', // Loại bỏ margin mặc định
            }}
            onEditorChange={onChange}
        />
    );
};

export default TinyMCE;