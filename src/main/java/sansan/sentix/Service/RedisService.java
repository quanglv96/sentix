package sansan.sentix.Service;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sansan.sentix.Utils.KeyConfig;

import javax.annotation.Resource;

@Service
public class RedisService {

    @Resource
    private RedisTemplate<String, String> redisTemplate; // thay StringRedisTemplate

    // Lưu dữ liệu
    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // Lưu dữ liệu
    public void putSearch(String key, String value) {
        redisTemplate.opsForHash().put(KeyConfig.SEARCH, key, value);
    }

    // Lấy dữ liệu
    public String getKeySearch(String key) {
        return (String) redisTemplate.opsForHash().get(KeyConfig.SEARCH, key);
    }

    // Xoá dữ liệu
    public void deleteSearch() {
        redisTemplate.delete(KeyConfig.SEARCH);
    }

    public Boolean isBlock(String ipCheck) {
        return !ObjectUtils.isEmpty(redisTemplate.opsForHash().get(KeyConfig.BLOCK_IP_LIST, ipCheck));
    }

    public void putBlockIp(String key, String value) {
        redisTemplate.opsForHash().put(KeyConfig.BLOCK_IP_LIST, key, value);
    }
}
