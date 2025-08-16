package karrot.chat.consumer.domain.chat.repository;

import karrot.chat.consumer.domain.chat.entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserChatRepository extends JpaRepository<UserChat, Long>, UserChatRepositoryCustom {
    List<UserChat> findByChatId(Long chatId);
}
