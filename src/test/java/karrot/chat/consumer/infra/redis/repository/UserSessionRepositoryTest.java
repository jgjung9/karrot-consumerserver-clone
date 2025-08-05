package karrot.chat.consumer.infra.redis.repository;

import karrot.chat.consumer.infra.redis.session.UserSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class UserSessionRepositoryTest {

    private static final String REDIS_IMAGE = "redis:8.2-alpine";
    private static final int PORT = 6379;

    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>(REDIS_IMAGE)
            .withExposedPorts(PORT);

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", () -> redisContainer.getHost());
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(PORT));
    }


    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void save() {
        UserSession session = new UserSession();
        session.setUserId(1L);
        session.setServerUrl("localhost:8080");
        userSessionRepository.save(session);
        String serverUrl = redisTemplate.opsForValue()
                .getAndDelete("user:session:" + session.getUserId());
        assertThat(serverUrl).isEqualTo(session.getServerUrl());
    }

    @Test
    void findAllByUserIds() {
        UserSession session = new UserSession();
        session.setUserId(1L);
        session.setServerUrl("localhost:8080");
        userSessionRepository.save(session);

        List<UserSession> userSessions = userSessionRepository.findAllByUserIds(List.of(session.getUserId()));
        assertThat(userSessions).contains(session);
    }
}