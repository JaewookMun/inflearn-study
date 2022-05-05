package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaEntityMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();


        try {
//
//            // 비영속
//            Member member = new Member();
//            member.setId(101L);
//            member.setName("HelloJPA");
//
//            // 영속
//            System.out.println("=== BEFORE ===");
//            em.persist(member);
//            System.out.println("=== AFTER ===");
//
//            Member findMember = em.find(Member.class, 101L);
//
//            System.out.println("findMember.Id = " + findMember.getId());
//            System.out.println("findMember.name = " + findMember.getName());
//
//            Member findMember1 = em.find(Member.class, 101L);
//            Member findMember2 = em.find(Member.class, 101L);
//            Member member1 = new Member(150L, "A");
//            Member member2 = new Member(160L, "A");
//
//            em.persist(member1);
//            em.persist(member2);

//            Member member = em.find(Member.class, 150L);
//            member.setName("ZZZZ");

            // JPA의 목적이 자바 컬렉션과 같이 객체를 다루는 것.

            //영속
//            Member member = em.find(Member.class, 150L);
//            member.setName("AAAA");
//
//            em.clear();
//
//            Member member2 = em.find(Member.class, 150L);


            System.out.println("========================");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close(); // EntityManager를 닫는 것은 굉장히 중요

        }
        emf.close(); // 닫아서 resource를 release 해준다.
    }
}
