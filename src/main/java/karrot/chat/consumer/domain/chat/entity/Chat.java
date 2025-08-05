package karrot.chat.consumer.domain.chat.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "thumbnail_id")
    private Long thumbnailId;
}
