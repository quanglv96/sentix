package sansan.sentix.common.Utils;

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
    String USER_AGENT = "User-Agent";
    String LOCALHOST = "localhost:1234";
    String SESSION_ID = "session_id";
    String USER_ID = "user_id";
    String USERNAME = "username";
    String AUTHORIZATION = "Authorization";
    String X_FORWARDED_HOST = "X-Forwarded-Host";
    String HOST = "Host";
    //TOPIC
    String TOPIC_ANALYSIS_NEWS = "ANALYSIS-NEWS";

    //CONFIG
    String ARTICLE_BLACKLIST_KEYWORDS = "ARTICLE_BLACKLIST_KEYWORDS";
    String TICKER_LIST = "TICKER_LIST";
    String TELEGRAM_SYNTAX_WEB_HOOK = "TELEGRAM_SYNTAX_WEB_HOOK";
    String TELEGRAM_TOKEN_ADMIN = "TELEGRAM_TOKEN_ADMIN";
    String TELEGRAM_URL_RECEIVE_ADMIN = "TELEGRAM_URL_RECEIVE_ADMIN";
    String BOT_TOKEN = "BOT_TOKEN";
    String URL_WEBHOOK = "URL_WEBHOOK";
    String RATE_LIMIT_MAX_REQ = "RATE_LIMIT_MAX_REQ";
    String RATE_LIMIT_WINDOW_SECONDS = "RATE_LIMIT_WINDOW_SECONDS";
    String BLOCK_IP_LIST = "BLOCK_IP_LIST";
    String SEARCH = "SEARCH";
}
