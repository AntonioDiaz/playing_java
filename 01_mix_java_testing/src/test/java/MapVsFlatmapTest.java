import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class MapVsFlatmapTest {


  @Test
  void test_map_with_optional() {
    Optional<String> optional = Optional.of("ole");
    Optional<Integer> optionalMapped = optional.map(p -> p.length());
    assertEquals(Optional.of(3), optionalMapped);
  }

  @Test
  void test_map_with_optional_empty() {
    Optional<String> optional = Optional.empty();
    Optional<Integer> optionalMapped = optional.map(p -> p.length());
    assertEquals(Optional.empty(), optionalMapped);
  }

  @Test
  void test_map_with_optional_of_optional() {
    Optional<Optional<String>> optional = Optional.of(Optional.of("ole"));
    Optional<Optional<Integer>> optionalMapped = optional.map(p -> p.map(p2->p2.length()));
    assertEquals(Optional.of(Optional.of(3)), optionalMapped);
  }

  @Test
  void test_flatmap() {
    Optional<Optional<String>> optionalOfOptional = Optional.of(Optional.of("ole"));
    Optional<String> optional = Optional.of("ole");
    assertEquals(Optional.of(3), optional.flatMap(s -> Optional.of(s.length())));
    assertEquals(Optional.of("ole"), optionalOfOptional.flatMap(s -> s));
    assertEquals(Optional.of("ole"), optionalOfOptional.map(s -> s.get()));
  }




}
