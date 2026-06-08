package sansan.sentix.common.Utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class DateTimeUtils {
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    public static LocalDateTime nowLocalDateTime() {
        Instant instant = Instant.now();
        return LocalDateTime.ofInstant(instant, DEFAULT_ZONE);
    }


    /**
     * Lấy ZonedDateTime hiện tại theo múi giờ +7
     */
    public static ZonedDateTime nowZonedDateTime() {
        return Instant.now().atZone(DEFAULT_ZONE);
    }

    /**
     * Lấy Instant hiện tại (UTC)
     */
    public static Instant nowInstant() {
        return Instant.now().atZone(DEFAULT_ZONE).toInstant();
    }

    /**
     * Lấy java.util.Date hiện tại theo múi giờ +7
     */
    public static Date nowDate() {
        return Date.from(Instant.now().atZone(DEFAULT_ZONE).toInstant());
    }

    /**
     * Lấy Epoch milli hiện tại (UTC)
     */
    public static long nowEpochMilli() {
        return nowInstant().toEpochMilli();
    }

    /**
     * Lấy Epoch second hiện tại (UTC)
     */
    public static long nowEpochSecond() {
        return Instant.now().getEpochSecond();
    }

    /**
     * Chuyển LocalDateTime sang Instant theo múi giờ +7
     */
    public static Instant toInstant(LocalDateTime ldt) {
        return ldt.atZone(DEFAULT_ZONE).toInstant();
    }

    /**
     * Chuyển Instant sang LocalDateTime theo múi giờ +7
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, DEFAULT_ZONE);
    }

    /**
     * Chuyển Instant sang ZonedDateTime theo múi giờ +7
     */
    public static ZonedDateTime toZonedDateTime(Instant instant) {
        return instant.atZone(DEFAULT_ZONE);
    }

    /**
     * Chuyển LocalDateTime sang Date theo múi giờ +7
     */
    public static Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(DEFAULT_ZONE).toInstant());
    }

    /**
     * Chuyển Instant sang Date
     */
    public static Date toDate(Instant instant) {
        return Date.from(instant);
    }

    //Hàm random thời gian theo khung giờ (CHUẨN)
    public static LocalDateTime randomTimeBetweenHours(int fromHour, int toHour) {
        if (fromHour < 0 || toHour > 23 || fromHour > toHour) {
            throw new IllegalArgumentException("Invalid hour range");
        }

        LocalDate today = nowLocalDateTime().toLocalDate();

        int hour = ThreadLocalRandom.current().nextInt(fromHour, toHour + 1);
        int minute = ThreadLocalRandom.current().nextInt(0, 60);

        return LocalDateTime.of(today, LocalTime.of(hour, minute));
    }

    public static LocalDateTime randomTimeBetweenHours(LocalDate date, int fromHour, int toHour) {
        if (fromHour < 0 || toHour > 23 || fromHour > toHour) {
            throw new IllegalArgumentException("Invalid hour range");
        }

        int hour = ThreadLocalRandom.current().nextInt(fromHour, toHour + 1);
        int minute = ThreadLocalRandom.current().nextInt(0, 60);

        return LocalDateTime.of(date, LocalTime.of(hour, minute));
    }

}
