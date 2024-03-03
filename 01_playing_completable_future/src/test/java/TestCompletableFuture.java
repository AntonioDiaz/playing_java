import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TestCompletableFuture {

  /** get(): waits for the completableFuture is complete. */
  @Test
  void test_async() throws ExecutionException, InterruptedException {
    CompletableFuture<String> future = new CompletableFuture<>();
    Executors.newCachedThreadPool().submit(() -> {
      Thread.sleep(500);
      future.complete("returned_value");
      return null;
    });
    assertEquals("returned_value", future.get());
  }

  @Test
  void test_supplyAsync() throws ExecutionException, InterruptedException {
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(5_000);
        return "1234";
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
    log.info("continue");
    assertEquals("1234", future.get());
  }

  @Test
  void test_thenApply() throws Exception {
    CompletableFuture<String> futureFirst = CompletableFuture.supplyAsync(() -> "1234");
    CompletableFuture<String> futureSecond = futureFirst.thenApply(s -> "*" + s);
    assertEquals("1234", futureFirst.get());
    assertEquals("*1234", futureSecond.get());
  }

  @Test
  void test_thenCompose() throws Exception {
    CompletableFuture<String> future = CompletableFuture
            .supplyAsync(() -> "1234")
            .thenCompose(s -> CompletableFuture.supplyAsync(() -> "*" + s));
    assertEquals("*1234", future.get());
  }

  @Test
  void test_thenCombine() throws Exception {
    CompletableFuture<String> future = CompletableFuture
            .supplyAsync(() -> "1234")
            .thenCombine(
                    CompletableFuture.supplyAsync(() -> "*"),
                    (returnedByFirst, returnedBySecond) -> returnedBySecond + returnedByFirst
            );
    assertEquals("*1234", future.get());
  }

  static Integer count = 0;

  @Test
  void test_futuresInParallel_allOf() throws Exception {
    CompletableFuture<Void> future1 = CompletableFuture.runAsync(()->TestCompletableFuture.count++);
    CompletableFuture<Void> future2 = CompletableFuture.runAsync(()->TestCompletableFuture.count++);
    CompletableFuture<Void> future3 = CompletableFuture.runAsync(()->TestCompletableFuture.count++);
    CompletableFuture.allOf(future1, future2, future3).get();
    assertTrue(future1.isDone());
    assertTrue(future2.isDone());
    assertTrue(future3.isDone());
    assertEquals(3, count);
  }

  @Test
  void test_futuresInParallel_join() {
    CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
    CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Beautiful");
    CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "World");
    String combined = Stream.of(future1, future2, future3)
            .map(CompletableFuture::join)
            .collect(Collectors.joining(" "));
    assertEquals("Hello Beautiful World", combined);
  }

  @Test
  void test_error_handle() throws Exception {
    String name = null;
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
      if (name == null) {
        throw new RuntimeException("Computation error!");
      }
      return "Hello, " + name;
    }).handle((s, t) -> {
      log.error(t.getMessage());
      return s != null ? s : "Hello, Stranger!";
    });
    assertEquals("Hello, Stranger!", future.get());
  }

  @Test
  void test_error_completeExceptionally() {
    String name = null;
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
      name.length();
      return "Hello, " + name;
    });
    future.completeExceptionally(new RuntimeException("Calculation failed!"));
    ExecutionException executionException = assertThrows(ExecutionException.class, future::get);
    assertEquals("Calculation failed!", executionException.getCause().getMessage());
  }

}