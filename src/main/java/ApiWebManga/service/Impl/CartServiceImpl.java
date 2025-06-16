package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.CartDetail;
import ApiWebManga.Entity.User;
import ApiWebManga.Exception.BadCredentialException;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Utils.SecurityUtils;
import ApiWebManga.dto.Request.CartAddItemRequest;
import ApiWebManga.dto.Response.CartTotalResponse;
import ApiWebManga.dto.Response.CartItemResponse;
import ApiWebManga.repository.BookRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public CartTotalResponse addOrUpdateItemCart(CartAddItemRequest request){
        if(request.getQuantity() <=0){//cái này xử lý trức tiếp fontend được nhưng cứ cho vào đây phòng hờ
            deleteItemCart(request.getBookId());
            return informationCart();
        }

        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialException("unauthorized"));
        log.info("email {}",email);
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(()->new NotFoundException("Book not found"));

        log.info("quantity {}",request.getQuantity());
        boolean bookExists = false;

        //kiểm tra xem sản phẩm có trong giỏ hàng chưa
        for(CartDetail detail : user.getCart().getCartDetails()){
            if(detail.getBook().getId().equals(request.getBookId())){
                //Cập nhật lại giá
                user.getCart().setTotalMoney(user.getCart().getTotalMoney() - detail.getTotalMoney() + book.getPrice()* request.getQuantity());

                //cập nhật số lượng
                detail.setQuantity(request.getQuantity());
                detail.setTotalMoney(book.getPrice() * request.getQuantity());
                log.info("tổng tiền {}",book.getPrice() * request.getQuantity());
                bookExists=true;
                break;
            }
        }

        //nếu sản phẩm chưa tồn tại thì thêm mới
        if(!bookExists){
            long totalMoney = book.getPrice()* request.getQuantity();
            CartDetail cartDetail = CartDetail.builder()
                    .cart(user.getCart())
                    .book(book)
                    .quantity(request.getQuantity())
                    .totalMoney(totalMoney)
                    .build();
            log.info(String.valueOf(totalMoney));
            user.getCart().setTotalMoney(user.getCart().getTotalMoney() + totalMoney);
            user.getCart().getCartDetails().add(cartDetail);
        }

        userRepository.save(user);
        return informationCart();
    }

    @Override
    public void deleteItemCart(Long bookId) {
        //kể cả bookId không trùng trong này thì code vẫn chạy được
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialException("unauthorized"));

        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        List<CartDetail> cartDetailList = user.getCart().getCartDetails();
        CartDetail cartDetail=null;

        //Tìm sản phẩm cần xóa
        for(CartDetail detail : cartDetailList){
            if(detail.getBook().getId().equals(bookId)){
                cartDetail = detail;
                user.getCart().setTotalMoney(user.getCart().getTotalMoney() - detail.getTotalMoney());
                break;
            }
        }
        //xóa sản phẩm nếu tìm thấy(chỉ lưu nếu tìm thấy)
        if(cartDetail != null){
            cartDetailList.remove(cartDetail);
            userRepository.save(user);
        }

    }

    @Override
    public void clearAllItemCart() {
        String email=SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialException("unauthorized"));

        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        user.getCart().getCartDetails().clear();//xóa hết sản phẩm trong giỏ
        user.getCart().setTotalMoney(0L);//reset giỏ hàng về 0

        userRepository.save(user);
    }

    public CartTotalResponse informationCart(){
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialException("unauthorized"));

        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        Long totalMoney = user.getCart().getTotalMoney();
        long totalElements = user.getCart().getCartDetails().size();
        List<CartItemResponse> carts= user.getCart().getCartDetails().stream()
                .map(CartItemResponse::convert).toList();
        return CartTotalResponse.builder()
                //.cartId(user.getCart().getId())
                .userId(user.getId())
                .totalElements(totalElements)
                .totalMoney(totalMoney)
                .items(carts)
                .build();
    }

}
