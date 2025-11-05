package Ratelimit1;
import java.util.HashMap;

/**
 * Interface for Rate Limiter.
 */
public interface RateLimit {
    boolean checkRate(String id);
}

/**
 * Data structure to store per-user token bucket info.
 */
class TokenBucket {
    double tokens;          // current tokens available
    long lastRefillTime;    // last time tokens were refilled (ms)

    TokenBucket(double tokens, long lastRefillTime) {
        this.tokens = tokens;
        this.lastRefillTime = lastRefillTime;
    }
}

/**
 * Token Bucket implementation of Rate Limiter.
 */
class TokenBucketRateLimiter implements RateLimit {
    private final double capacity = 5.0;          // max number of tokens
    private final double refillRatePerSec = 2.0;  // tokens added per second
    private final HashMap<String, TokenBucket> buckets = new HashMap<>();

    /**
     * Check if a request from user `id` is allowed.
     */
    @Override
    public boolean checkRate(String id) {
        long now = System.currentTimeMillis();

        // Get or create a token bucket for the user
        TokenBucket bucket = buckets.get(id);
        if (bucket == null) {
            bucket = new TokenBucket(capacity, now);
            buckets.put(id, bucket);
        }

        // --- Refill logic ---
        double elapsedSeconds = (now - bucket.lastRefillTime) / 1000.0;
        double tokensToAdd = elapsedSeconds * refillRatePerSec;

        bucket.tokens = Math.min(capacity, bucket.tokens + tokensToAdd);
        bucket.lastRefillTime = now;

        // --- Consume a token ---
        if (bucket.tokens >= 1) {
            bucket.tokens -= 1;
            System.out.printf("✅ Allowed for %s | Tokens left: %.2f%n", id, bucket.tokens);
            return true;
        } else {
            System.out.printf("❌ Rate limit exceeded for %s | Tokens left: %.2f%n", id, bucket.tokens);
            return false;
        }
    }
}

/**
 * Main class to test Token Bucket Rate Limiter.
 */
class Main {
    public static void main(String[] args) throws InterruptedException {
        RateLimit rateLimiter = new TokenBucketRateLimiter();
        String user = "customer123";

        // Simulate continuous requests
        for (int i = 0; i < 15; i++) {
            rateLimiter.checkRate(user);
            Thread.sleep(300); // each request every 300ms
        }
    }
}
