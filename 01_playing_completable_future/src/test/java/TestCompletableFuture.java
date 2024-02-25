import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestCompletableFuture {

  @Test
  void test_async() throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();
    Executors.newCachedThreadPool().submit(() -> {
      Thread.sleep(500);
      completableFuture.complete("vamos");
      return null;
    });
    String s = completableFuture.get();
    assertEquals("vamos", s);
  }

  @Test
  void testGetSync() throws ExecutionException, InterruptedException {
    CompletableFuture<String> completableFuture = new CompletableFuture<>();
    completableFuture.complete("hello");
    String s = completableFuture.get();
    assertEquals("hello",s);

  }



}