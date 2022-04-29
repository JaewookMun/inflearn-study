package com.jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;
    // column name을 "id"로 해도 되지만 dba 들이 테이블_id 방식을 선호

    @ManyToOne(fetch = FetchType.LAZY)
    // Lazy로 설정하면 proxy 객체인 ByteBuddyInterceptor로 초기화 한 상태에서
    // 필요시 db에서 조회해서 가져온다.
    @JoinColumn(name = "member_id") // 연관관계 주인으로 설정
    private Member member;
    // 객체의 변경포인트가 두개일 때
    // 양방향 참조가 일어난 상황, 데이터베이스에 있는 FK는 ORDERS 테이블에만 존재함.
    // 데이터를 변경할 때 JPA에서는 무엇을 기준으로 해야하는지 어려울 수 있음.
    // FK가 가까운 곳이 연관관계의 주인으로 매핑하면 된다.
    // 따라서 Order에 있는 멤버를 변경했을 때 변경될 수 있도록 처리한다.

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    // 원래 필드값을 먼저 persist해주고 나중에 객체도 persist해줘야하는데
    // cascade를 넣으면 객체만 persist해줘도 된다.

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
    // 1:1 관계일 경우 좀더 많이 조회하는 곳에 FK를 둔다.

    private LocalDateTime orderDate; // Date와 달리 java8에서 localDateTime 은 하이버네이트가 알아서 적용해준다.

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]

    // == 연관관계 메서드 ==
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    // 메서드의 위치는 핵심적으로 컨트롤하는 쪽에 두는 것이 좋다.
    // 양방향일 때 사용하면 좋다.

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //== 생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    /**
     * 주문 취소
     */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        // 강조할 때랑 이름이 같은 경우가 아니면 this를 잘 사용 안하심.
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
