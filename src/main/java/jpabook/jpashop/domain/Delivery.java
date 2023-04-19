package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;


    @OneToOne(mappedBy = "delivery", fetch=FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)// STRING이 아닌 ORDINAL로 쓰게 되면 READY,와 COMP 사이에 XXX 값이 추가가 되어 COMP가 세번째 값이 되면 추가하기전 데이터 들이 XXX로 바뀐다. (XXX가 두번째 값으로 되어서 그럼)
    private DeliveryStatus status; // READY, COMP

}
