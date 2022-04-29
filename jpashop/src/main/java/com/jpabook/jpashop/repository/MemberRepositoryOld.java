package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryOld {

    //@PersistenceContext
    // springboot.data.jpa 에서 지원 - @Autowired 가능
    // 원래는 @PersistenceContext Annotation이 필요.
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        // find(type, pk)
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        // jpql 사용 - SQL과 차이점존재
        // entity 객체를 대상으로 수행 (sql같은 경우 테이블을 대상으로 수행)
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
