package sansan.sentix.Config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SystemConfig {
    public static Map<String, String> TELEGRAM = new HashMap<>();
    public static Map<String, String> AUTH = new HashMap<>();
    public static Map<String, String> SYSTEM = new HashMap<>();
}
