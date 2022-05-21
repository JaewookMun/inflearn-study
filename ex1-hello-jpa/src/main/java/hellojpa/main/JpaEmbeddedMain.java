package hellojpa.main;

import hellojpa.Address;
import hellojpa.AddressEntity;
import hellojpa.Member;
import hellojpa.Period;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

public class JpaEmbeddedMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();


        try {

//            Member member = new Member();
//
//            member.setUsername("hello");
//            member.setWorkPeriod(new Period());
//            member.setHomeAddress(new Address("city", "street", "zipcode"));
//            em.persist(member);
            Member member = new Member();
            member.setUsername("memeber1");
            member.setHomeAddress(new Address("homeCity", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new AddressEntity("old1", "street", "10000"));
            member.getAddressHistory().add(new AddressEntity("old2", "street", "10000"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("======================= START =======================");
            Member findMember = em.find(Member.class, member.getId());

            // 컬렉션들은 지연로딩.
//            List<Address> addressHistory = findMember.getAddressHistory();
//            for (Address address : addressHistory) {
//                System.out.println("address = " + address.getCity());
//            }
//
//            Set<String> favoriteFoods = findMember.getFavoriteFoods();
//            for (String favoriteFood : favoriteFoods) {
//                System.out.println("favoriteFood = " + favoriteFood);
//            }

            //homeCity -> newCity
            // 값 타입은 아래와 같은 방식으로 교체를 해야함.
//            findMember.getHomeAddress().setCity("newCity");
//            Address a = findMember.getHomeAddress();
//            findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipcode()));
//
//            //치킨 -> 한식
//            //'String'은 통채로 갈아 끼워줘야함.
//            findMember.getFavoriteFoods().remove("치킨");
//            findMember.getFavoriteFoods().add("한식");
//
//            // 기본적으로 컬렉션은 데이터를 찾을 때 equals를 사용한다.
//            // 컬렉션을 다룰 때 equals, hashCode 가 의미 있음
//            findMember.getAddressHistory().remove(new Address("old1", "strret", "10000"));
//            findMember.getAddressHistory().add(new Address(("newCity1", "strret", "10000"));


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close(); // EntityManager를 닫는 것은 굉장히 중요
        }
        emf.close(); // 닫아서 resource를 release 해준다.
    }
}
