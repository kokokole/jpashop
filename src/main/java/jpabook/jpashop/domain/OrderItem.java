package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {


    @Id
    @GeneratedValue
    @Column(name = "oder_item_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문가격
    private int count; // 주문수량
//@NoArgsConstructor(access = AccessLevel.PROTECTED)써도됨
//    protected OrderItem(){
//        //쓰지마셈
//        //생성자 호출 제약, createOrderItem으로만 객체가 생성되도록 유도
//    }

    //생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); //재고수량 감소

        return orderItem;
    }

    //비즈니스 로직

    /**
     * 주문취소 되어 재고수량 원복
     */
    public void cancel() {
        getItem().addStock(count);
    }

    //조회 로직

    /**
     * 주문상품 전체 가격 조회
     * @return
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
