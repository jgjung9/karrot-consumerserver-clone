package karrot.chat.consumer.domain.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import karrot.chat.consumer.domain.chat.entity.QUserChat;
import karrot.chat.consumer.domain.chat.entity.UserChat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static karrot.chat.consumer.domain.chat.entity.QUserChat.userChat;

@Repository
@RequiredArgsConstructor
@Transactional
public class UserChatRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public void save(UserChat userChat) {
        return;
    }

    public List<UserChat> findAllByChatId(Long chatId) {
        return queryFactory.selectFrom(userChat)
                .where(userChat.chatId.eq(chatId))
                .fetch();
    }
}
