package org.krypto.lab2.controller;

import lombok.RequiredArgsConstructor;
import org.krypto.lab2.dto.EntropyResponse;
import org.krypto.lab2.dto.FileGenerationRequest;
import org.krypto.lab2.dto.FrequencyAnalysisResponse;
import org.krypto.lab2.service.FileAnalysisService;
import org.krypto.lab2.service.FileGeneratorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/entropy")
@RequiredArgsConstructor
public class EntropyController {

    private final FileAnalysisService fileAnalysisService;
    private final FileGeneratorService fileGeneratorService;

    @PostMapping("/calculate")
    public ResponseEntity<EntropyResponse> calculateEntropy(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileAnalysisService.calculateEntropy(file));
    }

    @PostMapping("/frequencies")
    public ResponseEntity<FrequencyAnalysisResponse> analyzeFrequencies(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileAnalysisService.analyzeFrequencies(file));
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateFile(@RequestBody FileGenerationRequest request) throws IOException {
        byte[] data = fileGeneratorService.generateFile(request.type(), request.size());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"generated_" + request.type() + ".bin\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }

    @PostMapping("/generate-and-analyze")
    public ResponseEntity<EntropyResponse> generateAndAnalyze(@RequestBody FileGenerationRequest request) throws IOException {
        byte[] data = fileGeneratorService.generateFile(request.type(), request.size());

        MultipartFile mockFile = new MockMultipartFile("generated.bin", data);
        EntropyResponse response = fileAnalysisService.calculateEntropy(mockFile);

        return ResponseEntity.ok(response);
    }

    private static class MockMultipartFile implements MultipartFile {
        private final String name;
        private final byte[] content;

        public MockMultipartFile(String name, byte[] content) {
            this.name = name;
            this.content = content;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return name;
        }

        @Override
        public String getContentType() {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() {
            return content;
        }

        @Override
        public java.io.InputStream getInputStream() {
            return new java.io.ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException {
            java.nio.file.Files.write(dest.toPath(), content);
        }
    }
}
