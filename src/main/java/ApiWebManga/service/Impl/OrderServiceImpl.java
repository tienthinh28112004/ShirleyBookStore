package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.Order;
import ApiWebManga.Entity.OrderDetail;
import ApiWebManga.Entity.User;
import ApiWebManga.Enums.OrderStatus;
import ApiWebManga.Enums.PaymentExpression;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Utils.SecurityUtils;
import ApiWebManga.dto.Request.OrderRequest;
import ApiWebManga.dto.Response.OrderResponse;
import ApiWebManga.repository.BookRepository;
import ApiWebManga.repository.OrderRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    //tạo 1 cái order đầy đủ thông tin sản phẩm rồi chuyển sang thanh toán(chú ý là khi mà thanh toán thì phần cart phải xóa hết thông tin của cái đặt hàng đi)
    //1 cái chuyển trạng thái của order(chủ user có quyền)
    //1 cái hiển thị đầy đủ thông tin của order
    //=>chú ý lưu thông tin đơn hàng avof csdl để sau này có thể truy suất khi người dùng yêu cầu xem lại các đơn hàng ủa mình đã đặt(sử dụng orderId để lấy)
    //1 cái xem tất c đơn hàng hiện có trong csdl(dành riêng cho admin)
    //????liệu 1 user có thể có nhiều order???
    //(đã có order,orderDetail,:))))cố làm gọn nhẹ nhất có thể thôi:)))
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    //cái anyf sẽ chỉ được động bởi admin


    public OrderResponse createOrder(OrderRequest orderRequest){
        String email= SecurityUtils.getCurrentLogin().orElseThrow(()->new NotFoundException("người dùng chưa đăng nhập"));
        User user=userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("User not found"));
        Order order=Order.builder()
                .fullName(orderRequest.getFullName())
                .phoneNumber(orderRequest.getPhoneNumber())
                .address(orderRequest.getPhoneNumber())
                .orderDate(LocalDateTime.now())
                .note(orderRequest.getNote())
                .user(user)
                .orderStatus(OrderStatus.PENDING)
                .paymentExpression(PaymentExpression.DIRECTPAYMENT)
                .build();
        List<OrderDetail> orderDetails=new ArrayList<>();
        long totalMoney=0;
        for(int i=0;i<orderRequest.getDetailRequests().size();i++){
            Book book=bookRepository.findById(orderRequest.getDetailRequests().get(i).getBookId()).orElseThrow(()->new NotFoundException("Book Not found"));
            long totalMoneyBook = book.getPrice()*orderRequest.getDetailRequests().get(i).getQuantity();
            totalMoney+=totalMoneyBook;
            orderDetails.add(OrderDetail.builder()
                    .book(book)
                    .price(book.getPrice())
                    .order(order)
                    .quantity(orderRequest.getDetailRequests().get(i).getQuantity())
                    .totalMoneyBook(totalMoneyBook)
                    .build());
        }
        order.setOrderDetails(orderDetails);
        order.setTotalMoney(totalMoney);
        orderRepository.save(order);
        return OrderResponse.convert(order);
    }
    //lấy ra danh sách lịch sử ngươời dùng đã order
    public List<OrderResponse> findOrderByUser(){
        String email= SecurityUtils.getCurrentLogin().orElseThrow(()->new NotFoundException("người dùng chưa đăng nhập"));
        User user=userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("User not found"));
        List<Order> orderList=orderRepository.findOrderByUserId(user.getId());
        return orderList.stream().map(OrderResponse::convert).collect(Collectors.toList());
    }

    public OrderResponse informationCart(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(()->new NotFoundException("Order not found"));
        return OrderResponse.convert(order);
    }
    public void updateOrderStatus(Long orderId,OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setOrderStatus(newStatus);
        orderRepository.save(order);
    }

    public void updatePaymentExpression(Long orderId, PaymentExpression newPayment) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setPaymentExpression(newPayment);
        orderRepository.save(order);
    }
    //lấy ra các thông tin đơn hàng chuẩn nhất
    public List<OrderResponse> orderRecent(int page,int size){
        Pageable pageable= PageRequest.of(page-1,size);
        Page<Order> orderList=orderRepository.findAll(pageable);
        return orderList.stream().map(OrderResponse::convert).collect(Collectors.toList());
    }
}
