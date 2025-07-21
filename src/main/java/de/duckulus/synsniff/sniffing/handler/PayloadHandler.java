package de.duckulus.synsniff.sniffing.handler;

import de.duckulus.synsniff.sniffing.SynPayload;

public interface PayloadHandler {

  void handlePayload(SynPayload payload);

}
