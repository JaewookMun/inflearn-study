package hellojpa.main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        // persistence.xml 에 있는 persistence-unit name을 넣어준다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        // JPA는 트랜잭션이 굉장히 중요
        // JPA에서 하는 모든 작업은 트랜잭션에서 이루어져야 한다.
        EntityTransaction tx = em.getTransaction();
        tx.begin();


        try {
//            Member member = new Member();
//            member.setId(2L);
//            member.setName("helloB");
//            em.persist(member);
//            Member findMember = em.find(Member.class, 1L);
//            findMember.setName("HelloJPA");

            // Java Collection을 다루는 것처럼 사용할 수 있도록 설계됨.
            // -> 조회 후 값을 수정한 뒤 후 처리가 필요없음.
            // JPA를 통해 엔티티를 가져오면 데이터를 JPA가 관리함.
            // Transaction 커밋 시점에 변경사항이 있으면 commit하기 전 update 쿼리를 실행하고 커밋한다.
            // EntityManager는 쓰레드간에 공유하면 안되고 사용하면 바로 없애야한다.
            // JPA의 모든 데이터 변경은 Transaction 안에서 실행

            // 쿼리 작성 가능.
            // jpql은 테이블 대상이 아닌 클래스를 대상으로 쿼리를 작성한다.
            // -> 객체를 대상으로하는 객체지향 쿼리라 생각가능
//            List<Member> result = em.createQuery("select m from Member as m", Member.class)
//                    .getResultList();
//
//            for (Member member : result) {
//                System.out.println("member.name = " + member.getName());
//            }


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close(); // EntityManager를 닫는 것은 굉장히 중요

        }
        emf.close(); // 닫아서 resource를 release 해준다.
    }
}
