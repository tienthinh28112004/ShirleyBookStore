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
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServer;

    @Bean
    public DefaultKafkaProducerFactory<String,Object> producerFactory(){
        Map<String,Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrapServer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializable.class);
        return new DefaultKafkaProducerFactory<>(config);
    }
    @Bean
    public KafkaTemplate<String,Object> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
    @Bean
    public NewTopic saveToElasticSearch() {
        return new NewTopic("save-to-elastic-search", 3, (short) 1);
    }

//    Producer kết nối với Kafka Broker thông qua bootstrap-servers.
//    Producer chọn topic để gửi dữ liệu.
//    Kafka chọn partition (dựa vào key hoặc ngẫu nhiên).
//    Message được lưu vào partition với một offset.
}
