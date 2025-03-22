package ApiWebManga.service;

import ApiWebManga.Entity.BookElasticSearch;
import org.springframework.kafka.support.Acknowledgment;


public interface KafkaService {
    void saveBookToElasticSearch(BookElasticSearch bookElasticSearch, Acknowledgment acknowledgement);
    //chú  import đúng Acknowledgment
}
