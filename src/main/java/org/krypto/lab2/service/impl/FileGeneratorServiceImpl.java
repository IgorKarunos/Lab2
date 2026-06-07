package org.krypto.lab2.service.impl;

import org.krypto.lab2.service.FileGeneratorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;

@Service
public class FileGeneratorServiceImpl implements FileGeneratorService {

    private final Random random = new Random();

    @Override
    public byte[] generateFile(String type, int size) throws IOException {
        return switch (type.toLowerCase()) {
            case "identical" -> generateIdenticalBytes(size);
            case "binary" -> generateBinaryBytes(size);
            case "random" -> generateRandomBytes(size);
            case "text" -> generateEnglishText(size);
            default -> throw new IllegalArgumentException("Неизвестный тип файла: " + type);
        };
    }

    private byte[] generateIdenticalBytes(int size) {
        byte[] data = new byte[size];
        byte value = (byte) 'A';
        java.util.Arrays.fill(data, value);
        return data;
    }

    private byte[] generateBinaryBytes(int size) {
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = (byte) (random.nextInt(2) == 0 ? '0' : '1');
        }
        return data;
    }

    private byte[] generateRandomBytes(int size) {
        byte[] data = new byte[size];
        random.nextBytes(data);
        return data;
    }

    private byte[] generateEnglishText(int size) {
        String sampleText = "The quick brown fox jumps over the lazy dog. " +
                "This is a sample English text with common words and phrases. " +
                "Entropy measures the amount of information or uncertainty in data. " +
                "Higher entropy means more randomness and less predictability. ";

        StringBuilder sb = new StringBuilder();
        while (sb.length() < size) {
            sb.append(sampleText);
        }

        return sb.toString().substring(0, size).getBytes();
    }
}
