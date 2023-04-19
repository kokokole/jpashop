package jpabook.jpashop.domain;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "memeber_id") // name 지정안하면 변수이름이 칼럼 name이 됨
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // Order 테이블에 있는 member라는 변수와 매핑
    private List<Order> orders = new ArrayList<>();


}
