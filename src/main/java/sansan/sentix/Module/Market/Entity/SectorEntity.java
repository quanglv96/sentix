package sansan.sentix.Module.Market.Entity;

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

@Entity
@Table(name = "ST_SECTORS")
@Data
public class SectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "st_sectors_seq")
    @SequenceGenerator(name = "st_sectors_seq", sequenceName = "seq_st_sectors", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Lob
    @Column(name = "industry_code")
    private String industryCode; // Chuỗi varcar : HPG;VHM

    @Column(name = "sentiment_index_7d")
    private BigDecimal sentimentIndex7d;
}