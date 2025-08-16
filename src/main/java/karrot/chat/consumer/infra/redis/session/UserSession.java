package karrot.chat.consumer.infra.redis.session;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSession {
    private Long userId;
    private String serverUrl;
}
