package karrot.chat.consumer.infra.redis.repository;

import karrot.chat.consumer.infra.redis.session.UserSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserSessionRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void save(UserSession session) {
        redisTemplate.opsForValue()
                .set(
                        "user:session:" + session.getUserId().toString(),
                        session.getServerUrl()
                );
    }

    public List<UserSession> findAllByUserIds(List<Long> userIds) {
        List<String> parsedUserIds = userIds.stream().map(id -> "user:session:" + id.toString()).toList();
        log.debug("parsedUserIds = {}", parsedUserIds);
        List<String> serverInfos = redisTemplate.opsForValue()
                .multiGet(parsedUserIds);

        List<UserSession> sessions = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            String serverInfo = serverInfos.get(i);
            if (serverInfo == null || serverInfo.isEmpty()) {
                sessions.add(null);
                continue;
            }

            sessions.add(
                    UserSession.builder()
                    .userId(userIds.get(i))
                    .serverUrl(serverInfo)
                    .build()
            );
        }
        log.debug("findAllByUserIds sessions = {}", sessions);
        return sessions;
    }
}
