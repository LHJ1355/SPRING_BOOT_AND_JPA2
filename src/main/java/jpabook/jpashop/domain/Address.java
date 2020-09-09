package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    //값 타입은 변경 불가능하게 설계
    //생성자에서 값을 모두 초기화해서 변경 불가능한 클래스로 만듦
    private String city;
    private String street;
    private String zipcode;


    //JPA 스펙상 엔티티나 임베디드 타입은 기본 생성자를 public나 protect로 설정해야 한다.

    //JPA가 이런 제약조건을 두는 이유는 JPA 구현 라이브러리가 객체를 생성할 때,
    //리플렉션같은 기술을 사용할 수 있도록 지원해야 하기 때문이다.
    protected Address(){

    }

    public Address(String city, String street, String zipcode){
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
