package org.example.producer_1;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class SimpleProducer {
    private final static Logger logger = LoggerFactory.getLogger(SimpleProducer.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVERS = "localhost:29092";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties configs = new Properties();
        // 필수 설정값 3가지
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 커스텀 파티셔너 설정
        configs.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class);

        KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

        String messageValue = "testMessage3";
        // ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, messageValue);
        // key까지 전송
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME,"key",messageValue);

        // 파티션 번호까지 지정하려면 2번째인자에 번호를 넣으면 된다.
        // ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME,1,"key",messageValue);

        // 즉각 전송을 뜻하는게 아니라, 파라미터로 들어간 record를 프로튜서 내부에 가지고 있다가 배치 형태로 묶어서 브로커에 전송한다.
        // 배치전송이라고 부르고 타 메시지 플랫폼과 차별화된 전송 속도를 가지게 된다.
        // producer.send(record);

        // get()을 통해 프로듀서로 보낸 데이터의 결과를 동기적으로 가져올수 있다
        //RecordMetadata recordMetadata = producer.send(record).get();
        // logger.info("{}", recordMetadata);

        // 동기로 결과를 가져오면 성능에 이슈가 있어서 Callback 클래스를 생성하여 전송결과에 대응하는 로직을 만들수 있다.
        producer.send(record, new ProducerCallback());


        logger.info("{}", record);

        producer.flush();
        producer.close();

    }
}
