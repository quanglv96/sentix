package sansan.sentix.common.Utils;

import lombok.Getter;

@Getter
public enum AppStatus {
    ACTIVE("ACTIVE"),
    ONLINE("ONLINE"),
    OFF("OFF"),
    BLOCK("BLOCK"),
    INACTIVE("INACTIVE"),
    FINISHED("FINISHED"),
    PENDING("PENDING"),
    PENDING_APPROVAL("PENDING_APPROVAL"),
    MANUAL_APPROVAL("MANUAL_APPROVAL"),
    PROCESS("PROCESS"),
    ERROR("ERROR"),
    SUCCESS("SUCCESS"),
    NEW("NEW"),
    OLD("OLD"),
    UNREAD("UNREAD"),
    READ("READ"),
    EXPIRED("EXPIRED"),
    AUTO("AUTO"),
    MANUAL("MANUAL"),
    AUDIO("AUDIO");
    private final String status;

    AppStatus(String status) {
        this.status = status;
    }
}
