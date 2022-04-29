package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        // Item은 DB에 넣을 때 까지 id값이 없다.
        if(item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item); // merge는 실무에서 사용할 일이 크게 없다. - JPA에서 권장하는 데이터 수정방법은 아님.
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }


}
