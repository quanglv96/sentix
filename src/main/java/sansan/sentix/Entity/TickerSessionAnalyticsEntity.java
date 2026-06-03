package sansan.sentix.Entity;

import lombok.Data;
import sansan.sentix.Entity.ConvertStatus.MarketSessionConverter;
import sansan.sentix.Utils.MarketSession;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ST_TICKER_SESSION_ANALYTICS")
public class TickerSessionAnalyticsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Khai báo ID tự tăng
    @Column(name = "ID")
    private Long id;

    @Column(name = "TICKER")
    private String ticker;

    @Column(name = "ANALYTICS_DATE")
    private LocalDate analyticsDate;

    @Column(name = "MARKET_SESSION")
    @Convert(converter = MarketSessionConverter.class)
    private MarketSession marketSession;

    @Column(name = "CLOSE_PRICE")
    private BigDecimal closePrice;

    @Column(name = "VOLUME")
    private Long volume;

    @Column(name = "TICKER_SENTIMENT")
    private BigDecimal tickerSentiment;

    @Column(name = "SECTOR_SENTIMENT")
    private BigDecimal sectorSentiment;

    @Column(name = "TA_RSI_14")
    private BigDecimal taRsi14;

    @Column(name = "TA_MA_20")
    private BigDecimal taMa20;

    @Column(name = "VAL_PE")
    private BigDecimal valPe;

    @Column(name = "VAL_PB")
    private BigDecimal valPb;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
}
