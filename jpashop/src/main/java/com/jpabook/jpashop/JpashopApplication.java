package com.jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) { SpringApplication.run(JpashopApplication.class, args); }

	@Bean
	Hibernate5Module hibernate5Module() {
		Hibernate5Module hibernate5Module = new Hibernate5Module();
//		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
		// JSON 생성 시점에 Lazy loading을 다 해버리는 옵션.
		// 이렇게 사용하면 안됨. 1. entity의 변동가능성 2. 성능문제 (필요없는 정보들도 조회해서 가져옴)
		return hibernate5Module;
	}
}
