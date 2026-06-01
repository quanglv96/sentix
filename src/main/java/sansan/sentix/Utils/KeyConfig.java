package sansan.sentix.Utils;

public interface KeyConfig {
    String SESSION_ID = "session_id";
    String USER_ID = "user_id";
    String USERNAME = "username";
    String AUTHORIZATION = "Authorization";
    String USER_AGENT = "User-Agent";
    String X_FORWARDED_HOST = "X-Forwarded-Host";
    String HOST = "Host";
    String USER_NAME = "USER_NAME";
    Object SECRET_KEY = "SECRET_KEY";

    String RATE_LIMIT_MAX_REQ = "RATE_LIMIT_MAX_REQ";
    String RATE_LIMIT_WINDOW_SECONDS = "RATE_LIMIT_WINDOW_SECONDS";
    String SEARCH = "SEARCH";
    String BLOCK_IP_LIST = "BLOCK_IP_LIST";
    String CONFIG_WEBHOOK = "CONFIG_WEBHOOK";
    String BOT_TOKEN = "BOT_TOKEN";
    String API_TOKEN = "API_TOKEN";
    String URL_WEBHOOK = "URL_WEBHOOK";
    String URL_RECEIVE = "URL_RECEIVE";
}
