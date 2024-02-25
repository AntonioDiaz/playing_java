package org.adiaz;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class Main {

  public static void main(String[] args) {

    DeleteMe deleteMe = new DeleteMe("que pacha");
    log.info("toma toma {}", deleteMe);
  }
}