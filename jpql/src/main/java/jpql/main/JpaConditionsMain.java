package jpql.main;

import jpql.Member;
import jpql.MemberType;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaConditionsMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("관리자");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            member.setTeam(team);
            em.persist(member);


            em.flush();
            em.clear();

            // 기본 CASE 식
            String query1 =
                    "select " +
                            "case when m.age <= 10 then '학생요금'" +
                            "     when m.age >= 60 then '경로요금'" +
                            "     else '일반요금' " +
                            "end " +
                    "from Member m";

            // COALESCE
            String query2 = "select coalesce(m.username, '이름 없는 회원') from Member m";

            // NULLIF
            String query3 = "select nullif(m.username, '관리자') from Member m";


            List<String> result = em.createQuery(query3, String.class)
                    .getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }


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
