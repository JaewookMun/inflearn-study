package com.jpabook.jpashop;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

//    @Autowired
//    MemberRepository memberRepository;
//
//    // @Transactional 이 @Test에 있으면 테스트가 끝나면 DB를 롤백해버린다.
//    @Test
//    @Transactional // 스프링 프레임워크에 종속적인 코드를 작성하기 때문에 스프링꺼를 사용하는 것을 권장
//    @Rollback(false)
//    public void testMember() throws Exception {
//        //given
//        Member member = new Member();
//        member.setUsername("memberA");
//
//        //when
//        Long saveId = memberRepository.save(member);
//        Member findMember = memberRepository.find(saveId);
//
//        //then
//        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
//        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
//        Assertions.assertThat(findMember).isEqualTo(member);
//        System.out.println("findMember == member " + (findMember == member));
//        // 같은 transcation에서 조회했을 때 id 값이 같으면 똑같은것으로 취급
//        // -> 영속성 컨텍스트 (기본편)
//    }
}