package com.dietmall.user.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MealImageStorageService {

	private static final long MAX_FILE_SIZE =
	        20 * 1024 * 1024;

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of(
                    ".jpg",
                    ".jpeg",
                    ".png",
                    ".webp",
                    ".heic",
                    ".heif"
            );

    private final Path mealUploadPath;

    public MealImageStorageService(
            @Value("${app.upload-dir}") String uploadDir) {

        this.mealUploadPath =
                Paths.get(uploadDir)
                        .toAbsolutePath()
                        .normalize()
                        .resolve("meals");

        try {

            Files.createDirectories(
                    mealUploadPath
            );

        } catch (IOException e) {

            throw new IllegalStateException(
                    "식단 이미지 저장 폴더를 만들 수 없습니다.",
                    e
            );
        }
    }

    // 식단 사진 저장
    public String saveImage(
            MultipartFile file) {

        validateImage(file);

        String originalFilename =
                file.getOriginalFilename();

        String extension =
                getExtension(
                        originalFilename
                );

        String storedFilename =
                UUID.randomUUID()
                        .toString()
                        + extension;

        Path destination =
                mealUploadPath
                        .resolve(storedFilename)
                        .normalize();

        if (!destination.getParent()
                .equals(mealUploadPath)) {

            throw new IllegalArgumentException(
                    "잘못된 파일 경로입니다."
            );
        }

        try (InputStream inputStream =
                file.getInputStream()) {

            Files.copy(
                    inputStream,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {

            throw new IllegalStateException(
                    "식단 이미지 저장에 실패했습니다.",
                    e
            );
        }

        return "/uploads/meals/"
                + storedFilename;
    }

    // 저장된 식단 사진 불러오기
    public Resource loadImage(
            String imageUrl) {

        if (imageUrl == null
                || imageUrl.isBlank()) {

            throw new IllegalArgumentException(
                    "등록된 식단 이미지가 없습니다."
            );
        }

        String filename =
                imageUrl.substring(
                        imageUrl.lastIndexOf('/') + 1
                );

        Path imagePath =
                mealUploadPath
                        .resolve(filename)
                        .normalize();

        if (!imagePath.getParent()
                .equals(mealUploadPath)) {

            throw new IllegalArgumentException(
                    "잘못된 이미지 경로입니다."
            );
        }

        try {

            Resource resource =
                    new UrlResource(
                            imagePath.toUri()
                    );

            if (!resource.exists()
                    || !resource.isReadable()) {

                throw new IllegalArgumentException(
                        "식단 이미지 파일을 찾을 수 없습니다."
                );
            }

            return resource;

        } catch (MalformedURLException e) {

            throw new IllegalStateException(
                    "식단 이미지를 불러올 수 없습니다.",
                    e
            );
        }
    }

    // 업로드할 이미지 검사
    private void validateImage(
            MultipartFile file) {

        if (file == null
                || file.isEmpty()) {

            throw new IllegalArgumentException(
                    "이미지 파일을 선택해주세요."
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {

            throw new IllegalArgumentException(
                    "이미지 파일은 20MB 이하만 업로드할 수 있습니다."
            );
        }

        String contentType =
                file.getContentType();

        if (contentType == null
                || !contentType.startsWith("image/")) {

            throw new IllegalArgumentException(
                    "이미지 파일만 업로드할 수 있습니다."
            );
        }

        String extension =
                getExtension(
                        file.getOriginalFilename()
                );

        if (!ALLOWED_EXTENSIONS.contains(
                extension)) {

            throw new IllegalArgumentException(
                    "jpg, jpeg, png, webp, heic, heif 파일만 업로드할 수 있습니다."
            );
        }
    }

    // 원래 파일 이름에서 확장자 가져오기
    private String getExtension(
            String filename) {

        if (filename == null
                || filename.isBlank()) {

            return "";
        }

        int dotIndex =
                filename.lastIndexOf('.');

        if (dotIndex < 0
                || dotIndex == filename.length() - 1) {

            return "";
        }

        return filename
                .substring(dotIndex)
                .toLowerCase();
    }
}