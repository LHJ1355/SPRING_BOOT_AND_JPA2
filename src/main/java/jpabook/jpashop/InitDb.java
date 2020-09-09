package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }



    @Component
    @Transactional
    static class InitService {

        @PersistenceContext
        private EntityManager em;

        public void dbInit1(){
            Member member = createMember("userA", new Address("서울", "1", "1111"));

            Book book1 = createItem("JPA1 BOOK", 10000, 100);

            Book book2 = createItem("JPA2 BOOK", 20000, 200);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 2);

            Delivery delivery = new Delivery(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2(){
            Member member = createMember("userB", new Address("전주", "2", "2222"));

            Book book1 = createItem("SPRING1 BOOK", 30000, 300);

            Book book2 = createItem("SPRING2 BOOK", 40000, 400);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 2);

            Delivery delivery = new Delivery(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }


        private Member createMember(String name, Address address) {
            Member member = new Member(name, address);
            em.persist(member);
            return member;
        }

        private Book createItem(String name, int price, int stockQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            em.persist(book);
            return book;
        }

    }
}
