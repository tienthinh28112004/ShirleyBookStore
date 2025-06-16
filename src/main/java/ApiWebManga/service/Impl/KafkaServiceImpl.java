package ApiWebManga.service.Impl;

import ApiWebManga.Entity.BookElasticSearch;
import ApiWebManga.repository.BookElasticRepository;
import ApiWebManga.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {
    private final BookElasticRepository bookElasticRepository;
    @Override
    @KafkaListener(topics = "save-to-elastic-search",groupId = "book-elastic-search")

    public void saveBookToElasticSearch(BookElasticSearch bookElasticSearch, Acknowledgment acknowledgement) {
        try{
            if(bookElasticSearch !=null && bookElasticRepository.existsById(bookElasticSearch.getId())){
                bookElasticRepository.save(bookElasticSearch);
                acknowledgement.acknowledge();
            }
        }catch (Exception e){
            throw  e;
        }
    }
}
