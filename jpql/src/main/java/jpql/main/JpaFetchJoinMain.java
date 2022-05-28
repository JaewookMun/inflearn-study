package jpql.main;

import jpql.Member;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class JpaFetchJoinMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            // 페치조인
            String query1 = "select m from Member m join fetch m.team";

            // 컬렉션 페치조인
            String query2 = "select distinct t from Team t join fetch t.members";

            String query3 = "select t from Team t join t.members m";

            List<Team> result = em.createQuery(query3, Team.class)
                    .getResultList();

            System.out.println("result = " + result.size());

//            for (Member member : result) {
//                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
//                //회원1, 팀A(SQL)
//                //회원2, 팀A(1차캐시)
//                //회원3, 팀B(SQL)
//
//                //회원 100명 -> N + 1 문제(최악의 경우 쿼리 101번 실행)
//
//            }

            for (Team team : result) {
                System.out.println("team = " + team.getName() + " and memmbers=" + team.getMembers().size());

                for(Member member : team.getMembers()) {
                    System.out.println("  -> member = " + member);
                }
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
