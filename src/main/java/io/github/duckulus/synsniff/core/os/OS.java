package io.github.duckulus.synsniff.core.os;

import io.github.duckulus.synsniff.core.SynFingerprint;

public enum OS {

  LINUX(ReferenceFingerprints.LINUX),
  WINDOWS(ReferenceFingerprints.WINDOWS),
  APPLE(ReferenceFingerprints.APPLE);

  private final SynFingerprint referenceFingerprint;

  OS(SynFingerprint referenceFingerprint) {
    this.referenceFingerprint = referenceFingerprint;
  }

  public SynFingerprint getReferenceFingerprint() {
    return referenceFingerprint;
  }

}
