package ApiWebManga.config;

import com.fasterxml.jackson.databind.JsonSerializable;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {
    //file này đóng vai trò là thành phần chịu trách nghiệm gửi dữ liệu(message) vào kafka Topic
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServer;//chứa địa chỉ của boostrap-servers(9094) cổng kết nối giữa producer và consumer

    @Bean//do kafka chỉ chấp nhận dữ liệu dạng nhị phân nên cần 2 dòng 3,4 để chuyển dữ liệu sang dạng phù hợp sau đó chuyển sang dạng nhị phân
    public DefaultKafkaProducerFactory<String,Object> producerFactory(){
        Map<String,Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrapServer);//nơi lưu trữu địa chỉ của Kafka broker(1 nơi lưu trữ trung gian trong qua partition và short)
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);//nếu có key thì kafka sẽ dùng mảng băm để quyết định message sẽ ược gửi vào partition nào(nếu không thì sẽ chọn partition ngẫu nhiên)
        //dữ liệu truyền vào là chuỗi String,kafka dùng StringSerializer để chuyển key sang String ,kafka tiếp tục mã hóa dữ liệu sang dạng nhị phân và lưu vào Kafka broker
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializable.class);//xác định dữ liệu là json
        //Dữ liệu value thường là object,nhung kafka chỉ nhận dữ liệu dạng nhị phân nên cần JsonSerializer chuyển object sang dạng Json rồi tiếp tục mã hóa sang dạng nhị phân để gửi lên
        return new DefaultKafkaProducerFactory<>(config);//tạo ra producer
    }

    @Bean//Tạo một KafkaTemplate để gửi dữ liệu vào Kafka.
    public KafkaTemplate<String,Object> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
        //tạo ra 1 KafkaTemplate bằng việc sử dụng producerFactory() ở bên trên,cần truyền vào 1 topic và 1 message
    }

    @Bean
    public NewTopic saveToElasticSearch() {
        return new NewTopic("save-to-elastic-search", 3, (short) 1);//partion giúp chia nh dữ liệu gửi lên từ đó tăng tốc độ xử lý,giúp xử lí song song
        //lưu 3 partion là phù hợp vì nó có thể xử lí 3 message 1 lúc(không nhiều,cũng không ít)
        //việc không lưu bản sao là vì chúng ta chỉ có 1 kafka broker thôi nên việc lưu nhiều là vô nghĩa
    }

//    Producer kết nối với Kafka Broker thông qua bootstrap-servers.
//    Producer chọn topic để gửi dữ liệu.
//    Kafka chọn partition (dựa vào key hoặc ngẫu nhiên).
//    Message được lưu vào partition với một offset.
}
