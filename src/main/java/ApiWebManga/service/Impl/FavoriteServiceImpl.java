package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.Favorite;
import ApiWebManga.Entity.User;
import ApiWebManga.Exception.BadCredentialException;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Utils.SecurityUtils;
import ApiWebManga.dto.Request.FavoriteRequest;
import ApiWebManga.dto.Response.FavoriteResponse;
import ApiWebManga.dto.Response.PageResponse;
import ApiWebManga.repository.BookRepository;
import ApiWebManga.repository.FavoriteRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final FavoriteRepository favoriteRepository;

    public void createFavorite(FavoriteRequest request){
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialException("Email không hợp lệ"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(()->new NotFoundException("Book not found"));

        boolean isAlreadyFavorite = favoriteRepository.existsByUserAndBook(user,book);
        if(isAlreadyFavorite){
            //nếu như đã tồn tại yêu thích rồi thì đưa ra lỗi
            throw new BadCredentialException("Already in favorites");
        }

        Favorite favorite=Favorite.builder()
                .user(user)
                .book(book)
                .build();
        favoriteRepository.save(favorite);
    }

    public FavoriteResponse findFavoriteById(Long id){
        Favorite favorite= favoriteRepository.findById(id)
                    .orElseThrow(()->new BadCredentialException("Favorite not existed"));
        return FavoriteResponse.convert(favorite);
    }

    public PageResponse<List<FavoriteResponse>> findAllByUserCurrent(int page,int size){
        //lấy ra danh sách yêu thích của người dùng hiện tại
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialException("Email không hợp lệ"));

        log.info("vào ddeens đây");
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page-1,size);
        Page<Favorite> favorites = favoriteRepository.findByUser(user,pageable);//lấy ra danh sách yêu thích của nguwoif dùng hiện tại
        log.info("số lương {}",favorites.getTotalElements());
        List<FavoriteResponse> favoriteResponses= favorites.stream().map(FavoriteResponse::convert).toList();
        log.info("số lương {}",favoriteResponses.size());
        //chuyển về danh sách yêu thích để trả về
        return PageResponse.<List<FavoriteResponse>>builder()
                .pageNo(page)//trang hiện tại
                .pageSize(favorites.getSize())
                .totalPages(favorites.getTotalPages())
                .totalElements(favorites.getTotalElements())
                .items(favoriteResponses)
                .build();
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")//chỉ xóa được nhưng yêu thích của mình thôi
    public void deleteFavorite(Long favoriteId){
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialException("Email không hợp lệ"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        List<Favorite> favorites = favoriteRepository.findByUser(user);

        Favorite favoriteToDelete = favorites.stream()
                .filter(f -> Objects.equals(f.getId(),favoriteId))
                .findFirst()//tìm bản ghi ầu tiên có id yêu thích trùng với id id đưa vào
                .orElseThrow(()->new NotFoundException("Favorite not found"));
        favoriteRepository.delete(favoriteToDelete);
    }
}
