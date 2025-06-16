package ApiWebManga.repository.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Consumer;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteriaQueryConsumer implements Consumer<SearchCriteria> {

    private CriteriaBuilder criteriaBuilder;
    private Root<?> root;
    private Predicate predicate;
    @Override
    public void accept(SearchCriteria param) {//vd title:conan
        switch (param.getOperation()){
            case ":" ->{
                if(root.get(param.getKey()).getJavaType().equals(String.class)){
                    predicate = criteriaBuilder.and(predicate,criteriaBuilder.like(root.get(param.getKey()),"%"+param.getValue()+"%"));
                }else {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(param.getKey()), param.getValue()));
                }
            }
            case "!" ->
                predicate = criteriaBuilder.and(predicate,criteriaBuilder.notEqual(root.get(param.getKey()),param.getValue()));
            case ">" ->
                predicate = criteriaBuilder.and(predicate,criteriaBuilder.greaterThanOrEqualTo(root.get(param.getKey()),param.getValue().toString()));//nếu kiểu dữ liệu là Integer hoặc date thì cần ép kiểu
            case "<" ->
                predicate = criteriaBuilder.and(predicate,criteriaBuilder.lessThanOrEqualTo(root.get(param.getKey()),param.getValue().toString()));
        }

    }
}
