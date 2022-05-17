package hellojpa.main;

import hellojpa.Member;
import hellojpa.Team;
import org.hibernate.Hibernate;

import javax.persistence.*;

public class JpaProxyMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();


        try {

            Member member1 = new Member();
            member1.setUsername("member1");
            em.persist(member1);

            em.flush();
            em.clear();

            Member refMember = em.getReference(Member.class, member1.getId());
            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));
            System.out.println("refMember = " + refMember.getClass()); //Proxy
            Hibernate.initialize(refMember); //강제초기화


            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember = " + findMember.getClass()); //Member

            System.out.println("refMember == findMember : " + (refMember == findMember));
            // proxy를 한번 조회하면 find를 사용해도 프록시로 생성됨.
            // @@@ JPA에서는 동일 트랜잭션에서 같은 조건으로 검색한 대상은 항상 같다.

//            Member findMember = em.find(Member.class, member.getId());
//            Member findMember = em.getReference(Member.class, member1.getId());
//            System.out.println("before findMember = " + findMember.getClass());
//            System.out.println("findMember.getUsername() = " + findMember.getUsername());
//            System.out.println("after findMember = " + findMember.getClass());


//            printMemeber(member);

//            printMemberAndTeam(member);

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();

        }
        emf.close();
    }


    /**
     * 상황에 따라 Member 만출력 / Member와 Team을 출력
     * 형태로 나뉠 수 있음.
     * 
     * @param member
     */
    
//    private static void printMemeber(Member member) {
//        System.out.println("member = " + member.getUsername());
//    }
//
//    private static void printMemberAndTeam(Member member) {
//        String username = member.getUsername();
//        System.out.println("username = " + username);
//
//        Team team = member.getTeam();
//        System.out.println("team = " + team.getName());
//    }
}
