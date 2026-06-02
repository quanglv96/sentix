package sansan.sentix.Entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ST_TICKERS")
@Data
public class TickerEntity {

    @Id
    @Column(name = "ticker")
    private String ticker;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "sector_id", nullable = false)
    private Long sectorId;

    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;

    @Column(name = "f_score")
    private Integer fScore;

    @Column(name = "z_score")
    private BigDecimal zScore;

    @Column(name = "sentiment_index_7d")
    private BigDecimal sentimentIndex7d;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}