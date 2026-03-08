package cup.website.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Redemption {
	
	private static final AtomicLong idGenerator = new AtomicLong(0);

    private Long id;
    private String userId;
    private String username;
    private String type;     // "MEDIA"
    private String content;  // URL
    private long duration;   // Seconds
    private int cost;
    private boolean isPlayed = false;
    private LocalDateTime createdAt;

    public Redemption() {
        this.id = idGenerator.incrementAndGet(); // Auto-assign ID
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
    public boolean isPlayed() { return isPlayed; }
    public void setPlayed(boolean played) { isPlayed = played; }
}
