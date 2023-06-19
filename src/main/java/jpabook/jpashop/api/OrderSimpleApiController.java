package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * N To One(ManyToOne, OneToOne)에서 성능 최적화를 어떻게 할 것인가???
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }
        // -> 좋지못한 방법
        return orders;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){ //DTO로 바꿔서 보내자
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream().map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    // v1, v2 API에는 둘다 데이터베이스 쿼리를 너무 많이 호출하는 문제가 있다.
    // 실제로 api를 실행하면 5번의 쿼리가 실행됨
    // ORDER 조회 시 SQL 1번 실행되고 결과 ORDER가 2개가 나왔다.
    // stream().map()에 의해서 루프가 시행될 때 SimpleOrderDto객체를 만들고 member와 delivery 테이블을 조회함
    // 그다음 루프에서도 SimpleOrderDto객체를 만들고 member와 delivery 테이블을 조회함
    // N + 1 문제 -> Order를 조회하기 위해서 Order 결과개수(N)만큼 쿼리가 더 실행된다.
    // LAZY 로딩이 아닌 EAGER로 바꾸면 훨씬 더 많은 쿼리가 실행되기 때문에 EAGER로 바꾸는 건 좋은 해결책이 아니다.
    // LAZY 로딩을 해야 영속성 컨텍스트에서 데이터를 가져올 수 있기 때문에 실행되는 쿼리수를 줄일 수 있다.
    // N + 1 문제는 fetch join을 통해서 해결할 수 있다.
    /*

     */


    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders = orderRepository.findWithMemberDelivery(); //메소드에서 fetch join
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }


    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY 초기화 -> 객체가 생성될 때 영속성 컨텍스트에서 데이터가 없으면 쿼리가 실행됨
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화 -> 객체가 생성될 때 영속성 컨텍스트에서 데이터가 없으면 쿼리가 실행됨


        }
    }
}
