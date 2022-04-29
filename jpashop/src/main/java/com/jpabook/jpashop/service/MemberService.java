package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.repository.MemberRepository;
import com.jpabook.jpashop.repository.MemberRepositoryOld;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// JPA 의 데이터 변경은 Transactional안에서 수행되어야함 - spring에서 제공하는 Transactional 권장
// class에 Transactional을 사용하면 하위 public 메서드가 모두 적용.
@Transactional(readOnly = true) // 읽기에는 readOnly = true 사용
@RequiredArgsConstructor // final 이 붙은 필드에 대한 생성자를 생성
//@AllArgsConstructor
public class MemberService {
    
    //@Autowired
    private final MemberRepository memberRepository;
    // 인터페이스만 만들고 선언하면 JPA가 알아서 빈을 만들어서 넣어준다.

    // 필드 주입 바꿀수 없다는 단점 존재. - 주입하기 까다로움 (테스트 어렵)
    // final 접근제어자를 사용하면

//    @Autowired // 생성자가 하나만 있으면 @Autowired를 하지 않아도 됨.
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
    // 생성자 injection 사용을 권장한다. -

    // setter 방식은 테스트하기에는 편리하지만 바뀔 수 있다는 단점이 존재
    /*
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    */

    //회원 가입
    @Transactional // 따로 설정 하면 readOnly = false 가 적용
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //인터페이스로 선언할 수 없는 것은 만들어줘야함.
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    // Transaction이 끝나고 커밋할 때 JPA가 영속성 객체의 변경값을 반영.
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findById(id).get();
        member.setName(name);
    }
}
