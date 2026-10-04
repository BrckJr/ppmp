package io.github.brckjr.ppmp.domain.service.auth;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** In-memory brute-force protection: blocks an identifier after too many recent failures. */
@ApplicationScoped
public class LoginAttemptLimiter {

  static final int MAX_FAILURES = 5;
  static final Duration WINDOW = Duration.ofMinutes(15);

  private final ConcurrentMap<String, Deque<Instant>> failures = new ConcurrentHashMap<>();

  public boolean isBlocked(String key) {
    Deque<Instant> attempts = failures.get(key);
    if (attempts == null) {
      return false;
    }
    synchronized (attempts) {
      prune(attempts);
      return attempts.size() >= MAX_FAILURES;
    }
  }

  public void recordFailure(String key) {
    Deque<Instant> attempts = failures.computeIfAbsent(key, ignored -> new ArrayDeque<>());
    synchronized (attempts) {
      prune(attempts);
      attempts.addLast(Instant.now());
    }
  }

  public void reset(String key) {
    failures.remove(key);
  }

  private static void prune(Deque<Instant> attempts) {
    Instant cutoff = Instant.now().minus(WINDOW);
    while (!attempts.isEmpty() && attempts.peekFirst().isBefore(cutoff)) {
      attempts.removeFirst();
    }
  }
}
