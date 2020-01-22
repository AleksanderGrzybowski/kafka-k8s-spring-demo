package pl.kelog.producer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.stream.IntStream;

import static pl.kelog.producer.KafkaConfig.TASK_SEPARATOR;

@Service
@Profile("worker")
public class WorkerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public WorkerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = KafkaConfig.TASKS_TOPIC, groupId = KafkaConfig.GROUP_ID)
    public void listen(ConsumerRecord<String, String> message) {
        String[] parts = message.value().split(TASK_SEPARATOR);
        String batchId = parts[0];
        int number = Integer.parseInt(parts[1]);

        BigInteger result = factorial(number);

        kafkaTemplate.send(KafkaConfig.RESULTS_TOPIC, batchId + TASK_SEPARATOR + result.toString());
    }

    private BigInteger factorial(int number) {
        return IntStream.range(1, number).mapToObj(BigInteger::valueOf).reduce(BigInteger.ONE, BigInteger::multiply);
    }
}
