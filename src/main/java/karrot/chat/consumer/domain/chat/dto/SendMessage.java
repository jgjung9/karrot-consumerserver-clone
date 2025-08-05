package karrot.chat.consumer.domain.chat.dto;

import karrot.chat.consumer.domain.chat.entity.UserChat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SendMessage {
    private Long receiverId;
    private Long chatId;
    private Long senderId;
    private String message;
    private LocalDateTime createAt;
}
