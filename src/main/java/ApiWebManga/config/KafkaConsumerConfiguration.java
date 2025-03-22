package ApiWebManga.config;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfiguration {
    //nhận dữ liệu từ kafka topic,đọc dữ liệu từ 1 hoặc nhiều partitions của topic,
    // có thể thuộc 1 nhóm để xử lý dữ liệu song song
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServices;//nơi kết nối giữa producer và consumer

    @Bean
    public ConsumerFactory<String,Object> consumerFactory(){
        Map<String,Object> result = new HashMap<>();
        result.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServices);//nới mà producer và consumer giao tiếp với nhau
        result.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);//dùng để chuyển key nhận được từ dạng nhị phân sang dang String(nhận được từ broker (Nơi lưu trữ là ổ cứng máy tính)
        result.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);//dùng để chuyển value nhận được từ nhị phân sang dang Json
        result.put(JsonDeserializer.TRUSTED_PACKAGES,"*");//đánh dấu các package đều được phép deserialize dữ liệu Json thành Java Object
        result.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);//Tắt auto-commit offset, giúp Consumer kiểm soát khi nào đánh dấu dữ liệu đã đọc (Giảm rủi ro mất dữ liệu nếu có lỗi).
        return new DefaultKafkaConsumerFactory<>(result);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,Object> kafkaListenerContainerFactory(){//dùng để khởi tạo các Kafka Listener(Consumer lắng nghe sự ện từ kafka)
        ConcurrentKafkaListenerContainerFactory<String,Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        //giúp nhiều consumer hoạt động song song(multi-threading) để tặng hiểu suất xử lí từ kafka
        factory.setConsumerFactory(consumerFactory());//định nghĩa cách Consumer kết nối với kafka và deserialize dữ liệu
        factory.setConcurrency(3);//(xư lý dữ liệu song song khi nhận từ kafka) số lượng tối đa bằng số lương partion của topic
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);//consummer chỉ xác nhận thành công khi gọi ack.acknowledge()
        //ở bên trên đã câu hình là n không tự đông commit nên ở đây ta cấu hiình MANUAL để khi người dùng xác nhận thì nó mới xác nhân với kafka là xử lí thành công
        //mặc định sẽ là RECORD(tự động commit) còn không chúng ta có thể sửa đổi sang manulal,count,time,batch,manual_immediate
        //có thể thêm retry ở đây nhưng sẽ dùng sau
        return factory;
    }
}
