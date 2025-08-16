package karrot.chat.consumer.domain.chat.repository;

import karrot.chat.consumer.domain.chat.entity.UserChat;
import karrot.chat.consumer.domain.chat.entity.UserChatId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserChatRepository extends JpaRepository<UserChat, UserChatId>, UserChatRepositoryCustom {
    List<UserChat> findByChatId(Long chatId);
}
