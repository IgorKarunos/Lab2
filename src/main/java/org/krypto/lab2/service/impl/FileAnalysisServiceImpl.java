package org.krypto.lab2.service.impl;

import org.krypto.lab2.dto.CharacterFrequency;
import org.krypto.lab2.dto.EntropyResponse;
import org.krypto.lab2.dto.FrequencyAnalysisResponse;
import org.krypto.lab2.service.FileAnalysisService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileAnalysisServiceImpl implements FileAnalysisService {

    private static final int BUFFER_SIZE = 8192;

    @Override
    public EntropyResponse calculateEntropy(MultipartFile file) {
        try {
            long[] frequencies = countFrequencies(file);
            long totalBytes = file.getSize();
            double entropy = calculateShannonEntropy(frequencies, totalBytes);
            int uniqueChars = countUniqueCharacters(frequencies);

            return new EntropyResponse(file.getOriginalFilename(), totalBytes, entropy, uniqueChars);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при анализе файла", e);
        }
    }

    @Override
    public FrequencyAnalysisResponse analyzeFrequencies(MultipartFile file) {
        try {
            long[] frequencies = countFrequencies(file);
            long totalBytes = file.getSize();
            List<CharacterFrequency> result = new ArrayList<>();

            for (int i = 0; i < 256; i++) {
                if (frequencies[i] > 0) {
                    double probability = (double) frequencies[i] / totalBytes;
                    char character = (i >= 32 && i <= 126) ? (char) i : '.';
                    result.add(new CharacterFrequency(i, character, frequencies[i], probability));
                }
            }

            return new FrequencyAnalysisResponse(file.getOriginalFilename(), totalBytes, result);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при анализе файла", e);
        }
    }

    private long[] countFrequencies(MultipartFile file) throws IOException {
        long[] frequencies = new long[256];

        try (InputStream is = file.getInputStream()) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    frequencies[buffer[i] & 0xFF]++;
                }
            }
        }

        return frequencies;
    }

    private double calculateShannonEntropy(long[] frequencies, long totalBytes) {
        if (totalBytes == 0) return 0.0;

        double entropy = 0.0;
        for (long freq : frequencies) {
            if (freq > 0) {
                double probability = (double) freq / totalBytes;
                entropy -= probability * (Math.log(probability) / Math.log(2));
            }
        }

        return entropy;
    }

    private int countUniqueCharacters(long[] frequencies) {
        int count = 0;
        for (long freq : frequencies) {
            if (freq > 0) count++;
        }
        return count;
    }
}
