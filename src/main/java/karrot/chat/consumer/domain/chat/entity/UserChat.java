package karrot.chat.consumer.domain.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

@Entity
@Data
@IdClass(UserChatId.class)
public class UserChat {
    @Id
    private Long chatId;

    @Id
    private Long userId;

    private Boolean mute;

    @Column(name = "display_idx")
    private String displayIdx;

    public UserChatId getUserChatId() {
        return new UserChatId(chatId, userId);
    }
}