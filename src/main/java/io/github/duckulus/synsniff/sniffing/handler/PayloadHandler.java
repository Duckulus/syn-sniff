package io.github.duckulus.synsniff.sniffing.handler;

import io.github.duckulus.synsniff.sniffing.SynPayload;

public interface PayloadHandler {

  void handlePayload(SynPayload payload);

}
