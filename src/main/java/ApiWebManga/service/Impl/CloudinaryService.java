package ApiWebManga.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file){//xử lý file
        try{
            var result =cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(//chuyển dữ liệu sang dạng byte rồi upload
                    "folder", "/upload",//ảnh sẽ được lưu trong thư mục /upload (có thể thay thế bằng thư mục khác vì đoạn code nếu không tìm thấy thư mục chỉ định trong cloudinay sẽ tự động tạo thư mục mới)
                    "use_filename", true,//sử dụng tên file gốc nếu có thể(vì upload nên sẽ lấy tên file khác nên phải để là true để nó lấy ược tên thư mục gốc)
                    "unique_filename", true,//nếu có nhiều thư mục trùng tên nhau thì cloudinay sẽ tự động tạo tên file duy nhất để tránh truùng lặp
                    "resource_type","auto"//tự động xác định lại file hình ảnh,video,âm thanh...
            ));
            //=>result lúc này sẽ có các thuộc tinh "secure_url"(url an toàn của ảnh),public_id (id duy nhất của ảnh),format(định dạng file),width,height(kích thước của ảnh)
            return result.get("secure_url").toString();//ở đây chúng ta lấy ra đường dẫn và chuyển nso sang string
        } catch (IOException e) {
            throw new RuntimeException("Image upload fail");
        }
    }
}
