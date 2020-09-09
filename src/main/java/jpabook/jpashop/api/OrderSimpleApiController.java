package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * XToOne 관계, NOT COLLECTION
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    //엔티티 직접 노출
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getStatus();
            order.getOrderItemList().stream().forEach(m -> m.getItem().getId());
        }
        return all;
    }


    //엔티티를 DTO로 변환
    @GetMapping("/api/v2/simple-orders")
    public Result ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderSimpleDto> collect = orders.stream()
                .map(o -> new OrderSimpleDto(o))
                .collect(Collectors.toList());

        //return collect;
        return new Result(collect.size(), collect);
    }

    //페치 조인 최적화
    //fetch join 을 사용해 lazy loading에 의해 유발되는 n+1쿼리 문제 제거
    @GetMapping("/api/v3/simple-orders")
    public Result ordersV3(){
        List<Order> orders = orderRepository.findAllWithFetchJoin();
        List<OrderSimpleDto> collect = orders.stream().map(o -> new OrderSimpleDto(o))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    //JPA에서 DTO로 바로 조회
    @GetMapping("api/v4/simple-orders")
    public Result ordersV4(){
        List<OrderSimpleQueryDto> orderDtos = orderSimpleQueryRepository.findOrderDtos();

        return new Result(orderDtos.size(), orderDtos);
    }

    @Data
    static class OrderSimpleDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public OrderSimpleDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getDateTime();
            orderStatus = order.getStatus();
            address= order.getDelivery().getAddress();
        }
    }

    @Data
    static class Result<T>{
        private int count;
        private T data;

        public Result(int count, T data) {
            this.count = count;
            this.data = data;
        }
    }

}
