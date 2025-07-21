package de.duckulus.synsniff.core;

import java.util.Map;

public enum FingerprintField {
  IP_TOTAL_LEN(Integer.class, 5),
  IP_ID(Integer.class, 3),
  IP_TTL(Integer.class, 2),

  TCP_OPTIONS(String.class, 5),
  TCP_OFFSET(Integer.class, 5),
  TCP_WINDOW_SCALING(Integer.class, 3),
  TCP_WINDOW_SIZE(Integer.class, 2),
  TCP_TIMESTAMP(Integer.class, 2),
  TCP_TIMESTAMP_ECHO_REPLY(Integer.class, 2),
  TCP_MSS(Integer.class, 1);


  private final Class<?> type;
  private final double weight;

  FingerprintField(Class<?> type, double weight) {
    this.type = type;
    this.weight = weight;
  }

  public double getWeight() {
    return weight;
  }

  @SuppressWarnings("unchecked")
  public <T> T get(Map<FingerprintField, Object> map) {
    if (!map.containsKey(this)) {
      return null;
    }
    return (T) type.cast(map.get(this));
  }
}
