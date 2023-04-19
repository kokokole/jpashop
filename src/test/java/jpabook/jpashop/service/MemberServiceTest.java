package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;
//
//    @Autowired
//    EntityManager em;


    @Test
//    @Rollback(false) // 테스트여도 rollback안되게 설정할 때 사용
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //commit을 안해서 실제로는 insert가 되지 않는다. //테스트라 rollback됨
//        em.flush(); // rollback하지만 insert까지 해보고 싶을때


        //then
        assertEquals(member, memberRepository.findOne(savedId)); // Assert.assertEquals



    }


    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{

        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        //when

        memberService.join(member1);
        memberService.join(member2);

        //then
        fail("에외가 발생해야 한다."); //Assert.fail
    }

}