package com.github.jorgecastilloprz.corleone;

import java.util.Collections;
import javax.annotation.processing.Processor;

final class TestProcessors {
  static Iterable<? extends Processor> corleoneProcessors() {
    return Collections.singletonList(new CorleoneProcessor());
  }
}
