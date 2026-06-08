package sansan.sentix.Module.User.Entity;

import lombok.Data;
import sansan.sentix.Entity.ConvertStatus.UserStatusConverter;
import sansan.sentix.common.Utils.UserStatus;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ST_USERS")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "st_users_seq")
    @SequenceGenerator(name = "st_users_seq", sequenceName = "seq_st_users", allocationSize = 1)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status")
    @Convert(converter = UserStatusConverter.class)
    private UserStatus status;
}