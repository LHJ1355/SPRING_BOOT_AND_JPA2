package jpabook.jpashop.repository.order.query;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class OrderQueryDto {
    private Long orderId;
    private String name;
    private List<OrderItemQueryDto> orderItems = new ArrayList<>();

    public OrderQueryDto(Long id, String name) {
        this.orderId = id;
        this.name = name;
    }

    public void addOrderItems(List<OrderItemQueryDto> orderItems) {
        this.orderItems.addAll(orderItems);
    }
}
