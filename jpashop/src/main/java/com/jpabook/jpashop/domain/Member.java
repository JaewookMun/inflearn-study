package com.jpabook.jpashop.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    // 단점 - presentation 관련 검증 로직이 엔티티 안에 모두 존재함.
    // entity를 수정했을 때 api 스팩이 변경되는 것이 문제.
    @NotEmpty
    private String name;

    @Embedded // 내장 타입
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member") // 매핑을 주체적으로 하는 것이 아니라 Order의 member의 값을 읽는다.
    private List<Order> orders = new ArrayList<>();
}
