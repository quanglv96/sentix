package sansan.sentix.Utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum MarketSession {

    MID_DAY(0), // sau khi đóng phiên sáng),
    END_OF_DAY(1);

    private final int value;

    public static MarketSession fromValue(Integer dbData) {
        for (MarketSession session : values()) {
            if (Objects.equals(session.value, dbData)) {
                return session;
            }
        }
        throw new IllegalArgumentException("[Error][MarketSession] Unknown DB value: " + dbData);
    }
}
