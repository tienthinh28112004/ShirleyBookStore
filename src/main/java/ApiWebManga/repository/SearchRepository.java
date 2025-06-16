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
    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<List<BookDetailResponse>> getBookWithSortMultiFieldAndSearch(int page, int size, String sortBy, String authorName, String... search){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = criteriaQuery.from(Book.class);

        Predicate predicate = getPredicate(criteriaBuilder,root,search);

        if(StringUtils.hasLength(authorName)){
            log.info("Sort Book and Join User");
            Join<Book, User> userJoin = root.join("author");
            Predicate likeToFullName = criteriaBuilder.like(userJoin.get("fullName"),"%"+authorName+"%");
            Predicate likeToEmail = criteriaBuilder.like(userJoin.get("email"),"%"+authorName+"%");

            Predicate finalPredicate = criteriaBuilder.or(likeToEmail,likeToFullName);

            predicate= criteriaBuilder.and(predicate,finalPredicate);
        }
        criteriaQuery.where(predicate);
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
        List<Book> bookList = entityManager.createQuery(criteriaQuery)
                .setFirstResult((page-1)*size)
                .setMaxResults(size)
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Book> root = query.from(Book.class);

        Predicate predicate = getPredicate(criteriaBuilder,root,search);

        if(StringUtils.hasLength(userName)){
            log.info("join book and user");
            Join<Book,User> userJoin = root.join("author");
            Predicate likeToFullName = criteriaBuilder.like(userJoin.get("fullName"),"%"+userName+"%");
            Predicate likeToEmail = criteriaBuilder.like(userJoin.get("email"),"%"+userName+"%");
            Predicate finalPre = criteriaBuilder.or(likeToFullName,likeToEmail);

            predicate=criteriaBuilder.and(predicate,finalPre);

        }
        query.select(criteriaBuilder.count(root)).where(predicate);
        return entityManager.createQuery(query)
                .getSingleResult();
    }
    private Predicate getPredicate(CriteriaBuilder criteriaBuilder,Root root, String... search){
        Predicate predicate = criteriaBuilder.conjunction();

        Predicate isActivePredicate = criteriaBuilder.isTrue(root.get("isActive"));
        predicate = criteriaBuilder.and(predicate, isActivePredicate);

        List<SearchCriteria> criteriaList = new ArrayList<>();
        if(search != null){
            for(String s:search) {
                log.info(s);
                Pattern pattern = Pattern.compile("(\\w+?)([:<>!])(.*)");
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()) {

                    if (matcher.group(1).equalsIgnoreCase("category")) {

                        Join<Book, BookHasCategory> bookHasCategoryJoin = root.join("category");
                        Join<BookHasCategory, Category> categoryJoin = bookHasCategoryJoin.join("category");

                        Predicate categoryPredicate = criteriaBuilder.like(categoryJoin.get("name"), "%" + matcher.group(3) + "%");
                        predicate = criteriaBuilder.and(predicate, categoryPredicate);
                    } else {

                        criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));

                    }
                }
            }
        }

        SearchCriteriaQueryConsumer queryConsumer = new SearchCriteriaQueryConsumer(criteriaBuilder,root,predicate);

        if(!criteriaList.isEmpty()){
            criteriaList.forEach(queryConsumer);
            predicate =criteriaBuilder.and(predicate,queryConsumer.getPredicate());
        }
        return predicate;
    }
}
