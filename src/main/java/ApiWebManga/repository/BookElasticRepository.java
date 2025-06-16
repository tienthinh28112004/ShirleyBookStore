package ApiWebManga.repository;

import ApiWebManga.Entity.BookElasticSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookElasticRepository extends ElasticsearchRepository<BookElasticSearch,Long> {
}
