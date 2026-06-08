package sansan.sentix.Module.Config.Entity;


import lombok.Data;
import sansan.sentix.Module.Config.Converter.ConfigStatusConverter;
import sansan.sentix.Module.Config.Utils.ConfigStatus;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ST_CONFIG")
@Data
public class Config {
    @Id
    @Column(name = "CONFIG_KEY")
    private String configKey;

    @Lob // 👉 Bắt buộc phải có để map với kiểu dữ liệu CLOB trong Oracle
    @Column(name = "CONFIG_VALUE")
    private String configValue;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "STATUS")
    @Convert(converter = ConfigStatusConverter.class)
    private ConfigStatus status;

}
