package jpabook.jpashop.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.OrderItem;
import lombok.Data;

@Data
public class OrderItemQueryDto {
    //groupingBy를 위함
    @JsonIgnore
    private Long orderId;

    private String itemName;
    private int itemPrice;
    private int count;

    public OrderItemQueryDto(Long id, String itemName, int price, int count){
        this.orderId = id;
        this.itemName = itemName;
        this.itemPrice = price;
        this.count = count;
    }
}
