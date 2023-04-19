package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "member_id") //외래키 설정
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    /*
    persist(orderItemA)
    persist(orderItemB)
    persist(orderItemC)
    orderItemA, orderItemB, orderItemC를 Order에 넣기
    persist(order)

    // cascade 설정을 하면 아래와 같은 작업으로 orderItemA, orderItemB, orderItemC를 넣은 상태로 Order를 저장할 수 있음
    orderItemA, orderItemB, orderItemC를 Order에 넣기
    persist(order)

    */

    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    // 연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }


    //(주문) 생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) { //파이썬의 가변 함수처럼 OrderItem 파라미터가 0개일 수도 있고 그 이상일 수도 있다.
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    //비즈니스 로직
    /**
     * 주문취소
     */
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL); // JPA Dirty Checking Update에 의해서 Update쿼리를 사용하지 않아도 Order의 상태변화를 확인하여 자동으로 Update한다.
        for (OrderItem orderItem : orderItems) { //상품재고수량 원복
            orderItem.cancel();
        }
    }

    //조회로직
    /**
     * 전체 주문 가격 조회(계산)
     */
    public int getTotalPrice(){
        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        /*
         for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
         */
        return totalPrice;
    }





}
