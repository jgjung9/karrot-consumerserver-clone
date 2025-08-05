package karrot.chat.consumer.infra.redis.session;

import lombok.Data;

@Data
public class UserSession {
    private Long userId;
    private String serverUrl;
}
