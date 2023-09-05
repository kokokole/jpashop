package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

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
    public List<OrderSimpleQueryDto> ordersV2(){ //DTO로 바꿔서 보내자
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        List<OrderSimpleQueryDto> result = orders.stream().map(o -> new OrderSimpleQueryDto(o))
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

    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleQueryDto> ordersV3(){
        List<Order> orders = orderRepository.findWithMemberDelivery(); //메소드에서 fetch join
        List<OrderSimpleQueryDto> result = orders.stream()
                .map(o -> new OrderSimpleQueryDto(o))
                .collect(Collectors.toList());
        return result;
    }

    //위에 v3방법은 엔티티를 DTO로 변환하는 작업을 진행했는데 v4에서는 JPA에서 바로 DTO로 전환하는 방식을 사용
    //쿼리는 v3 fetch join을 했을떄에 비해서 select 시 필요한 데이터만 select해온다
    //그러나 v4방식은 재사용성이 떨어진다. -> DTO에 쿼리를 고정시켰기 때문, API스펙이 repository에 포함되게 된다. (JPQL내 객체를 생성하는 구문때문)
    // -> API스펙이 변경되면 JPQL도 변경해야하는 문제 발생
    // 성능은 v4, 재사용성은 v3가 좋다 -> 어떻게 선택해야할까??
    /*
    쿼리 방식 선택 권장 순서
    1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다. (v2)
    2. 필요하면 페치조인으로 성능을 최적화한다 -> 대부분의 성능 이슈가 해결된다. (v3)
    3. 그래도 안도면 DTO로 직접 조회하는 방법을 사용한다. (v4)
    4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용한다.
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }




}
