package sansan.sentix.Entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "config")
@Data
public class Config {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "TYPE")
    private String type; //TELEGRAM, EMAIL, etc. => AppType

    @Column(name = "KEY")
    private String key;

    @Lob
    @Column(name = "VALUE")
    private String value;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    private String status; // "ACTIVE", "INACTIVE" => AppStatus

}
