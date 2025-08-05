package karrot.chat.consumer.domain.chat.repository;

import karrot.chat.consumer.domain.chat.entity.Message;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void save() {
        Message message = new Message(null, 1L, 1L, "hello", LocalDateTime.now());
        Long savedId = messageRepository.save(message);

        Message found = messageRepository.findById(savedId);
        assertThat(found).isEqualTo(message);
    }
}