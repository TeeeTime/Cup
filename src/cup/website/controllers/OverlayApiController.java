package cup.website.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cup.website.model.Redemption;
import cup.website.service.RedemptionQueueService;

@RestController
@RequestMapping("/api/overlay")
public class OverlayApiController {

	private final RedemptionQueueService queueService;

    public OverlayApiController(RedemptionQueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping("/queue/next")
    public ResponseEntity<?> getNextItem() {
        Optional<Redemption> item = queueService.getNext();
        
        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/queue/complete/{id}")
    public ResponseEntity<?> markComplete(@PathVariable Long id) {
        queueService.markComplete(id);
        return ResponseEntity.ok().build();
    }
}
