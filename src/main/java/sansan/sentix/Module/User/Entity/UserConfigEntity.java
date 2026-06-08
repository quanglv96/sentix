package sansan.sentix.Module.User.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ST_USER_CONFIGS")
@Data
public class UserConfigEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "default_stop_loss_pct")
    private BigDecimal defaultStopLossPct;

    @Column(name = "default_trailing_stop_pct")
    private BigDecimal defaultTrailingStopPct;

    @Column(name = "telegram_chat_id")
    private String telegramChatId;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}