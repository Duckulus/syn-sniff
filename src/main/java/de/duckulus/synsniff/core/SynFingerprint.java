package de.duckulus.synsniff.core;

import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.TcpOptionKind;

import java.util.EnumMap;
import java.util.Objects;
import java.util.stream.Collectors;

public record SynFingerprint(EnumMap<FingerprintField, Object> data) {

  public static SynFingerprint fromHeaders(IpV4Packet.IpV4Header iph, TcpPacket.TcpHeader tcph) {
    EnumMap<FingerprintField, Object> data = new EnumMap<>(FingerprintField.class);

    String options = tcph.getOptions().stream().map(opt -> opt.getKind().name()).collect(Collectors.joining("|"));

    data.put(FingerprintField.IP_TOTAL_LEN, iph.getTotalLengthAsInt());
    data.put(FingerprintField.IP_ID, iph.getIdentificationAsInt());
    data.put(FingerprintField.IP_TTL, iph.getTtlAsInt());

    data.put(FingerprintField.TCP_OPTIONS, options);
    data.put(FingerprintField.TCP_OFFSET, tcph.getDataOffsetAsInt());
    tcph.getOptions().stream().filter(opt -> opt.getKind() == TcpOptionKind.WINDOW_SCALE).findFirst()
            .ifPresent(opt -> data.put(FingerprintField.TCP_WINDOW_SCALING, ((TcpWindowScaleOption) opt).getShiftCountAsInt()));
    data.put(FingerprintField.TCP_WINDOW_SIZE, tcph.getWindowAsInt());
    tcph.getOptions().stream().filter(opt -> opt.getKind() == TcpOptionKind.TIMESTAMPS).findFirst().ifPresent(opt -> {
      data.put(FingerprintField.TCP_TIMESTAMP, ((TcpTimestampsOption) opt).getTsValue());
      data.put(FingerprintField.TCP_TIMESTAMP_ECHO_REPLY, ((TcpTimestampsOption) opt).getTsEchoReply());
    });
    tcph.getOptions().stream().filter(opt -> opt.getKind() == TcpOptionKind.MAXIMUM_SEGMENT_SIZE).findFirst()
            .ifPresent(opt -> data.put(FingerprintField.TCP_MSS, ((TcpMaximumSegmentSizeOption) opt).getMaxSegSizeAsInt()));

    return new SynFingerprint(data);
  }

  public double matchScore(SynFingerprint other) {
    if (other == null) return 0.0;

    double totalWeight = 0.0;
    double matchingWeight = 0.0;

    for (FingerprintField field : FingerprintField.values()) {
      double weight = field.getWeight();

      Object thisValue = this.data().get(field);
      Object otherValue = other.data().get(field);

      totalWeight += weight;

      if (thisValue == null && otherValue == null) {
        matchingWeight += weight;
        continue;
      }

      if (thisValue == null || otherValue == null) {
        continue;
      }

      if (Objects.equals(thisValue, otherValue)) {
        matchingWeight += weight;
      }
    }

    return totalWeight == 0.0 ? 1.0 : matchingWeight / totalWeight;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final EnumMap<FingerprintField, Object> data = new EnumMap<>(FingerprintField.class);

    public Builder ipTotalLen(int totalLen) {
      data.put(FingerprintField.IP_TOTAL_LEN, totalLen);
      return this;
    }

    public Builder ipId(int id) {
      data.put(FingerprintField.IP_ID, id);
      return this;
    }

    public Builder ipTtl(int ttl) {
      data.put(FingerprintField.IP_TTL, ttl);
      return this;
    }

    public Builder tcpOptions(String options) {
      data.put(FingerprintField.TCP_OPTIONS, options);
      return this;
    }

    public Builder tcpOffset(int offset) {
      data.put(FingerprintField.TCP_OFFSET, offset);
      return this;
    }

    public Builder tcpWindowScaling(int shiftCount) {
      data.put(FingerprintField.TCP_WINDOW_SCALING, shiftCount);
      return this;
    }

    public Builder tcpWindowSize(int windowSize) {
      data.put(FingerprintField.TCP_WINDOW_SIZE, windowSize);
      return this;
    }

    public Builder tcpTimestamp(int timestamp) {
      data.put(FingerprintField.TCP_TIMESTAMP, timestamp);
      return this;
    }

    public Builder tcpTimestampEchoReply(long echoReply) {
      data.put(FingerprintField.TCP_TIMESTAMP_ECHO_REPLY, echoReply);
      return this;
    }

    public Builder tcpMss(int mss) {
      data.put(FingerprintField.TCP_MSS, mss);
      return this;
    }

    public SynFingerprint build() {
      return new SynFingerprint(new EnumMap<>(data));
    }

  }

}
