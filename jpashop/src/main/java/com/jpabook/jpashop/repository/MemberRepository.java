package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 구현체를 Spring Data JPA가 자동으로 만들어서 연결해 준다.

    //select m from member m where m.name = :name
    List<Member> findByName(String name);
}
