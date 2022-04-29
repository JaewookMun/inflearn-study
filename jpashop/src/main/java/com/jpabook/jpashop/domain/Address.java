package com.jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    // jpa spec 상 기본생성자가 필요하기 때문에 작성
    // public or protected로 설정해야한다. (public 보다는 protected가 안전)
    // jpa가 프록시를 생성하거나 리플렉션을 쓸 때 기본생성자가 필요하기 때문에...
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}
