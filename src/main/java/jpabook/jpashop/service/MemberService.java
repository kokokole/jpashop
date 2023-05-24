package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@Transactional //public 메소드는 기본적으로 Transaction안에서 동작함
//각 메소드에 Transactional을 설정할 수도 있음
//클래스 내에서 기본적인 Transactionl 정책을 설정할 때 클래스 대상으로 @Transactional 해준다고 보면됨
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    //필드 주입
//    @Autowired
//    private MemberRepository memberRepository;

    //변경이 없기 때문에 final 추천(컴파일 시 에러 확인 가능)
    private final MemberRepository memberRepository;


    //생성자 주입
    //의존관계를 파악 하기 쉽다.
//   @Autowired // 생성자가 1개일 경우 생략가능
//   ==> lombok의 @AllArgsContructor를 사용하면 자동생성, 자동 Autowired 설정 // 필드가 final이기 때문에 @RequiredArgsConstructor(-->final필드만 가지고 생성자를 생성)를 사용해도 됨
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /*
    //setter 메서드 주입
    // 굳이 이렇게 쓸 필요는 없다.
    private MemberRepository memberRepository;
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
     */




    /**
     * 회원가입
     * @param member
     * @return
     */
    @Transactional // 클래스쪽에 @Transactional(readOnly=true)이지만, 데이터 변경 메소드는 readOnly = false로 쓰겠다.
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //중복회원일 경우 예외처리

        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     *회원 전체 조회
     */
//    @Transactional(readOnly = true) // select 시 readOnly로 할 경우 성능면에서 이점이 있다.
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /**
     * 특정회원 조회
     * @param memberId
     * @return
     */
//    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
