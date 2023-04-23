package jpabook.jpashop.service;


import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;

    private final MemberRepository memberRepository;

    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order); //order를 persist해서 저장하면 Order와 연관관계 설정된 OrderList도 Cascade 옵션에 의해서 자동으로 같이 persist된다.
        // Delivery도 Order에서 연관관계 설정이 되어 있고 Cascade옵션이 설정되어 있어 같이 persist가 자동으로 된다.
        // 삭제시에도 동일하게 Cascade됨

        /*
        Delivery와 OrderItem는 Order에서만 사용하기 때문에 Order내에서만 관리되므로 Cascade를 설정해서 관리하면 편하다(Persist 및 Delete하는 라이프사이클이 동일하다.)
        만약 Delivery와 OrderItem를 Order 외 다른 곳에서도 처리한다면 Cascade를 사용하면 안된다.
         */
        return order.getId();
    }

    /**
     * 취소
     * @param orderId
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }


    //검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);

    }

}
