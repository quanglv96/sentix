package sansan.sentix.Common.Config;
import org.springframework.stereotype.Component;
import sansan.sentix.Module.Config.ConfigService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class ConfigLoader {
    @Resource
    private ConfigService configService;

    @PostConstruct
    public void init() {
        System.out.println("run - ConfigLoader");
        configService.loadMemoryConfig();
    }
}