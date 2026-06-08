package sansan.sentix.Common.Cache;

import sansan.sentix.Module.Config.Entity.Config;

import java.util.List;

public final class GlobalCache {
    private static CacheService cacheService;

    private static CacheService getService() {
        if (cacheService == null) {
            cacheService = SpringContextHolder.getBean(CacheService.class);
        }
        return cacheService;
    }

    public static String getConfig(String key) {
        return getService().getConfig(key);
    }

    public static void putAll(List<Config> configs) {
        CacheService service = getService();

        configs.forEach(service::putConfig);
    }
}
