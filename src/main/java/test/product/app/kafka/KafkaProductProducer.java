package test.product.app.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import test.product.app.dto.ProductDto;

@Service
public class KafkaProductProducer {

    @Value("${spring.kafka.product-topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, ProductDto> kafkaTemplate;

    public void sendProduct (ProductDto productDto) {
        kafkaTemplate.send(topic, productDto);
    }
}
