package io.github.duckulus.synsniff.core;

import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.TcpPacket;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public record SynFingerprint(Map<FingerprintField, Object> data) {

  public static SynFingerprint fromHeaders(IpV4Packet.IpV4Header iph, TcpPacket.TcpHeader tcph) {
    EnumMap<FingerprintField, Object> data = new EnumMap<>(FingerprintField.class);

    for (FingerprintField field : FingerprintField.values()) {
      field.extractValue(iph, tcph).ifPresent(value -> data.put(field, value));
    }

    return new SynFingerprint(data);
  }

  public Optional<Integer> getIpTotalLen() {
    return FingerprintField.IP_TOTAL_LEN.get(this.data);
  }

  public Optional<Integer> getIpId() {
    return FingerprintField.IP_IDENTIFICATION.get(this.data);
  }

  public Optional<Integer> getIpTtl() {
    return FingerprintField.IP_TIME_TO_LIVE.get(this.data);
  }

  public Optional<String> getTcpOptions() {
    return FingerprintField.TCP_OPTIONS.get(this.data);
  }

  public Optional<Integer> getTcpOffset() {
    return FingerprintField.TCP_OFFSET.get(this.data);
  }

  public Optional<Integer> getTcpWindowScaling() {
    return FingerprintField.TCP_WINDOW_SCALING.get(this.data);
  }

  public Optional<Integer> getTcpWindowSize() {
    return FingerprintField.TCP_WINDOW_SIZE.get(this.data);
  }

  public Optional<Integer> getTcpTimestamp() {
    return FingerprintField.TCP_TIMESTAMP.get(this.data);
  }

  public Optional<Integer> getTcpTimestampEchoReply() {
    return FingerprintField.TCP_TIMESTAMP_ECHO_REPLY.get(this.data);
  }

  public Optional<Integer> getTcpMss() {
    return FingerprintField.TCP_MAXIMUM_SEGMENT_SIZE.get(this.data);
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
    private final Map<FingerprintField, Object> data = new EnumMap<>(FingerprintField.class);

    public Builder ipTotalLen(int totalLen) {
      data.put(FingerprintField.IP_TOTAL_LEN, totalLen);
      return this;
    }

    public Builder ipId(int id) {
      data.put(FingerprintField.IP_IDENTIFICATION, id);
      return this;
    }

    public Builder ipTtl(int ttl) {
      data.put(FingerprintField.IP_TIME_TO_LIVE, ttl);
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
      data.put(FingerprintField.TCP_MAXIMUM_SEGMENT_SIZE, mss);
      return this;
    }

    public SynFingerprint build() {
      return new SynFingerprint(new EnumMap<>(data));
    }

  }

}
