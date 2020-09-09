package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int orderCount;


    public void setOrder(Order order){
        this.order = order;
    }

    protected  OrderItem(){}

    public OrderItem(Item item, int orderPrice, int orderCount) {
        this.item = item;
        this.orderPrice = orderPrice;
        this.orderCount = orderCount;
    }

    //==생성 메소드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int orderCount){
       OrderItem orderItem = new OrderItem(item, orderPrice, orderCount);
       item.removeStock(orderCount);
       return orderItem;
    }
    //==비지니스 로직==//
    public void cancel(){
        item.addStock(orderCount);
    }


    public int getTotalPrice() {
        return orderPrice * orderCount;
    }
}
