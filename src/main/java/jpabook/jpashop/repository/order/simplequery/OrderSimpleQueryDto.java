package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public OrderSimpleQueryDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY 초기화 -> 객체가 생성될 때 영속성 컨텍스트에서 데이터가 없으면 쿼리가 실행됨
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화 -> 객체가 생성될 때 영속성 컨텍스트에서 데이터가 없으면 쿼리가 실행됨

        }

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name; // LAZY 초기화 -> 객체가 생성될 때 영속성 컨텍스트에서 데이터가 없으면 쿼리가 실행됨
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address; // LAZY 초기화 -> 객체가 생성될 때 영속성 컨텍스트에서 데이터가 없으면 쿼리가 실행됨

    }

}
