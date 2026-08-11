package com.dietmall.group.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DietLogImageService {

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of(
                    "jpg",
                    "jpeg",
                    "png",
                    "webp"
            );


    private final Path dietLogUploadPath;


    public DietLogImageService(
            @Value("${app.upload-dir}") String uploadDir) {

        this.dietLogUploadPath =
                Paths.get(
                        uploadDir,
                        "group-logs"
                )
                .toAbsolutePath()
                .normalize();
    }


    public String save(
            MultipartFile file) {

        validateFile(file);


        String extension =
                getExtension(
                        file.getOriginalFilename()
                );


        String savedFileName =
                UUID.randomUUID()
                        + "."
                        + extension;


        try {

            Files.createDirectories(
                    dietLogUploadPath
            );


            Path targetPath =
                    dietLogUploadPath
                            .resolve(savedFileName)
                            .normalize();


            if (!targetPath.startsWith(
                    dietLogUploadPath)) {

                throw new IllegalArgumentException(
                        "잘못된 파일 경로입니다."
                );
            }


            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );


            return savedFileName;

        } catch (IOException e) {

            throw new IllegalStateException(
                    "다이어트 로그 사진 저장에 실패했습니다.",
                    e
            );
        }
    }


    public Path getFilePath(
            String savedFileName) {

        Path filePath =
                dietLogUploadPath
                        .resolve(savedFileName)
                        .normalize();


        if (!filePath.startsWith(
                dietLogUploadPath)) {

            throw new IllegalArgumentException(
                    "잘못된 파일 경로입니다."
            );
        }


        return filePath;
    }


    public void delete(
            String savedFileName) {

        if (savedFileName == null
                || savedFileName.isBlank()) {

            return;
        }


        Path filePath =
                getFilePath(savedFileName);


        try {

            Files.deleteIfExists(filePath);

        } catch (IOException e) {

            throw new IllegalStateException(
                    "다이어트 로그 사진 삭제에 실패했습니다.",
                    e
            );
        }
    }


    private void validateFile(
            MultipartFile file) {

        if (file == null
                || file.isEmpty()) {

            throw new IllegalArgumentException(
                    "다이어트 로그 사진은 필수입니다."
            );
        }


        String extension =
                getExtension(
                        file.getOriginalFilename()
                );


        if (!ALLOWED_EXTENSIONS.contains(
                extension)) {

            throw new IllegalArgumentException(
                    "jpg, jpeg, png, webp 파일만 업로드할 수 있습니다."
            );
        }
    }


    private String getExtension(
            String originalFilename) {

        if (originalFilename == null
                || originalFilename.isBlank()) {

            throw new IllegalArgumentException(
                    "파일 이름을 확인할 수 없습니다."
            );
        }


        int dotIndex =
                originalFilename
                        .lastIndexOf('.');


        if (dotIndex < 0
                || dotIndex
                == originalFilename.length() - 1) {

            throw new IllegalArgumentException(
                    "파일 확장자를 확인할 수 없습니다."
            );
        }


        return originalFilename
                .substring(dotIndex + 1)
                .toLowerCase();
    }
}