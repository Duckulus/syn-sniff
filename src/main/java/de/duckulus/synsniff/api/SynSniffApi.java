package de.duckulus.synsniff.api;

/**
 * This is the public facing api of SynSniff intended to be used by Developers
 */
public interface SynSniffApi {

  /**
   * @return an instance of {@link FingerprintService}
   */
  FingerprintService getFingerprintService();

}
