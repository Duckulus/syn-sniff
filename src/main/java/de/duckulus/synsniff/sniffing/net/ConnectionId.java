package de.duckulus.synsniff.sniffing.net;

import java.net.Inet4Address;

public record ConnectionId(Inet4Address address, int port) {
}
