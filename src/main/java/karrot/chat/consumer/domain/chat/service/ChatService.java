package karrot.chat.consumer.domain.chat.service;

import karrot.chat.consumer.domain.chat.entity.Message;
import karrot.chat.consumer.domain.chat.repository.UserChatRepository;
import karrot.chat.consumer.infra.redis.repository.UserSessionRepository;
import karrot.chat.consumer.domain.chat.dto.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final RestTemplate restTemplate;
    private final UserChatRepository userChatRepository;
    private final UserSessionRepository userSessionRepository;

    @Transactional
    public void sendChat(Long chatId, Long senderId, String messageBody, LocalDateTime createdAt) {
        log.debug("sendChat");

        List<Long> userIds = userChatRepository.findByChatId(chatId).stream()
                .map(uc -> uc.getUserId())
                .toList();
        log.debug("userIds = {}", userIds);
        userSessionRepository.findAllByUserIds(userIds).stream()
                .forEach(session -> {

                    log.debug("session: {}", session);
                    // 현재 채팅 서버에 연결x 유저의 경우
                    if (session == null) {
                        // TODO: 세션 정보가 없는 경우 알림(푸시) 서버로 전달
                        return;
                    }

                    // 보내는 사람과 받는 사람이 같은 경우
                    if (session.getUserId().equals(senderId)) {
                        return;
                    }
                    SendMessage request = SendMessage.builder()
                            .senderId(senderId)
                            .chatId(chatId)
                            .receiverId(session.getUserId())
                            .message(messageBody)
                            .createdAt(createdAt)
                            .build();
                    log.debug("sendChat request = {}", request);
                    restTemplate.put(
                            "http://" + session.getServerUrl() + "/chat/deliver",
                            request
                    );
                });
    }
}
