package karrot.chat.consumer.infra.redis.stream;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class RedisStreamConsumerTest {
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
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RedisStreamConsumer redisStreamConsumer;

    @Before
    public void createConsumerGroup() {
        redisStreamConsumer.createConsumerGroup();
    }

    @Test
    void getFromStream() {
        Map<Object, Object> data1 = Map.of(
                "senderId", "1",
                "chatId", "1",
                "message", "test message1",
                "createAt", LocalDateTime.now().toString()
        );
        Map<Object, Object> data2 = Map.of(
                "senderId", "2",
                "chatId", "1",
                "message", "test message2",
                "createAt", LocalDateTime.now().toString()
        );

        redisTemplate.opsForStream().add("chat-stream", data1);
        redisTemplate.opsForStream().add("chat-stream", data2);

        List<Map<Object, Object>> result = redisStreamConsumer.getAllFromStream("chat-stream")
                .stream()
                .map(record -> record.getValue())
                .toList();
        assertThat(2).isEqualTo(result.size());
        assertThat(result.get(0)).containsAllEntriesOf(data1);
        assertThat(result.get(1)).containsAllEntriesOf(data2);
    }
}