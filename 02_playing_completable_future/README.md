<h1>Examples: Completable Future</h1>

<!-- TOC -->
  * [Links](#links)
  * [Code](#code)
    * [Wait for the completableFuture is complete: get](#wait-for-the-completablefuture-is-complete--get)
    * [static method: supplyAsync](#static-method--supplyasync)
    * [Process results: __thenApply__](#process-results--thenapply)
    * [Process results: __thenCompose__](#process-results--thencompose)
    * [Process results: __thenCombine__](#process-results--thencombine)
    * [Run Futures in Parallel: __allOf__](#run-futures-in-parallel--allof)
    * [Run Futures in Parallel: __joining__](#run-futures-in-parallel--joining)
    * [Error handling: __handle__](#error-handling--handle)
    * [Error handling: __completeExceptionally__](#error-handling--completeexceptionally)
<!-- TOC -->

## Links
https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html
https://www.baeldung.com/java-completablefuture

## Code
### Wait for the completableFuture is complete: get
```java
CompletableFuture<String> future = new CompletableFuture<>();
Executors.newCachedThreadPool().submit(() -> {
  Thread.sleep(500);
  future.complete("returned_value");
  return null;
});
String s = future.get();
```

### static method: supplyAsync 
```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
  try {
    Thread.sleep(5_000);
    return "1234";
  } catch (InterruptedException e) {
    throw new RuntimeException(e);
  }
});
assertEquals("1234", future.get());
```

### Process results: __thenApply__
```java
@Test
void test_thenApply() throws Exception {
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "1234");
    CompletableFuture<String> futureSecond = future.thenApply((s) -> "*" + s);
    assertEquals("*1234", futureSecond.get());
}
```

### Process results: __thenCompose__
```java
CompletableFuture<String> future = CompletableFuture
        .supplyAsync(() -> "1234")
        .thenCompose(s-> CompletableFuture.supplyAsync(()-> "*" + s));
assertEquals("*1234", future.get());
```

### Process results: __thenCombine__
````java
CompletableFuture<String> future = CompletableFuture
        .supplyAsync(() -> "1234")
        .thenCombine(
                CompletableFuture.supplyAsync(() -> "*"),
                (returnedByFirst, returnedBySecond) -> returnedBySecond + returnedByFirst
        );
assertEquals("*1234", future.get());
````

### Run Futures in Parallel: __allOf__
```java
CompletableFuture<Void> future1 = CompletableFuture.runAsync(()->TestCompletableFuture.count++);
CompletableFuture<Void> future2 = CompletableFuture.runAsync(()->TestCompletableFuture.count++);
CompletableFuture<Void> future3 = CompletableFuture.runAsync(()->TestCompletableFuture.count++);
CompletableFuture.allOf(future1, future2, future3).get();
assertTrue(future1.isDone());
assertTrue(future2.isDone());
assertTrue(future3.isDone());
```

### Run Futures in Parallel: __joining__
```java
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Beautiful");
CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "World");
String combined = Stream.of(future1, future2, future3)
        .map(CompletableFuture::join)
        .collect(Collectors.joining(" "));
assertEquals("Hello Beautiful World", combined);
```

### Error handling: __handle__
```java
String name = null;
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
  if (name == null) {
    throw new RuntimeException("Computation error!");
  }
  return "Hello, " + name;
}).handle((s, t) -> {
  return s != null ? s : "Hello, Stranger!";
});
assertEquals("Hello, Stranger!", future.get());
```

### Error handling: __completeExceptionally__
```java
String name = null;
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
  name.length();
  return "Hello, " + name;
});
future.completeExceptionally(new RuntimeException("Calculation failed!"));
ExecutionException executionException = assertThrows(ExecutionException.class, future::get);
assertEquals("Calculation failed!", executionException.getCause().getMessage());
```