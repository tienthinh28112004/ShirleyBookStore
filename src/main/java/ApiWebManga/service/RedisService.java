package ApiWebManga.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    void save(String key,String value);//lưu cặp key-value vào redis
    void setTimeToLive(String key, long timeoutInDays);//lưu thời gian cho key tồn tại
    void save(String key, String value, long duration, TimeUnit timeUnit);
    String get(String key);
    void delete(String key);
    //việc có thêm trường field thường dành cho kiến thwusc nâng cao liên quan đến việc lấy nheieuf sản phẩm trong giỏ hàng(ở đây chưa học)
    //void delete(String key,String field);
    //void delete(String key,List<String> fields);
    //void hashSet(String key,String field,Object value);//redis sẽ lưu 3 trường key,field,value
    //boolean hashExists(String key,String field);//kiểm tra xem reddis có tồn tại key hay field
    //Map<String,Object> getField(String key);
    //Object hashGet(String key,String field);
    //List<Object> hashGetByFieldPrefixes(String key,String filedPrefix);
    //Set<String> getFieldPrefixes(String key);
}
