package cup.website.service;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Service;

import cup.website.model.Redemption;

@Service
public class RedemptionQueueService {
	
    private final ConcurrentLinkedQueue<Redemption> queue = new ConcurrentLinkedQueue<>();

    public void add(Redemption redemption) {
        queue.add(redemption);
    }

    public Optional<Redemption> getNext() {
        return queue.stream()
                .filter(r -> !r.isPlayed())
                .findFirst();
    }

    public void markComplete(Long id) {
        Optional<Redemption> item = queue.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();

        if (item.isPresent()) {
            Redemption r = item.get();
            r.setPlayed(true);
            
            queue.remove(r); 
        }
    }
}
