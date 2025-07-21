package de.duckulus.synsniff.core;

public class ReferenceFingerprints {

  public static SynFingerprint LINUX = SynFingerprint.builder()
          .ipTotalLen(60)
          .ipId(61394)
          .ipTtl(50)
          .tcpOptions("Maximum Segment Size|SACK Permitted|Timestamps|No Operation|Window Scale")
          .tcpOffset(10)
          .tcpWindowScaling(7)
          .tcpWindowSize(64240)
          .tcpTimestamp(687336770)
          .tcpTimestampEchoReply(0)
          .tcpMss(1460)
          .build();

  public static SynFingerprint WINDOWS = SynFingerprint.builder()
          .ipTotalLen(52)
          .ipId(44594)
          .ipTtl(111)
          .tcpOptions("Maximum Segment Size|No Operation|Window Scale|No Operation|No Operation|SACK Permitted")
          .tcpOffset(8)
          .tcpWindowScaling(8)
          .tcpWindowSize(64240)
          .tcpMss(1452)
          .build();

  public static SynFingerprint APPLE = SynFingerprint.builder()
          .ipTotalLen(64)
          .ipId(0)
          .ipTtl(50)
          .tcpOptions("")
          .tcpOffset(11)
          .tcpWindowScaling(6)
          .tcpWindowSize(65535)
          .tcpTimestamp(661931028)
          .tcpTimestampEchoReply(0)
          .tcpMss(1460)
          .build();

}
