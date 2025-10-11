package com.ktbweek4.community.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalFileStorage {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.public-base-url}")
    private String publicBaseUrl;

    /**
     * 파일 저장 (로컬 디스크)
     */
    public StoredFile save(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일은 업로드할 수 없습니다.");
        }

        // 원본 확장자 보존
        String originalName = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0) ext = originalName.substring(dot); // ".jpg"

        // 고유 파일명 생성
        String newName = UUID.randomUUID().toString().replace("-", "") + ext;
        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);

        Path target = dir.resolve(newName).normalize().toAbsolutePath();

        // 업로드 경로 이탈 방지
        if (!target.startsWith(dir.toAbsolutePath())) {
            throw new SecurityException("잘못된 파일 경로입니다.");
        }

        // 파일 복사
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        // 외부 접근 URL 생성
        String url = publicBaseUrl + "/" + newName;

        return new StoredFile(newName, url);
    }

    /**
     * URL을 기준으로 로컬 파일 삭제
     * - DB에 저장된 public URL을 받아 내부 경로로 변환 후 삭제
     */
    public void deleteByUrl(String publicUrl) throws IOException {
        if (publicUrl == null || publicUrl.isBlank()) return;

        // public URL 예: http://localhost:8080/files/abc123.jpg
        // → 실제 경로: /Users/.../uploads/abc123.jpg
        if (!publicUrl.startsWith(publicBaseUrl)) {
            throw new IllegalArgumentException("해당 URL은 이 서버에서 관리하는 파일이 아닙니다: " + publicUrl);
        }

        String fileName = publicUrl.substring(publicBaseUrl.length() + 1); // "/" 제거
        Path filePath = Paths.get(uploadDir).resolve(fileName).normalize().toAbsolutePath();

        // 보안 체크
        if (!filePath.startsWith(Paths.get(uploadDir).toAbsolutePath())) {
            throw new SecurityException("잘못된 파일 삭제 요청입니다.");
        }

        // 파일 존재 여부 확인 후 삭제
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new IOException("파일 삭제 중 오류 발생: " + e.getMessage(), e);
        }
    }

    /**
     * 저장 결과 DTO (레코드)
     */
    public record StoredFile(String storedName, String publicUrl) {}
}