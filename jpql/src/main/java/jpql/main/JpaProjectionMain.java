package jpql.main;

import jpql.Address;
import jpql.Member;
import jpql.MemberDto;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaProjectionMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("memeber1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            /**
             * 엔티티 프로젝션
             */
            // > 컬렉션으로 조회된 모든 대상을 영속성 컨텍스트에서 관리
            // > 변경사항이 있으면 해당 내용이 다 반영됨
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                .getResultList();

            Member findMember = result.get(0);
            findMember.setAge(20);

            // join query 실행 - join은 명시적으로 유사해야함.
            // jpql은 실행될 sql 형태와 유사해야함. - 예측이 쉽게 가능해야함.
//            List<Team> result1 = em.createQuery("select m.team from Member m", Team.class)
            List<Team> result1 = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();

            

            /**
             * 임베디드 타입 프로젝션
             */

            List<Address> result2 = em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();

            
            
            /**
             * 스칼라 타입 프로젝션
             */
            // 일반 sql 과 유사 - 여러 값들을 한번에 조회한다.
            // Query Type
            List resultList = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();


            Object o = resultList.get(0);
            Object[] results = (Object[]) o;
            System.out.println("username = " + results[0]);
            System.out.println("age = " + results[1]);


            // Object[] type
            List<Object[]> list2 = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();


            // new keyword
            List<MemberDto> resultList1 = em.createQuery("select new jpql.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                    .getResultList();

            MemberDto memberDto = resultList1.get(0);
            System.out.println("memberDto = " + memberDto.getUsername());
            System.out.println("memberDto = " + memberDto.getAge());

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
