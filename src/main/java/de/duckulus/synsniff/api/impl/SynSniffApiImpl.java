package de.duckulus.synsniff.api.impl;

import de.duckulus.synsniff.api.SynSniffApi;

public class SynSniffApiImpl implements SynSniffApi {

  private final LocalFingerprintService fingerprintService;

  public SynSniffApiImpl() {
    fingerprintService = new LocalFingerprintService();
  }

  @Override
  public LocalFingerprintService getFingerprintService() {
    return fingerprintService;
  }

}
