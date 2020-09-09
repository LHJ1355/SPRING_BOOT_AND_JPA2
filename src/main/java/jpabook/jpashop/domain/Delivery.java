package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    private DeliveryStatus status;

    public void setOrder(Order order){
        this.order = order;
    }

    public Delivery(){}

    public Delivery(Address address){
        this.address = address;
        this.status = DeliveryStatus.READY;
    }

}
