package karrot.chat.consumer.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SendMessage {
    private Long senderId;
    private Long chatId;
    private Long receiverId;
    private String message;
    private LocalDateTime createdAt;
}
