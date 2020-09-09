package jpabook.jpashop.api;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;


    //엔티티를 DTO로 변환
    //n+1문제 발생함
    @GetMapping("api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    //fetch join 최적화
    //페이징 불가 = distinct를 쓰더라도 일단 메모리에 올라간 후에 중복제거가 일어나기에 메모리초과가 일어날 수 있음
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit){
        List<Order> orders = orderRepository.findAllWithFetchJoin(offset, limit);
        List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    //n+1문제 발생
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDto();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }
    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private List<OrderItemDto> orderItems;
        private int totalPrice;

        public OrderDto(Order o){
            this.orderId = o.getId();
            this.name = o.getMember().getName();
            List<OrderItemDto> collect = o.getOrderItemList().stream().map(oi -> {
                this.totalPrice += oi.getTotalPrice();
                return new OrderItemDto(oi);
            })
                    .collect(Collectors.toList());
            this.orderItems = collect;
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName;
        private int itemPrice;
        private int count;

        public OrderItemDto(OrderItem oi){
            this.itemName = oi.getItem().getName();
            this.itemPrice = oi.getOrderPrice();
            this.count = oi.getOrderCount();
        }
    }
}
