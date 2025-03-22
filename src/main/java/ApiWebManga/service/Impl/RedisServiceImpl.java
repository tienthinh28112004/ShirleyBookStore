package ApiWebManga.service.Impl;

import ApiWebManga.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String,String> redisTemplate;//thao tác với nhwunxg thằng lưu 2 trường(thường dùng)
    //private final HashOperations<String,String,Object> hashOperations;//thao tác với nhwunxg thằng lưu 3 trường dùng khi nhóm dữ liệu như thông tin người dùng,giỏ hàng,cài đặt,..
    @Override
    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setTimeToLive(String key, long timeoutInDays) {
        redisTemplate.expire(key,timeoutInDays, TimeUnit.DAYS);
    }

    @Override
    public void save(String key, String value, long duration, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key,value,duration,timeUnit);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    //opsForValue():Lưu và lấy giá trị đơn giản (String)
    //opsForHash(): Làm việc với kiểu Hash (tương tự HashMap)
    //opsForList(): Thao tác với danh sách (List)
    //opsForSet() : Thao tác với tập hợp không trùng lặp (Set)
    //opsForStream() :Làm việc với Redis Streams (dùng cho message queue)


//    @Override
//    public void hashSet(String key, String field, Object value) {
//        hashOperations.put(key, field, value);
//    }

//    @Override
//    public boolean hashExists(String key, String field) {
//        return hashOperations.hasKey(key,field);
//    }

//    @Override
//    public Map<String, Object> getField(String key) {
//        return hashOperations.entries(key);
//    }

//    @Override
//    public Object hashGet(String key, String field) {
//        return hashOperations.get(key,field);
//    }

//    @Override
//    public List<Object> hashGetByFieldPrefixes(String key,String filedPrefix) {
//        List<Object> objects = new ArrayList<>();
//
//        Map<String,Object> hashEntries = hashOperations.entries(key);
//        for(Map.Entry<String,Object> entry : hashEntries.entrySet()){
//            if(entry.getKey().startsWith(filedPrefix)){
//                objects.add(entry.getValue());
//            }
//        }
//        return objects;
//    }
//
//    @Override
//    public Set<String> getFieldPrefixes(String key) {
//        return hashOperations.entries(key).keySet();
//    }

//    @Override
//    public void delete(String key, String field) {
//        hashOperations.delete(key,field);
//    }
//
//    @Override
//    public void delete(String key, List<String> fields) {
//        for(String field: fields){
//            hashOperations.delete(key,field);
//        }
//    }
}
