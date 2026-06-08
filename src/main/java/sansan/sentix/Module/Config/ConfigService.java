package sansan.sentix.Module.Config;


public interface ConfigService {
    String findByConfigKey(String key);

    void loadMemoryConfig();
}
