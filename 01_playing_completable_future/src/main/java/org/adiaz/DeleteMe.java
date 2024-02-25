package org.adiaz;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@ToString
@Slf4j
public class DeleteMe {

  public String tomatome() {
    log.info("ole ole");
    return "ole ole;";
  }

  private String name;
}
