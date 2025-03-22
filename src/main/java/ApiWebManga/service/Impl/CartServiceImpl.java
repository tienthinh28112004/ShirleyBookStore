package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.Cart;
import ApiWebManga.Entity.CartDetail;
import ApiWebManga.Entity.User;
import ApiWebManga.Exception.BadCredentialException;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Utils.SecurityUtils;
import ApiWebManga.dto.Request.CartAddItemRequest;
import ApiWebManga.dto.Response.CartInformationResponse;
import ApiWebManga.dto.Response.CartItemResponse;
import ApiWebManga.repository.BookRepository;
import ApiWebManga.repository.CartRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartRepository cartRepository;
//    @Override//mặc định ban đầu vào đã được tạo 1 giỏ hàng rỗng để shopping rồi
//    public CartInformationResponse createDefaultCart() {
//        String email = SecurityUtils.getCurrentLogin()
//                .orElseThrow(()->new BadCredentialException("unauthorized"));
//
//        User user=userRepository.findByEmail(email)
//                .orElseThrow(()->new NotFoundException("User not found"));
//        List<CartDetail> cartDetailList=new ArrayList<>();
//        Cart cart = Cart.builder()
//                .user(user)
//                .totalMoney(0L)
//                .cartDetails(cartDetailList)//ban đầu khơởi tạo giỏ hàng là rỗng
//                .build();
//
//        cartRepository.save(cart);
//        return informationCart();
////        log.info("lấy được user");
////        Book book = bookRepository.findById(request.getBookId())
////                .orElseThrow(()->new NotFoundException("Book not found"));
////        log.info("{}",book.getId());
////        if(book.getPrice()==null) book.setPrice(0L);
////        Long totalMoneys=book.getPrice()*request.getQuantity();
////        List<CartDetail> cartDetailList = new ArrayList<>();
////        Cart cart = Cart.builder()
////                .user(user)
////                .totalMoney(0)
////                .cartDetails(cartDetailList)
////                .build();
////        cartRepository.save(cart);
////        log.info("lưu cart thành công");
////        var carts =cartRepository.findAllByUserId(user.getId());
////        long totalMoney=0;
////        for(int i=0;i<carts.size();i++){
////            totalMoney += carts.get(i).getTotalPrice();
////        }
////        Long totalElements = cartRepository.countByUserId(user.getId());
////        return CartCreationResponse.builder()
////                .cartId(cart.getId())
////                .userId(user.getId())
////                .totalElements(totalElements)
////                .totalMoney(totalMoney)
////                .items(carts)
////                .build();
//    }


    @Override
    public CartInformationResponse addItemCart(CartAddItemRequest request){
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialException("unauthorized"));

        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(()->new NotFoundException("Book not found"));

        if(book.getPrice()==null) book.setPrice(0L);
        Long totalMoneys=book.getPrice()*request.getQuantity();
        CartDetail cartDetail = CartDetail.builder()
                .cart(user.getCart())
                .book(book)
                .price(book.getPrice())
                .quantity(request.getQuantity())
                .totalMoney(totalMoneys)
                .build();
        long totalMoney = cartDetail.getTotalMoney() + user.getCart().getTotalMoney();
        List<CartDetail> cartDetails=user.getCart().getCartDetails();
        cartDetails.add(cartDetail);
        Cart cart=Cart.builder()
                .user(user)
                .cartDetails(cartDetails)
                .totalMoney(totalMoney)
                .build();
        user.setCart(cart);
        userRepository.save(user);
        return informationCart();
    }

    @Override
    public Void deleteItemCart(Long bookId) {
        //kể cả bookId không trùng trong này thì code vẫn chạy được
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialException("unauthorized"));

        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        long totalMoney = 0;
        List<CartDetail> cartDetailList = new ArrayList<>();
        List<CartDetail> carts=user.getCart().getCartDetails();
        for(int i=0;i<carts.size();i++){
            if(carts.get(i).getBook().getId()!=bookId){
                cartDetailList.add(carts.get(i));
                totalMoney+=carts.get(i).getTotalMoney();
            }
        }
        Cart cart=Cart.builder()
                .user(user)
                .cartDetails(carts)
                .totalMoney(totalMoney)
                .build();
        user.setCart(cart);
        userRepository.save(user);
        //return informationCart();

//        Cart cart = cartRepository.findById(cartItem)
//                .orElseThrow(()->new NotFoundException("Cart not found"));
//
//        if(!Objects.equals(user,cart.getUser())){
//            throw new RuntimeException("Access Dined");
//        }
//        cartRepository.delete(cart);
        return null;
    }
    @Override
    public CartInformationResponse informationCart(){
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialException("unauthorized"));

        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        Long totalMoney = user.getCart().getTotalMoney();
        long totalElements = user.getCart().getCartDetails().size();
        List<CartItemResponse> carts= user.getCart().getCartDetails().stream().map(
                cartDetail -> CartItemResponse.builder()
                        .bookId(cartDetail.getBook().getId())
                        .title(cartDetail.getBook().getTitle())
                        .thumbnail(cartDetail.getBook().getThumbnail())
                        .priceBook(cartDetail.getPrice())
                        .totalPrice(cartDetail.getPrice()*cartDetail.getQuantity())
                        .build()
        ).toList();
        return CartInformationResponse.builder()
                //.cartId(cart.getId())
                .userId(user.getId())
                .totalElements(totalElements)
                .totalMoney(totalMoney)
                .items(carts)
                .build();
    }

}
