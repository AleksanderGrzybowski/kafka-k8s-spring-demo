package pl.kelog.producer;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static pl.kelog.producer.KafkaConfig.TASK_SEPARATOR;

@Service
@Profile("producer")
public class ProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Map<String, Stats> finishedCounts = new HashMap<>();

    private static final int ITEM_COUNT = 100;
    private static final int MAX_ITEM_VALUE = 10000;

    public ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    void sendMessages() {
        String batchId = createBatchId();
        List<Integer> newBatch = createBatch();
        finishedCounts.put(batchId, new Stats(System.currentTimeMillis()));

        System.out.println("Submitting [" + batchId + "] - " + newBatch.size() + " elements...");
        newBatch.forEach(task -> kafkaTemplate.send(KafkaConfig.TASKS_TOPIC, batchId + TASK_SEPARATOR + task));
    }

    @KafkaListener(topics = KafkaConfig.RESULTS_TOPIC, groupId = KafkaConfig.GROUP_ID)
    public void acceptResults(String message) {
        String[] parts = message.split(TASK_SEPARATOR);
        String batchId = parts[0];

        Stats stats = finishedCounts.get(batchId);
        stats.finishedCount += 1;
        System.out.println("[" + stats.finishedCount + "/" + ITEM_COUNT + "] Got result from batch " + batchId);
        if (stats.finishedCount >= ITEM_COUNT) {
            System.out.println("Batch " + batchId + " finished in " + (System.currentTimeMillis() - stats.startTimeMillis) + " ms.");
        }
    }

    private static String createBatchId() {
        return UUID.randomUUID().toString().substring(0, 5);
    }

    private static List<Integer> createBatch() {
        return new Random().ints().filter(i -> i > 1).limit(ITEM_COUNT).map(i -> i % MAX_ITEM_VALUE).boxed().collect(toList());
    }

    private static class Stats {
        Stats(long start) {
            this.startTimeMillis = start;
        }

        int finishedCount = 0;
        long startTimeMillis;
    }
}
