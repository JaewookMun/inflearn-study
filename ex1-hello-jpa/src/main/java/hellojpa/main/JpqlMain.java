package hellojpa.main;

import hellojpa.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JpqlMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // JPQL - 단순 String이기 때문에 동적쿼리를 작성하기 어렵다.
            // 동적쿼리를 작성하기 매우 어려움.
            String qlString = "select m From Member m where m.username like '%kim%'";

            //select m from Member m <- Member 자체를 조회
            List<Member> result = em.createQuery(qlString
                    , Member.class)
                    .getResultList();


            // Criteria
            // 장점
            // - compile 오류를 확인가능
            // - 동적쿼리 작성 용이
            // 단점
            // SQL 스럽지 않다.
            // 유지보수가 어렵다.
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            Root<Member> m = query.from(Member.class);

            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));

            String username = "dsafas";
            if (username != null) {
                cq = cq.where(cb.equal(m.get("username"), "kim"));
            }

            List<Member> resultList = em.createQuery(cq).getResultList();


            // QueryDSL


            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            // flush -> commit(), query 실행

            // em.flush();

            // dbConnection.executeQuery("select * from member");
            // ㄴ 결과 0 -> jpa 는 flush 가 이루어져야 쿼리를 실행하기 때문에
            // JPA 와 관계 없는 방식으로 SQL 을 실행하기 직전에 직접 flush 해줘야한다.

            // Native Query
            List<Member> resultList1 = em.createNativeQuery("select member_id, city, street, zipcode, username from member", Member.class)
                    .getResultList();


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
