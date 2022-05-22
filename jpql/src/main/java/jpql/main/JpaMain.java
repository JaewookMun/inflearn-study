package jpql.main;

import jpql.Member;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /***********
             * 기본문법
             */
            
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

//            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
//            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
//
//            Query query3 = em.createQuery("select m.username, m.age from Member m");

//            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);

//            List<Member> resultList = query.getResultList();
//            Member singleResult = query.getSingleResult();
            // Spring Data JPA -> Exception 을 터트리지 않고 처리하여 결과 제공


            Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();

            System.out.println("singleResult = " + singleResult.getUsername());


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
