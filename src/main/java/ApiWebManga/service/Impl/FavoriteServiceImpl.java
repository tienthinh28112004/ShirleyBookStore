package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.Favorite;
import ApiWebManga.Entity.User;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Utils.SecurityUtils;
import ApiWebManga.dto.Response.BookDetailResponse;
import ApiWebManga.repository.BookRepository;
import ApiWebManga.repository.FavoriteRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final FavoriteRepository favoriteRepository;

    @Override//dùng để bật tắt yêu thích truyện của người dùng
    @Transactional//đảm bảo tính nhất quán khi xóa dữ liệu
    public boolean toggleFavorite(Long bookId) {
        Long userId=getUserIdCurrent();

        Book book=bookRepository.findById(bookId)
                .orElseThrow(()->new NotFoundException("Book not found"));

        if(favoriteRepository.existsByUserIdAndBookId(userId,book.getId())){
            //nếu yêu thích rồi thì sẽ xóa nó khỏi csdl,để đánh dấu là chưa yêu thích
            favoriteRepository.deleteByUserIdAndBookId(userId,book.getId());
            return false;
        }else{
            //nếu mà chưa yêu thích,sẽ lưu vào csdl để dánh dấu là yêu thích
            favoriteRepository.save(Favorite.builder()
                            .userId(userId)
                            .bookId(book.getId())
                    .build());
            return true;
        }
    }

    @Override//lấy ra id dánh sách truyện yêu thích của người dùng đã đăng nhập
    public List<BookDetailResponse> getFavoriteBook() {
        Long userId= getUserIdCurrent();
        List<Long> listBookId = favoriteRepository.findBookIdsByUserId(userId);
        List<Book> listBook=new ArrayList<>();
        for(int i=0;i<listBookId.size();i++){
            listBook.add(bookRepository.findById(listBookId.get(i)).
                    orElseThrow(()->new NotFoundException("User Not Founl")));
        }
        return listBook.stream().map(BookDetailResponse::convert).collect(Collectors.toList());
    }

    public Long getUserIdCurrent(){
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new NotFoundException("User chưa đăng nhập"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        return user.getId();
    }
}
