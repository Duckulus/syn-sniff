package io.github.duckulus.synsniff.sniffing;

import io.github.duckulus.synsniff.SynSniff;
import io.github.duckulus.synsniff.exception.SynSniffException;
import io.github.duckulus.synsniff.sniffing.handler.PayloadHandler;
import org.pcap4j.core.*;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.factory.PacketFactories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A thread which listens for incoming TCP SYN packets on a specific network interface and port
 * Published these to a list of handlers
 */
public class SynPacketSniffer extends Thread {

  private static final Logger log = LoggerFactory.getLogger(SynPacketSniffer.class);

  static {
    // due to Bukkits class loading behavior pcap4j is not able to find the PacketFactoryBinder in the ClassPath
    // this hack temporarily updates the context class loader of the thread in order for pcap4j to be able to initialize itself correctly
    ClassLoader originalClassLoader = null;
    try {
      originalClassLoader = Thread.currentThread().getContextClassLoader();
      Thread.currentThread().setContextClassLoader(SynSniff.class.getClassLoader());
      Class.forName(PacketFactories.class.getName());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      Thread.currentThread().setContextClassLoader(originalClassLoader);
    }
  }

  public static SynPacketSniffer run(String networkInterfaceName, int port) {
    return run(networkInterfaceName, port, Executors.newSingleThreadExecutor());
  }

  public static SynPacketSniffer run(String networkInterfaceName, int port, Executor executor) {
    SynPacketSniffer sniffer = new SynPacketSniffer(networkInterfaceName, port, executor);
    sniffer.start();
    return sniffer;
  }


  private PcapHandle handle;

  private final String networkInterfaceName;
  private final int port;

  private final Executor executor;

  private final Set<PayloadHandler> handlers = Collections.newSetFromMap(new ConcurrentHashMap<>());

  private SynPacketSniffer(String networkInterfaceName, int port, Executor executor) {
    this.networkInterfaceName = networkInterfaceName;
    this.port = port;
    this.executor = executor;
  }

  @Override
  public void run() {
    PcapNetworkInterface nic;
    try {
      nic = Pcaps.getDevByName(networkInterfaceName);
    } catch (PcapNativeException e) {
      log.error("Error while accessing network interface", e);
      return;
    }
    if (nic == null) {
      throw new SynSniffException("Specified network interface '" + networkInterfaceName + "' not found");
    }
    int snapshotLength = 65536;
    int readTimeout = 50;
    try {
      handle = nic.openLive(snapshotLength, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, readTimeout);
      handle.setFilter(getPacketFilter(port), BpfProgram.BpfCompileMode.OPTIMIZE);
      PacketListener listener = p -> {
        if (!p.contains(IpV4Packet.class) || !p.contains(TcpPacket.class)) {
          log.warn("got weird packet {}", p);
          return;
        }
        Instant timestamp = handle.getTimestamp().toInstant();
        IpV4Packet ipp = p.get(IpV4Packet.class);
        TcpPacket tcpp = p.get(TcpPacket.class);
        if (!tcpp.getHeader().getSyn()) {
          return;
        }
        SynPayload payload = new SynPayload(timestamp, ipp.getHeader(), tcpp.getHeader());
        publishPayload(payload);
      };
      handle.loop(-1, listener, executor);
    } catch (PcapNativeException | NotOpenException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  /*
  https://web.archive.org/web/20250628150448/https://support.excentis.com/knowledge/article/186
   */
  private String getPacketFilter(int dstPort) {
    // catch tcp/ip packets sent to dstPort with SYN set
    return String.format("ip and tcp and dst port %d and tcp[tcpflags] & tcp-syn != 0", dstPort);
  }

  private void publishPayload(SynPayload payload) {
    handlers.forEach(handler -> handler.handlePayload(payload));
  }

  public void close() {
    if (handle != null) {
      try {
        handle.breakLoop();
        handle.close();
        handle = null;
      } catch (NotOpenException ignored) {
      }
    }
  }

  /**
   * Add a handler to be called every time a SYN packet arrives
   *
   * @param payloadHandler the handler to be registered
   */
  public void registerPayloadHandler(PayloadHandler payloadHandler) {
    handlers.add(payloadHandler);
  }

  /**
   * Removes a handler from the list of handlers that are called when a SYN packet arrived
   *
   * @param payloadHandler the handler to be unregistered
   */
  public void unregisterPayloadHandler(PayloadHandler payloadHandler) {
    handlers.remove(payloadHandler);
  }


}
