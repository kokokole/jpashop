package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "memeber_id") // name 지정안하면 변수이름이 칼럼 name이 됨
    private Long id;

//    @NotEmpty
    private String name;

    @Embedded
    private Address address;

//    @JsonIgnore // Entity를 JSON으로 전환 시 해당 필드를 무시 이러면 Entity내 API를 위한 설정이 들어가게 되므로 유지보수에 좋지 않다
    @OneToMany(mappedBy = "member") // Order 테이블에 있는 member라는 변수와 매핑
    private List<Order> orders = new ArrayList<>();


}
