package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity // Entity 어노테이션이 있어야 JPA가 처음에 로드될 때 사용해야할 Entity인 것을 알 수 있다.
//@Table(name = "USER") // 필요시 테이블 이름을 별도로 설정가능
public class Member {

    @Id
    @GeneratedValue//(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
