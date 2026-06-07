package org.krypto.lab2.dto;

public record EntropyResponse(String fileName, long fileSize, double entropy, int uniqueCharacters) {
}
