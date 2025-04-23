package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.Order;
import ApiWebManga.Entity.OrderDetail;
import ApiWebManga.Entity.User;
import ApiWebManga.Enums.OrderStatus;
import ApiWebManga.Enums.PaymentExpression;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Utils.SecurityUtils;
import ApiWebManga.dto.Request.OrderDetailRequest;
import ApiWebManga.dto.Request.OrderRequest;
import ApiWebManga.dto.Response.OrderResponse;
import ApiWebManga.dto.Response.PageResponse;
import ApiWebManga.repository.BookRepository;
import ApiWebManga.repository.OrderRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.CartService;
import ApiWebManga.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final CartService cartService;
    //cái anyf sẽ chỉ được động bởi admin


    public OrderResponse createOrder(OrderRequest request){
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new NotFoundException("người dùng chưa đăng nhập"));

        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        Order order=Order.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .orderDate(LocalDateTime.now())
                .note(request.getNote())
                .user(user)
                .orderStatus(OrderStatus.PENDING)
                .paymentExpression(PaymentExpression.valueOf(String.valueOf(request.getPaymentExpression())))
                .build();
        List<OrderDetail> orderDetails=new ArrayList<>();
        long totalMoney=0;
        for(OrderDetailRequest x:request.getDetailRequests()){
            Book book=bookRepository.findById(x.getBookId())
                    .orElseThrow(()->new NotFoundException("Book Not found"));

            long totalMoneyBook = book.getPrice()*x.getQuantity();
            totalMoney+=totalMoneyBook;
            orderDetails.add(OrderDetail.builder()
                    .book(book)
                    .order(order)
                    .quantity(x.getQuantity())
                    .totalMoneyBook(totalMoneyBook)
                    .build());
        }
        //khi người dùng bấm thanh toán thì set tất cả giá trị quantity trong giỏ hàng =0 rồi gửi elen request là nó tự xóa
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

    public OrderResponse informationOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new NotFoundException("Order not found"));
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
    public PageResponse<List<OrderResponse>> orderRecent(int page, int size){
        Sort sort=Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable= PageRequest.of(page-1,size,sort);
        Page<Order> orderList=orderRepository.findAll(pageable);
        List<OrderResponse> responseList= orderList.stream().map(OrderResponse::convert).toList();
        return PageResponse.<List<OrderResponse>>builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(orderList.getTotalPages())
                .totalElements(orderList.getTotalElements())
                .items(responseList)
                .build();
    }
}
