package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
/*
    @Test
    @Transactional //EntityManager 사용시 Transactional 사용 필수, 테스트에서 Transactional 사용 시 DB를 롤백한다.
    @Rollback(false) // Transactional 사용하면서 테스트 시 롤백을 안할 경우
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
        System.out.println(findMember == member); // Transactional안에서 persistance context가 같기 때문에 id 값이 같으면 같은 객체(entity)로 본다
        //persistance context 내 같은 id값을 지닌 entity가 존재하기 때문에 조회(find) 시 해당 entity를 반환하여 findMember와 member가 같게 된다.
        //그래서 find 실행 시 같은 id값을 가진 entity가 1차 캐시에 존재하기 때문에 select 문 없이 해당 entity를 반환하게 된다. 실제 테스트 실행해보면 select문 실행이 없다.

    }*/
}