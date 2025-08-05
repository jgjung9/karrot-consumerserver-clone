package karrot.chat.consumer.domain.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import karrot.chat.consumer.domain.chat.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import static karrot.chat.consumer.domain.chat.entity.QMessage.message;

@Repository
@RequiredArgsConstructor
@Transactional
public class MessageRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public Long save(Message message) {
        if (message.getId() == null) {
            em.persist(message);
        } else {
            em.merge(message);
        }
        return message.getId();
    }

    public Message findById(Long id) {
        return queryFactory.selectFrom(message)
                .where(message.id.eq(id))
                .fetchOne();
    }
}
