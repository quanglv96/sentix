package sansan.sentix.Entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ST_USER_POSITIONS")
@Data
public class UserPositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "st_positions_seq")
    @SequenceGenerator(name = "st_positions_seq", sequenceName = "seq_st_positions", allocationSize = 1)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "ticker", nullable = false)
    private String ticker;

    @Column(name = "volume", nullable = false)
    private Integer volume;

    @Column(name = "buy_price", nullable = false)
    private BigDecimal buyPrice;

    @Column(name = "highest_price_since_buy", nullable = false)
    private BigDecimal highestPriceSinceBuy;

    @Column(name = "stop_loss_pct")
    private BigDecimal stopLossPct;

    @Column(name = "trailing_stop_pct")
    private BigDecimal trailingStopPct;

    @Column(name = "activated_trailing")
    private Integer activatedTrailing; // Map với NUMBER(1) trong Oracle (0: False, 1: True)

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}