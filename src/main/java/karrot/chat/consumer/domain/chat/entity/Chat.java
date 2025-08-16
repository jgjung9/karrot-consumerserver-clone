package karrot.chat.consumer.domain.chat.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    private String title;

    @Column(name = "thumbnail_id")
    private Long thumbnailId;

    @Enumerated(value = EnumType.STRING)
    private ChatType type;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "chat")
    private List<UserChat> userChats = new ArrayList<>();
}
