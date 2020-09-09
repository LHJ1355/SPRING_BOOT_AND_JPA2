package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
//Setter의 경우, 어디서 변경됬나 추적하기가 어려우므로
//되도록 사용하지 않도록 해야한다.
//변경이 필요한경우 엔티티 내부에 메소드를 만들어서 변경하면
//해당 메소드만 추적하면 되므로 유지보수가 쉬워진다.
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    public Member(){

    }

    public Member(String name, Address address){
        this.name = name;
        this.address = address;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
