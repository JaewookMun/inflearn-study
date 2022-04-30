package hellojpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        // persistence.xml 에 있는 persistence-unit name을 넣어준다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    }
}
