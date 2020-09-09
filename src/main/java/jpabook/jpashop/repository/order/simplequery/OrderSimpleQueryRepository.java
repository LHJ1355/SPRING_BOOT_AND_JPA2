package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

//repository는 보통 entity를 반환
//성능 초적화를 위해 query용 dto를 사용하는 repository, dto를 반환
//화면에 fit함
//패키지를 분리함으로써 기존 order repository는 순수하게 entity만 조회
@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    @PersistenceContext
    private EntityManager em;

    //재사용성 떨어짐, API 스펙에 맞춘 코드
    //API 스펙이 바뀌면 controller 계층이 아닌, repository를 수정해야 함
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.dateTime, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}
