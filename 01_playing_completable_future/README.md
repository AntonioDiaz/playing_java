<h1>Examples: Completable Future</h1>

<!-- TOC -->
  * [Links](#links)
  * [Code](#code)
<!-- TOC -->

## Links
https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html
https://www.baeldung.com/java-completablefuture

## Code
* get(): waits for the completableFuture is complete.
```java
CompletableFuture<String> completableFuture = new CompletableFuture<>();
Executors.newCachedThreadPool().submit(() -> {
  Thread.sleep(500);
  completableFuture.complete("returned_value");
  return null;
});
String s = completableFuture.get();
```

* supplyAsync(): static method 
```java
CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
  try {
    Thread.sleep(5_000);
    return "1234";
  } catch (InterruptedException e) {
    throw new RuntimeException(e);
  }
});
assertEquals("1234", completableFuture.get());
```

* Process results: __thenApply__
```java
@Test
void test_thenApply() throws Exception {
    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "1234");
    CompletableFuture<String> completableFutureSecond = completableFuture.thenApply((s) -> "*" + s);
    assertEquals("*1234", completableFutureSecond.get());
}
```

* Process results: __thenCompose__
```java
CompletableFuture<String> future = CompletableFuture
        .supplyAsync(() -> "1234")
        .thenCompose(s-> CompletableFuture.supplyAsync(()-> "*" + s));
assertEquals("*1234", future.get());
```

* Process results: __thenCombine__
````java
CompletableFuture<String> future = CompletableFuture
        .supplyAsync(() -> "1234")
        .thenCombine(
                CompletableFuture.supplyAsync(() -> "*"),
                (returnedByFirst, returnedBySecond) -> returnedBySecond + returnedByFirst
        );
assertEquals("*1234", future.get());
````

* __allOf__: Run Futures in Parallel
```java
CompletableFuture<Void> future1 = CompletableFuture.runAsync(()->TestCompletableFuture.count++);
CompletableFuture<Void> future2 = CompletableFuture.runAsync(()->TestCompletableFuture.count++);
CompletableFuture<Void> future3 = CompletableFuture.runAsync(()->TestCompletableFuture.count++);
CompletableFuture.allOf(future1, future2, future3).get();
assertTrue(future1.isDone());
assertTrue(future2.isDone());
assertTrue(future3.isDone());
```

* __joining__: Run Futures in Parallel
```java
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Beautiful");
CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "World");
String combined = Stream.of(future1, future2, future3)
        .map(CompletableFuture::join)
        .collect(Collectors.joining(" "));
assertEquals("Hello Beautiful World", combined);
```
