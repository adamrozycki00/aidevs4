package com.tenetmind.aidevs.domain.model.task02.tools;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import static com.tenetmind.aidevs.domain.model.task02.tools.CalculateDistanceTool.calculateDistanceTool;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

class CalculateDistanceToolTest {

  Pair<Double, Double> paris = Pair.of(48.8534, 2.3488);
  Pair<Double, Double> warsaw = Pair.of(52.2298, 21.0118);

  @Test
  void shouldCalculateDistanceTool() {
    // when
    double resultMeters = calculateDistanceTool(paris.getLeft(), paris.getRight(), warsaw.getLeft(), warsaw.getRight());

    // then calculated distance between Paris and Warsaw is close to 1367 km
    assertThat(resultMeters / 1000).isCloseTo(1367, offset(1.0));
  }
}
