package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderStatus;
import com.jpabook.jpashop.domain.QMember;
import com.jpabook.jpashop.domain.QOrder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    // 첫번째 동적 쿼리 생성방식
    // String 방식으로 jpql을 작성하면 컴파일단계에서 실수로 인한 에러를 발견하기 힘들다.
    public List<Order> findAllByString(OrderSearch orderSearch) {
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        // 이런 식으로 문자열(String)을 생성하는 것은 굉장히 번거롭고 실수로 인한 버그가 생길 가능성이 있다.

        //주문 상태 검색
        if(orderSearch.getOrderStatus() != null) {
          if(isFirstCondition) {
              jpql += " where";
              isFirstCondition = false;
          } else {
              jpql += " and";
          }
          jpql += " o.status = :status";
        }

        //주문 상태 검색
        if(StringUtils.hasText(orderSearch.getMemberName())) {
          if(isFirstCondition) {
              jpql += " where";
              isFirstCondition = false;
          } else {
              jpql += " and";
          }
          jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if(orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }


        return query.getResultList();


//        // 테이블로 표현해야하는 것을 객체로 표현
//        em.createQuery("select o from Order o join o.member m" +
//                " where o.status = :status" +
//                        "and m.name like :name", Order.class)
//                .setParameter("status", orderSearch.getOrderStatus())
//                .setParameter("name", orderSearch.getMemberName())
//                .setMaxResults(1000) // 최대 1000건
//                .getResultList();
    }
    
    // JPA가 제공하는 표준으로 제공하는 방식 - for Dynamic query SPEC
    // -> 실무에서 사용되지 않음
    // 치명적인 단점 - 유지보수성이 굉장히 안좋음. 돌리면 jpql이 생성되지만 실제로 떠올리기 힘듬.
    // 컴파일 단계에서 해결하기위한 노력
    // 해결 - Query DSL (자바 코드로 작성되기 때문에 컴파일단계에서 문제를 찾기 편하다.)
    // Springboot, JPA, QueryDSL
    /**
     * JPA Criteria
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();;
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        /// 주문 상태 검색
        if(orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        
        //회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.equal(m.get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    // 오타를 내면 컴파일단계에서 확인가능하며 코드 어시스턴스까지 지원됨.
    // generated된 파일들은 Git에 업로드되면 안됨. - ignore처리.
    public List<Order> findAll(OrderSearch orderSearch) {
        JPAQueryFactory query = new JPAQueryFactory(em);

        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return query
                .select(order)
                .from(order)
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
                .join(order.member, member)
                .limit(1000)
                .fetch();
    }

    private BooleanExpression nameLike(String memberName) {
        if(!StringUtils.hasText(memberName)) {
            return null;
        }

        return QMember.member.name.like(memberName);
    }

    //동적쿼리를 사용하기 위해 별도의 메서드로 처리
    private BooleanExpression statusEq(OrderStatus statusCond) {
        if(statusCond == null) {
            return null;
        }

        return QOrder.order.status.eq(statusCond);
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).getResultList();

        //fetch join - jpql에서만 존재함
        //join fetch를 사용하면 lazy를 무시하고 만들 때 join 시켜서 사용
        //실무에서 JPA를 사용하기 위해서는 fetch join을 무조건 100% 이해해야한다.
        //all 연관관계 - lazy로 설정하고 필요시 fetch join 사용
    }

    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch  oi.item i", Order.class)
                .getResultList();
        // order가 2개 orderItems 가 4개라면 결과적으로 join 때문에 order가 4개가 되어 출력된다.
        // fetch join 역시 db 입장에서는 join을 사용하는 것임.
        // 데이터의 중복 발생

        // db에서 distinct를 사용할 때 중복을 제거하는 것은 줄의 값이 같아야함.

        // distinct 키워드 역할
        //1. db에 distinct 키워드 날림
        //2. root엔티티가 중복될 경우 중복을 제거한다.
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }


    // 의존관계는 한방향으로 흘러야한다.
}
