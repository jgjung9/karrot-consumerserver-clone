package karrot.chat.consumer.infra.redis.stream;

import jakarta.annotation.PostConstruct;
import karrot.chat.consumer.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class RedisStreamConsumer {

    private final RedisTemplate<String, String> redisTemplate;
    private final ChatService chatService;
    private final String streamKey = "chat-stream";
    private final String groupName = "chat-consumer";
    private final String consumerName = UUID.randomUUID().toString();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    @Value("${scheduling.enabled:true}")
    private boolean scheduling;

    @PostConstruct
    private void init() {
        createConsumerGroup();
    }

    public void createConsumerGroup() {
        if (!redisTemplate.hasKey(streamKey)) {
            redisTemplate.opsForStream()
                    .add(streamKey, Map.of("createAt", LocalDateTime.now().toString()));
        }

        boolean exists = false;
        StreamInfo.XInfoGroups groups = redisTemplate.opsForStream().groups(streamKey);
        for (StreamInfo.XInfoGroup group : groups) {
            if (group.groupName().equals(groupName)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            redisTemplate.opsForStream().createGroup(streamKey, groupName);
        }
    }

    @Scheduled(fixedDelay = 100)
    private void consume() {
        if (!scheduling) {
            return;
        }

        List<MapRecord<String, Object, Object>> chatData = getAllFromStream(streamKey);
        for (MapRecord<String, Object, Object> chatDatum : chatData) {
            Map<Object, Object> chatInfo = chatDatum.getValue();
            chatService.sendChat(
                    Long.valueOf((String) chatInfo.get("chatId")),
                    Long.valueOf((String) chatInfo.get("senderId")),
                    (String) chatInfo.get("message"),
                    LocalDateTime.parse((String) chatInfo.get("createAt"), formatter)
            );
            redisTemplate.opsForStream()
                    .acknowledge(streamKey, groupName, chatDatum.getId());
        }
    }

    public List<MapRecord<String, Object, Object>> getAllFromStream(String streamKey) {
        return redisTemplate.opsForStream()
                .read(
                        Consumer.from(groupName, consumerName),
                        StreamReadOptions.empty(),
                        StreamOffset.create(streamKey, ReadOffset.lastConsumed())
                );
    }
}
