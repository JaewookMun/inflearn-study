package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderStatus;
import com.jpabook.jpashop.repository.OrderRepository;
import com.jpabook.jpashop.repository.OrderSearch;
import com.jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import com.jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
            // getMember, getDelivery 까지는 proxy라서 null이지만
            // getName을 하면 쿼리를 실행하여 값을 가져옴.
        }


        return all;
        // 양방향 연관관게 문제가 생김
        // -> 둘 중 하나를 json ignore 해주어야함.
        // 일대다 는 기본이 지연로딩이다. fetch.Lazy
        // 지연로딩 - 객체가 생성될 때 바로 초기화 하는 것이 아니라 이후에 초기화.

        // api에는 필요한 데이터만 노출하는 것이 필요
    }

    // v2는 api SPEC에 딱 맞추어 작성
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        //ORDER 2개 (Query가 5개 실행됨)
        //N + 1 -> 1 + 회원 N + 배송 N
            // 주문의 수가 증가할 수록 실행하는 쿼리가 더 많아짐.
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }
    // v2: 쿼리가 많이 나가는 문제 - 성능문제 (v1과 실행 쿼리의 갯수가 동일)

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    // fetch 키워드를 통해 하나의 sql로 원하는 결과를 가져올 수 있다.
    // 실무에서 많이 사용되는 기법 - *fetch join* @@ 굉장히 중요!
    // v3 단점 : 엔티티를 다 가져옴

    // JPA에서 DTO를 한번에 가져오는 방법
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }
    // v4는 fetch join에 비해 select 해서 가져오는 데이터가 적다.
    // sql 짜듯 jpql을 사용해서 가져온다.
    // 서로 장단점 존재. v3의 결과는 다양한 곳에서 활용 가능하지만
    // v4는 다른 곳에서 재사용할 수 없다는 점이 단점이다.
    // 해당 기능에 딱 맞게 최적화

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 초기화
        }
    }
}
