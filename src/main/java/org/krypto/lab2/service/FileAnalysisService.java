package org.krypto.lab2.service;

import org.krypto.lab2.dto.EntropyResponse;
import org.krypto.lab2.dto.FrequencyAnalysisResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileAnalysisService {
    EntropyResponse calculateEntropy(MultipartFile file);

    FrequencyAnalysisResponse analyzeFrequencies(MultipartFile file);
}
