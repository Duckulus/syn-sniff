package io.github.duckulus.synsniff.sniffing;

import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.TcpPacket;

import java.time.Instant;

public record SynPayload(Instant timestamp, IpV4Packet.IpV4Header iph, TcpPacket.TcpHeader tcph) {
}
