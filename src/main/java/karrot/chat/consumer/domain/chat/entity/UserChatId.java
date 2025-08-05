package karrot.chat.consumer.domain.chat.entity;

import jakarta.persistence.Column;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class UserChatId implements Serializable {
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "user_id")
    private Long userId;

    public UserChatId() {}

    public UserChatId(Long chatId, Long userId) {
        this.chatId = chatId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserChatId messageId = (UserChatId) o;
        return Objects.equals(chatId, messageId.chatId) && Objects.equals(userId, messageId.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, userId);
    }
}