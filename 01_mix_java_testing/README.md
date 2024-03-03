<h1>Java Playground</h1>

<!-- TOC -->
  * [Map vs Flatmap](#map-vs-flatmap)
<!-- TOC -->


## Map vs Flatmap
https://www.baeldung.com/java-difference-map-and-flatmap

````java
Optional<String> optional = Optional.of("ole");
Optional<Integer> optionalMapped = optional.map(p -> p.length());
assertEquals(Optional.of(3), optionalMapped);
````
