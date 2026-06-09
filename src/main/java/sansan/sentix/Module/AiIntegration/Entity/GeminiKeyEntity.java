package sansan.sentix.Module.AiIntegration.Entity;

import lombok.Data;
import sansan.sentix.Module.AiIntegration.Converter.GeminiKeyStatusConverter;
import sansan.sentix.Module.AiIntegration.Utils.GeminiKeyStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ST_GEMINI_KEY", schema = "XIANGQUANG")
public class GeminiKeyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "API_KEY", nullable = false)
    private String apiKey;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "STATUS")
    @Convert(converter = GeminiKeyStatusConverter.class)
    private GeminiKeyStatus status; // 1: Active, 0: Blocked/Hỏng

    @Column(name = "DAILY_COUNT")
    private Integer dailyCount;

    @Column(name = "BLOCKED_UNTIL")
    private LocalDateTime blockedUntil;
}