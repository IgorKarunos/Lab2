package org.krypto.lab2.dto;

public record FrequencyAnalysisResponse(String fileName, long fileSize,
                                        java.util.List<CharacterFrequency> frequencies) {
}
