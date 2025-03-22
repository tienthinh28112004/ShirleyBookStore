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

    private CriteriaBuilder criteriaBuilder;//Tạo điều kiện truy vấn(predicate)
    private Root<?> root;//đại diện cho các entity,model trong truy vấn
    private Predicate predicate;//đều kiện dùng để lọc trả về true false
    @Override
    public void accept(SearchCriteria param) {//vd title:conan
        switch (param.getOperation()){
            case ":" ->{
                if(root.get(param.getKey()).getJavaType().equals(String.class)){//nếu là String
                    //nếu là String dùng like để tìm kiếm gần đúng
                    predicate = criteriaBuilder.and(predicate,criteriaBuilder.like(root.get(param.getKey()),"%"+param.getValue()+"%"));
                }else {
                    //nếu không phải string duùng equal để so sánh trực tiếp
                    predicate = criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get(param.getKey()),param.getValue()));
                }
                //nếu param.getKey() là tên các cột các trường thì root.get(param.getKey()) đại diện cho tên các cột các trường ấy
            }
            case "!" ->
                predicate = criteriaBuilder.and(predicate,criteriaBuilder.notEqual(root.get(param.getKey()),param.getValue()));
            case ">" ->
                predicate = criteriaBuilder.and(predicate,criteriaBuilder.greaterThanOrEqualTo(root.get(param.getKey()),param.getValue().toString()));//nếu kiểu dữ liệu là Integer hoặc date thì cần ép kiểu
            case "<" ->
                predicate = criteriaBuilder.and(predicate,criteriaBuilder.lessThanOrEqualTo(root.get(param.getKey()),param.getValue().toString()));
        }
        //root.get(param.getKey()): Lấy thuộc tính của entity dựa vào key (param.getKey()),nếu ban đầu param.getKey là title thì ở đây dùng root.get(param.getKey()) sẽ lấy được giá trị bên trong nó để so sánh(ví dụ như conan,onepice)
        //criteriaBuilder.notEqual(root.get(param.getKey()),param.getValue()):điều kiện lặp là giá trị root.get(param.getKey()) khác với param.getValue()
        //criteriaBuilder.and(predicate, condition):Kết hợp điều kiện mới với điều kiện cũ bằng phép AND.ban ầu predicate có thể là null nhưng sau ta dùng phép and để kết hopwj thêm các điều kiện vào
    }
}
