package ApiWebManga.service;

import ApiWebManga.Entity.BookElasticSearch;
import ApiWebManga.dto.Request.AdminUploadBookRequest;
import ApiWebManga.dto.Request.BookCreationRequest;
import ApiWebManga.dto.Response.BookDetailResponse;
import ApiWebManga.dto.Response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    //upload sách
    BookDetailResponse uploadBook(BookCreationRequest request, MultipartFile thumbnail,MultipartFile book);

    BookDetailResponse adminUploadBook(AdminUploadBookRequest request, MultipartFile thumbnail);

    //lấy ra thông tin sách theo id
    BookDetailResponse getBookId(Long id);
    BookDetailResponse toggleBookStatus(Long id);
    //lấy ra các sách dựa vào page,size
    PageResponse<List<BookDetailResponse>> getAllBook(int page, int size,String keyword,String sorts);

    PageResponse<List<BookDetailResponse>> getBookByAuthor(int page, int size,String keyword,String sorts);
    //tìm kiếm và săắp xếp sách theo các yêu cầu khác nhau (có join bảng user)
    PageResponse<List<BookDetailResponse>> getBoolWithSortAndMultiFieldAndSearch(int page, int size,String sortBy, String authorName, String...search);
    //lấy ra danh sách book bằng cách tìm kiếm theo các tiêu chí(tìm kiếm theo 1 tiêu chí hay sao ấy (\-.-/)
    PageResponse<List<BookDetailResponse>> getBookWithSortAndSearchByKeyword(int page, int size, String keyword);
    PageResponse<BookElasticSearch> searchElastic(int page,int size,String keyword);//dùng cái này để truy vấn,sẽ có thể sắp xếp theo tiêu chí

}
