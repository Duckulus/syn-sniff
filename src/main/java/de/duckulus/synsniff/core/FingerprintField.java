package de.duckulus.synsniff.core;

import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.TcpOptionKind;

import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public enum FingerprintField {
  IP_TOTAL_LEN(Integer.class, 5),
  IP_IDENTIFICATION(Integer.class, 3),
  IP_TIME_TO_LIVE(Integer.class, 2),

  TCP_OPTIONS(String.class, 5),
  TCP_OFFSET(Integer.class, 5),
  TCP_WINDOW_SCALING(Integer.class, 3),
  TCP_WINDOW_SIZE(Integer.class, 2),
  TCP_TIMESTAMP(Integer.class, 2),
  TCP_TIMESTAMP_ECHO_REPLY(Integer.class, 2),
  TCP_MAXIMUM_SEGMENT_SIZE(Integer.class, 1);


  private final Class<?> type;
  private final double weight;

  FingerprintField(Class<?> type, double weight) {
    this.type = type;
    this.weight = weight;
  }

  public Optional<Object> extractValue(IpV4Packet.IpV4Header iph, TcpPacket.TcpHeader tcph) {
    return switch (this) {
      case IP_TOTAL_LEN -> Optional.of(iph.getTotalLengthAsInt());
      case IP_IDENTIFICATION -> Optional.of(iph.getIdentificationAsInt());
      case IP_TIME_TO_LIVE -> Optional.of(iph.getTtlAsInt());
      case TCP_OPTIONS ->
              Optional.of(tcph.getOptions().stream().map(opt -> opt.getKind().name()).collect(Collectors.joining("|")));
      case TCP_OFFSET -> Optional.of(tcph.getDataOffsetAsInt());
      case TCP_WINDOW_SCALING ->
              tcph.getOptions().stream().filter(opt -> opt.getKind() == TcpOptionKind.WINDOW_SCALE).findFirst()
                      .map(opt -> ((TcpWindowScaleOption) opt).getShiftCountAsInt());
      case TCP_WINDOW_SIZE -> Optional.of(tcph.getWindowAsInt());
      case TCP_TIMESTAMP ->
              tcph.getOptions().stream().filter(opt -> opt.getKind() == TcpOptionKind.TIMESTAMPS).findFirst()
                      .map(opt -> ((TcpTimestampsOption) opt).getTsValue());
      case TCP_TIMESTAMP_ECHO_REPLY ->
              tcph.getOptions().stream().filter(opt -> opt.getKind() == TcpOptionKind.TIMESTAMPS).findFirst()
                      .map(opt -> ((TcpTimestampsOption) opt).getTsEchoReply());
      case TCP_MAXIMUM_SEGMENT_SIZE ->
              tcph.getOptions().stream().filter(opt -> opt.getKind() == TcpOptionKind.MAXIMUM_SEGMENT_SIZE).findFirst()
                      .map(opt -> ((TcpMaximumSegmentSizeOption) opt).getMaxSegSizeAsInt());
    };
  }

  public double getWeight() {
    return weight;
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<T> get(Map<FingerprintField, Object> map) {
    if (!map.containsKey(this)) {
      return Optional.empty();
    }
    return Optional.of((T) type.cast(map.get(this)));
  }

  public String prettyName() {
    StringJoiner joiner = new StringJoiner(" ");
    String[] words = name().split("_");
    // Since the names always begin with the protocol, the first word should stay capitalized
    joiner.add(words[0]);
    for (int i = 1; i < words.length; i++) {
      joiner.add(Character.toUpperCase(words[i].charAt(0)) + words[i].substring(1).toLowerCase());
    }
    return joiner.toString();
  }
}
