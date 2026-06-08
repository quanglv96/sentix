package sansan.sentix.common.Config;
import org.springframework.stereotype.Component;
import sansan.sentix.Service.ConfigService;

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