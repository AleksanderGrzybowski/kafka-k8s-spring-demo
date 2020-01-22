package pl.kelog.producer;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Profile("producer")
public class ProducerTrigger {

    private static final String TRIGGER_FILE = "/tmp/trigger";

    private final ProducerService producerService;

    public ProducerTrigger(ProducerService producerService) {
        this.producerService = producerService;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Scheduled(fixedRate = 1000)
    public void trigger() {
        File file = new File(TRIGGER_FILE);
        if (file.exists()) {
            file.delete();
            producerService.sendMessages();
        }
    }
}
