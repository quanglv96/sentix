package sansan.sentix.Common.Service;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sansan.sentix.Common.Utils.Constants;

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
        redisTemplate.opsForHash().put(Constants.SEARCH, key, value);
    }

    // Lấy dữ liệu
    public String getKeySearch(String key) {
        return (String) redisTemplate.opsForHash().get(Constants.SEARCH, key);
    }

    // Xoá dữ Constants
    public void deleteSearch() {
        redisTemplate.delete(Constants.SEARCH);
    }

    public Boolean isBlock(String ipCheck) {
        return !ObjectUtils.isEmpty(redisTemplate.opsForHash().get(Constants.BLOCK_IP_LIST, ipCheck));
    }

    public void putBlockIp(String key, String value) {
        redisTemplate.opsForHash().put(Constants.BLOCK_IP_LIST, key, value);
    }
}
