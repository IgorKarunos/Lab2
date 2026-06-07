package org.krypto.lab2.service;

import java.io.IOException;

public interface FileGeneratorService {
    byte[] generateFile(String type, int size) throws IOException;
}
