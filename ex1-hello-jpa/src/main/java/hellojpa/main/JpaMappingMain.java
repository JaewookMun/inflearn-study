package hellojpa.main;

import hellojpa.Member;
import hellojpa.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMappingMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            // 저장
            Team team = new Team();
            team.setName("TeamA");

            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
//            member.changeTeam(team); //**
            em.persist(member);

            team.addMember(member); //연관관계 편의 메서드는 한쪽에만 사용.

            //주인이 아닌 필드는 단순한 조회를 사용 (DB에 수정 & 추가 적용 안됨)
            //but, OOP 관점에서 양쪽 모두 값을 셋팅해주는 것이 좋다.
            // 항상 양쪽에 값을 설정!!!
//            team.getMembers().add(member); //**

            em.flush();
            em.clear();

            Team findTeam = em.find(Team.class, team.getId());
            List<Member> members = findTeam.getMembers();

            for (Member m : members) {
                System.out.println("m = " + m.getUsername());
            }


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
