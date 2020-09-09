package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    @PersistenceContext
    private EntityManager em;

    public List<OrderQueryDto> findOrderQueryDto() {
        List<OrderQueryDto> results = findOrders();

        results.stream().forEach(r -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(r.getOrderId());
            r.setOrderItems(orderItems);
        });

        return results;
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> results = findOrders();

        List<Long> orderIds = results.stream().map(r -> r.getOrderId()).collect(Collectors.toList());

        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, i.price, oi.orderCount) from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in :order_ids", OrderItemQueryDto.class)
                .setParameter("order_ids", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getOrderId()));

        results.stream().forEach(r -> {
            r.addOrderItems(orderItemMap.get(r.getOrderId()));
            //r.setOrderItems(orderItemMap.get(r.getOrderId()));
        });

        return results;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, i.price, oi.orderCount) from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id = :order_id", OrderItemQueryDto.class)
                .setParameter("order_id", orderId)
                .getResultList();
        return orderItems;
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery("select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name) from Order o" +
                " join o.member m" +
                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }


}
