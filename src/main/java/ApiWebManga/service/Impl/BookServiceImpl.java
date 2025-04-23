package ApiWebManga.service.Impl;

import ApiWebManga.Entity.*;
import ApiWebManga.Exception.BadCredentialException;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Utils.SecurityUtils;
import ApiWebManga.dto.Request.BookCreationRequest;
import ApiWebManga.dto.Response.BookDetailResponse;
import ApiWebManga.dto.Response.PageResponse;
import ApiWebManga.repository.BookRepository;
import ApiWebManga.repository.CategoryRepository;
import ApiWebManga.repository.SearchRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.BookService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final BookRepository bookRepository;
    private final SearchRepository searchRepository;
    private final CategoryRepository categoryRepository;
    private final KafkaTemplate<String,Object> kafkaTemplate;
    @Override
    public BookDetailResponse uploadBook(BookCreationRequest request,
                                         MultipartFile thumbnail,
                                         MultipartFile bookPdf) {
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(() -> new BadCredentialException("unauthorized"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        //phải là author mới có thể tạo sách
//        int author=0;
//        List<UserHasRoles> userHasRolesList=new ArrayList<>(user.getUserHasRoles());
//        for(int i=0;i<userHasRolesList.size();i++){
//            if(userHasRolesList.get(i).getRole().getName().equalsIgnoreCase("author")){
//                author=1;
//            }
//        }
//        if(author==0) throw new BadCredentialException("Only authors can add books");
        log.info("Upload book start ...!");
        String thumbnailUrl = null;
        String bookPath = null;
        if (thumbnail != null) {
            thumbnailUrl = cloudinaryService.uploadImage(thumbnail);
        }
        if (bookPdf != null) {
            bookPath = cloudinaryService.uploadImage(bookPdf);
        }
        Book book = Book.builder()
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .description(request.getDescription())
                .price(request.getPrice())
                .thumbnail(thumbnailUrl)
                .bookPath(bookPath)
                .author(user)
                .publisher(email)
                .build();
        List<BookHasCategory> bookHasCategories = new ArrayList<>();
        //request.getCategoriesId().stream().map(categoryRepository::findById).forEach(bookHasCategories.);
        for(int i=0;i<request.getCategoriesId().size();i++){
            Category category = categoryRepository.findById(request.getCategoriesId().get(i))
                    .orElseThrow(()->new NotFoundException("Category not found"));
            bookHasCategories.add(BookHasCategory.builder()
                            .book(book)
                            .category(category)
                    .build());
        }
        book.setCategory(bookHasCategories);
        log.info("");
        bookRepository.save(book);

        BookElasticSearch bookElasticSearch=BookElasticSearch.builder()
                .id(book.getId())
                .title(book.getTitle())
                .price(book.getPrice())
                .isbn(book.getIsbn())
                .authorName(book.getAuthor().getFullName())
                .description(book.getDescription())
                .build();
        //kafkaTemplate là một Spring Kafka helper giúp bạn gửi message đến Kafka dễ dàng.
        //kafkaTemplate.send("save-to-elastic-search",bookElasticSearch);đã kịp làm đâu:))
        //gửi thông báo sang kafka để lưu bookElasticSerch vào topic save-to-elastic-search,tiếp tục xử lí tại kafkaService
        //**quy trình xử lý của kafka

        //1,Producer (bên gửi - bạn đang làm)
        //KafkaTemplate gửi đối tượng bookElasticSearch đến Kafka.
        //Kafka sẽ lưu object này vào topic "save-to-elastic-search".
        // Kafka tự động chia message vào các partitions (nếu có nhiều).(ở đây đã fix là 3 trong phần producer)

        //2,Broker (Kafka server)
        //Nhận message và phân phối nó vào partitions thích hợp trong topic.
        //Nếu topic có 3 partitions, Kafka chọn 1 trong 3 để lưu message.

        //3,Consumer (bên nhận - cần @KafkaListener)
        //Consumer lắng nghe "save-to-elastic-search".
        //Khi có message mới, Kafka gửi nó đến một consumer trong nhóm (groupId).
        //Consumer nhận được bookElasticSearch và xử lý nó (ví dụ: lưu vào Elasticsearch).
        return BookDetailResponse.convert(book);
    }

    @Override
    public BookDetailResponse getBookId(Long id) {
        log.info("Get Book By Id {}",id);
        Book book=bookRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Book not found"));
        return BookDetailResponse.convert(book);
    }

    @Override
    public PageResponse<List<BookDetailResponse>> getAllBook(int page, int size) {
        Pageable pageable= PageRequest.of(page -1,size);//để cho khi fontend lấy trang từ 1
        Page<Book> bookPage = bookRepository.findAll(pageable);

        List<Book> books =bookPage.getContent();

        List<BookDetailResponse> bookDetailResponses=books.stream().map(BookDetailResponse::convert).collect(Collectors.toList());

        return PageResponse.<List<BookDetailResponse>>builder()
                .pageNo(page)//curentPage
                .pageSize(pageable.getPageSize())
                .totalPages(bookPage.getTotalPages())
                .items(bookDetailResponses)//???
                .totalElements(bookPage.getTotalElements())
                .build();
        //books là 1 danh sách sao wor đây không tolisst được@@@
    }

    @Override
    public PageResponse<List<BookDetailResponse>> getBoolWithSortAndMultiFieldAndSearch(int page, int size, String sortBy , String authorName, String... search) {
        return searchRepository.getBookWithSortMultiFieldAndSearch(page, size, sortBy, authorName, search);
    }

    @Override
    public PageResponse<List<BookDetailResponse>> getBookWithSortAndSearchByKeyword(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page-1,size);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);//khởi tạo để cho nó biết cần truy vấn ở đâu
        Root<Book> root = criteriaQuery.from(Book.class);

        ArrayList<Predicate> predicateList = new ArrayList<>();
        if(StringUtils.hasLength(keyword)){
            Predicate toTitle = criteriaBuilder.like(root.get("title"),"%"+keyword+"%");
            Predicate toPrice = criteriaBuilder.like(root.get("description"),"%"+keyword+"%");
            predicateList.add(toTitle);
            predicateList.add(toPrice);

            //kết nối với nhau thông qua biến author
            Join<Book,User> userJoin = root.join("author",JoinType.LEFT);//giữu lại tất cả các sách ngay cả khi cúng không có tác giả
            //ở đây dùng left vì nếu join bình thường(inner Join) thì nó sẽ tự động xóa những bản ghi không có author hợp lệ(null)
            Predicate toFullName = criteriaBuilder.like(userJoin.get("fullName"),"%"+keyword+"%");
            Predicate toEmail = criteriaBuilder.like(userJoin.get("email"),"%"+keyword+"%");
            predicateList.add(toFullName);
            predicateList.add(toEmail);
        }
        Predicate predicate = criteriaBuilder.conjunction();//khởi tạo predicate ban đầu mặc định là true

        if(!predicateList.isEmpty()){
            predicate =predicateList.get(0);//khởi tạo giá trị mặc định ban đầu là predicateList[0]
            //phải khởi tạo giá trị mặc định vì giá trị ban đầu được gán là criteriaBuilder.conjunction() tức là true
            //nếu để nguyên không gán get(0) vào sẽ là TRUE OR X =>dư thừa cần đảm bảo logic đúng ngay từ đầu
            for(int i=1;i<predicateList.size();i++){
                predicate = criteriaBuilder.or(predicate,predicateList.get(i));
            }
        }
        criteriaQuery.where(predicate);//gán điều kiện truy vấn

        List<Book> bookList = entityManager.createQuery(criteriaQuery)//truy vấn và phân trang
                .setFirstResult((int) pageable.getOffset())//vị trí của phần tử đầu tiên cần lấy
                .setMaxResults(size)//số lượng bản ghi cần lấy
                .getResultList();

        Long totalElements = getTotalElement(keyword);

        return PageResponse.<List<BookDetailResponse>>builder()
                .pageNo(pageable.getPageNumber()+1)//lấy ra trang tiếp theo
                .pageSize(pageable.getPageSize())//lấy ra số lượng bản ghi cần lấy
                .totalPages((int) Math.ceil((double) totalElements/size))
                .totalElements(totalElements)
                .items(bookList.stream().map(BookDetailResponse::convert).collect(Collectors.toList()))
                .build();
    }

    @Override
    public PageResponse<BookElasticSearch> searchElastic(int page, int size, String keyword) {
        return null;
    }

    private Long getTotalElement(String keyword){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Book> root = criteriaQuery.from(Book.class);

        ArrayList<Predicate> predicateList = new ArrayList<>();
        if(StringUtils.hasLength(keyword)){
            Predicate toTitle = criteriaBuilder.like(root.get("title"),"%"+keyword+"%");
            Predicate toPrice = criteriaBuilder.like(root.get("description"),"%"+keyword+"%");
            predicateList.add(toTitle);
            predicateList.add(toPrice);

            Join<Book,User> userJoin = root.join("author",JoinType.LEFT);//sử dụng left để kể cả khi không có author trùng thì ta vẫn lấy được các giá trị của book
            Predicate toFullName = criteriaBuilder.like(userJoin.get("fullName"),"%"+keyword+"%");
            Predicate toEmail = criteriaBuilder.like(userJoin.get("email"),"%"+keyword+"%");
            predicateList.add(toFullName);
            predicateList.add(toEmail);
        }
        Predicate predicate = criteriaBuilder.conjunction();//khởi tạo là true
        if(!predicateList.isEmpty()){
            predicate = predicateList.get(0);

            for(int i=1;i<predicateList.size();i++){
                predicate =criteriaBuilder.or(predicate,predicateList.get(i));
            }
        }
        criteriaQuery.select(criteriaBuilder.count(root)).where(predicate);
        return entityManager.createQuery(criteriaQuery)
                .getSingleResult();
    }
}
