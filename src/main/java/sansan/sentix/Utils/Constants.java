package sansan.sentix.Utils;

import java.time.format.DateTimeFormatter;
import java.util.List;

public interface Constants {

    String BASE_DIR = "/opt/app/uploads/crawls";
    String VIDEO_DIR = "/opt/app/uploads/video";

    //TOPIC

    DateTimeFormatter DATE_TIME_FORMATTER_1 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    DateTimeFormatter DATE_TIME_FORMATTER_2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter DATE_TIME_FORMATTER_3 = DateTimeFormatter.ofPattern("dd-MM");
    DateTimeFormatter DATE_TIME_FORMATTER_4 = DateTimeFormatter.ofPattern("ddMMyyyy");
    DateTimeFormatter DATE_TIME_FORMATTER_5 = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter DATE_TIME_FORMATTER_6 = DateTimeFormatter.ofPattern("yyyyMMdd");
    String PROCEDURE_SUCCESS = "00";

    String USER_NAME = "CLIENT_USER";
    String ADMIN_NAME = "ADMIN_USER";
    String PARTNER_USER = "PARTNER_USER";
    String SESSION_ID = "sessionId";
    String USER_AGENT = "User-Agent";
    String LOCALHOST = "localhost:1234";
}
