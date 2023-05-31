package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct//생성 후 자동호출
    public void init(){
        initService.doInit1();
        initService.doInit2();

    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService{
        private final EntityManager em;

        public void doInit1(){
            Member member = createMember("UserA", "서울", "1", "1111");
            em.persist(member);


            Book book = new Book();
            book.setName("JPA1 Book");
            book.setPrice(10000);
            book.setStockQuantity(100);
            em.persist(book);

            Book book2 = new Book();
            book2.setName("JPA2 Book");
            book2.setPrice(20000);
            book2.setStockQuantity(100);
            em.persist(book2);


            OrderItem orderItem1 = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }

        public void doInit2(){
            Member member = createMember("UserB", "대구", "2", "2222");
            em.persist(member);


            Book book = new Book();
            book.setName("Spring1 Book");
            book.setPrice(20000);
            book.setStockQuantity(200);
            em.persist(book);

            Book book2 = new Book();
            book2.setName("Spring2 Book");
            book2.setPrice(40000);
            book2.setStockQuantity(300);
            em.persist(book2);


            OrderItem orderItem1 = OrderItem.createOrderItem(book, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);



        }

     
        
        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

    }

}

