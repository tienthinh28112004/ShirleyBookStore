package ApiWebManga.service.Impl;

import ApiWebManga.Entity.BookElasticSearch;
//import ApiWebManga.repository.BookElasticRepository;
import ApiWebManga.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {
//    private final BookElasticRepository bookElasticRepository;
    @Override
    @KafkaListener(topics = "save-to-elastic-search",groupId = "book-elastic-search")
    //groupId được định nghĩa khi lắng nghe kafka,nếu nhiều consummer có cùng groupId,kafka sẽ chia message giữa chúng(cân bằng tải),
    // nếu không có groupId mỗi consummer đều nhận tất car từ message từ topic(không có chia sẻ công việc)
    public void saveBookToElasticSearch(BookElasticSearch bookElasticSearch, Acknowledgment acknowledgement) {
        try{
//            if(bookElasticSearch !=null && bookElasticRepository.existsById(bookElasticSearch.getId())){
//                //nêu sách không lỗi và chưa được lưu lần nào thfi sẽ được lưu
//                bookElasticRepository.save(bookElasticSearch);
//                //do chúng ta đặt ack là manual nên chúng ta phải tự xác nhận thành công nêếu lưu xong(còn có các dạng record,count,time,batch,manual_immediate)
//                acknowledgement.acknowledge();//đánh đấu ghi lại thành công
//            }
        }catch (Exception e){
            throw  e;//nếu có lỗi ném ra ngoại lệ để kafka tự retry(khả năng của kafka là tự  retry lại nếu gửi lỗi)
        }
    }
}
