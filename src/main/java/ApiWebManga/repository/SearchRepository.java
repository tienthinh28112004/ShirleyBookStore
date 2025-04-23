package ApiWebManga.repository;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.BookHasCategory;
import ApiWebManga.Entity.Category;
import ApiWebManga.Entity.User;
import ApiWebManga.dto.Response.BookDetailResponse;
import ApiWebManga.dto.Response.PageResponse;
import ApiWebManga.repository.criteria.SearchCriteria;
import ApiWebManga.repository.criteria.SearchCriteriaQueryConsumer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class SearchRepository {
    @PersistenceContext//giúp inject EntityManager vào Spring Boot để làm việc với JPA.(entityManager thì tránh nên dùng autowired)
    private EntityManager entityManager;// giúp thêm, sửa, xóa, truy vấn dữ liệu trên database.

    public PageResponse<List<BookDetailResponse>> getBookWithSortMultiFieldAndSearch(int page, int size, String sortBy, String authorName, String... search){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();//tạo truy vấn động
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);//tạo ra một truy vấn(criteriaQuery) để lấy dữ liệu entity Book bằng Jpa Criteria API
        Root<Book> root = criteriaQuery.from(Book.class);//xác định Book là bảng gốc trong truy vấn

        Predicate predicate = getPredicate(criteriaBuilder,root,search);

        //tìm kiếm tác giải theo tên hoạc email
        if(StringUtils.hasLength(authorName)){//nếu User không null hoặc ""
            log.info("Sort Book and Join User");
            Join<Book, User> userJoin = root.join("author");//root lúc naày đại diện cho bảng book vfa thực hiện inner join với bảng User qua khóa ngoại author
            Predicate likeToFullName = criteriaBuilder.like(userJoin.get("fullName"),"%"+authorName+"%");//dùng criteriaBuider để tạo predicate
            Predicate likeToEmail = criteriaBuilder.like(userJoin.get("email"),"%"+authorName+"%");

            Predicate finalPredicate = criteriaBuilder.or(likeToEmail,likeToFullName);//tìm kiếm theo email hoặc username

            //điều kiện where(tìm kiếm)
            //criteriaQuery.where(predicate,finalPredicate);//lọc với 2 tiêu chí
            predicate= criteriaBuilder.and(predicate,finalPredicate);
        }
        criteriaQuery.where(predicate);//áp dụng để tìm kiếm
        //điều kiện order(sắp xếp)
        List<Order> orderList=new ArrayList<>();
        if(sortBy !=null){
            String[] listSort = sortBy.split(",");
            //sử dụng list để sắp xếp
            for(String sort :listSort){
                Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)");
                Matcher matcher = pattern.matcher(sort);
                if(matcher.find()){
                    log.info("sort {}",sort);
                    String columnName = matcher.group(1);
                    if(matcher.group(3).equalsIgnoreCase("asc")){
                        orderList.add(criteriaBuilder.asc(root.get(columnName)));
                    }else{
                        orderList.add(criteriaBuilder.desc(root.get(columnName)));
                    }
                }
            }
        }
        if(!orderList.isEmpty()){
            criteriaQuery.orderBy(orderList);
        }
        List<Book> bookList = entityManager.createQuery(criteriaQuery)//criteriaQuery ở trên là Book
                .setFirstResult((page-1)*size)//xác định vị trí bản ghi đầu tiên
                .setMaxResults(size)//giới hạn số lượng bản ghi trả về
                .getResultList();

        Long totalElements = getTotalElements(authorName, search);

        return PageResponse.<List<BookDetailResponse>>builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .totalElements(totalElements)
                .items(bookList.stream().map(BookDetailResponse::convert).collect(Collectors.toList()))
                .build();
    }

    private Long getTotalElements(String userName, String... search){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();//tạo truy vấn động
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);//do giá trị trả về là Long nên ở đây truyền Long vào
        Root<Book> root = query.from(Book.class);//xác định book là thực thể chính trong truy vấn

        Predicate predicate = getPredicate(criteriaBuilder,root,search);

        if(StringUtils.hasLength(userName)){
            log.info("join book and user");
            Join<Book,User> userJoin = root.join("author");
            Predicate likeToFullName = criteriaBuilder.like(userJoin.get("fullName"),"%"+userName+"%");
            Predicate likeToEmail = criteriaBuilder.like(userJoin.get("email"),"%"+userName+"%");
            Predicate finalPre = criteriaBuilder.or(likeToFullName,likeToEmail);

            //nếu tồn tại Username thì tiìm kiếm bản ghi bao gồm cả điều kiện username
            predicate=criteriaBuilder.and(predicate,finalPre);//tìm kiếm bắt buộc phải gần đúng

        }
        query.select(criteriaBuilder.count(root)).where(predicate);
        return entityManager.createQuery(query)//sử dụng câu lệnh query để lâấy ra số lượng book phù hợp
                .getSingleResult();
    }
    private Predicate getPredicate(CriteriaBuilder criteriaBuilder,Root root, String... search){
        Predicate predicate = criteriaBuilder.conjunction();//khởi tạo predicate ban đầu bằng true

        List<SearchCriteria> criteriaList = new ArrayList<>();
        if(search != null){
            for(String s:search) {
                log.info(s);
                Pattern pattern = Pattern.compile("(\\w+?)([:<>!])(.*)");
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()){
                    //tìm kiếm theo danh mục(do danh mục là 1 list nên phải dùng cách này)
                    if(matcher.group(1).equalsIgnoreCase("category")){
                        // Join Book -> BookHasCategory -> Category
                        Join<Book, BookHasCategory> bookHasCategoryJoin = root.join("category");//join book với BookHascategory
                        Join<BookHasCategory, Category> categoryJoin = bookHasCategoryJoin.join("category");//join BookHasCategory với bảng category

                        Predicate categoryPredicate = criteriaBuilder.like(categoryJoin.get("name"),"%"+matcher.group(3)+"%");//lấy tên của dnah mục category để so sánh
                        predicate = criteriaBuilder.and(predicate,categoryPredicate);
                    }else{
                        //còn các trường còn lại không phải danh mục lên có thể tìm theo kiểu này
                        criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2),matcher.group(3)));
                    }
                }
            }
        }

        //xác định queryConsumer với giá trị mặc định ban đầu
        SearchCriteriaQueryConsumer queryConsumer = new SearchCriteriaQueryConsumer(criteriaBuilder,root,predicate);

        if(!criteriaList.isEmpty()){//nếu nhưu có search
            criteriaList.forEach(queryConsumer);//duyệt qua từng tiêu và áp dụng vào queryConsumer
            predicate =criteriaBuilder.and(predicate,queryConsumer.getPredicate());//lấy ra tiêu chí đã được tạo trong queryConsumer
        }
        return predicate;
    }
}
