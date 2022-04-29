package com.jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {
    // repository는 순수한 entity를 조회하기 위해 사용 - 재사용성.
    // 이 repository는 화면을 위해 별도로 생성
    // 화면에 의존적인 조회전용 메서드 - 별도로 분리 > 유지보수에도 효율적

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                        "select new com.jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
    // 성능상 v4이 좀 더 낫지만 화면에 의존성이 갖는 느낌.
    //v3와 v4의 성능차이가 크게 나지 않음.
}
