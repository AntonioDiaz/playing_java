import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloTest {

  @Test
  void testGetSync() throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();
    completableFuture.complete("hello");
    String s = completableFuture.get();
    assertEquals("hello",s);

  }

  @Test
  void test_async() throws ExecutionException, InterruptedException {

    CompletableFuture<String> completableFuture = calculateAsync();
    String s = completableFuture.get();
    assertEquals("vamos", s);
  }

  private CompletableFuture<String> calculateAsync() {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();
    Executors.newCachedThreadPool().submit(() -> {
      Thread.sleep(500);
      completableFuture.complete("vamos");
      return null;
    });
    return completableFuture;
  }


}