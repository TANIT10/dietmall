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
            20L * 1024L * 1024L;

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of(
                    ".jpg",
                    ".jpeg",
                    ".png",
                    ".webp",
                    ".heic",
                    ".heif"
            );

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of(
                    "image/jpeg",
                    "image/png",
                    "image/webp",
                    "image/heic",
                    "image/heif"
            );

    private static final Set<String> ALLOWED_HEIF_BRANDS =
            Set.of(
                    "heic",
                    "heix",
                    "hevc",
                    "hevx",
                    "heif",
                    "mif1",
                    "msf1"
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

        validateImage(
                file
        );

        String originalFilename =
                file.getOriginalFilename();

        String extension =
                getExtension(
                        originalFilename
                );

        String storedFilename =
                UUID.randomUUID()
                        + extension;

        Path destination =
                mealUploadPath
                        .resolve(storedFilename)
                        .normalize();

        if (!destination.startsWith(mealUploadPath)) {

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
                extractFilenameFromImageUrl(
                        imageUrl
                );

        Path imagePath =
                mealUploadPath
                        .resolve(filename)
                        .normalize();

        if (!imagePath.startsWith(mealUploadPath)) {

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

        String contentType =
                file.getContentType();

        if (contentType == null
                || !ALLOWED_CONTENT_TYPES.contains(
                        contentType.toLowerCase()
                )) {

            throw new IllegalArgumentException(
                    "올바른 이미지 파일 형식이 아닙니다."
            );
        }

        validateImageSignature(
                file,
                extension
        );
    }

    private void validateImageSignature(
            MultipartFile file,
            String extension) {

        byte[] header =
                new byte[16];

        try (InputStream inputStream =
                     file.getInputStream()) {

            int read =
                    inputStream.read(header);

            if (read < 12) {

                throw new IllegalArgumentException(
                        "올바른 이미지 파일이 아닙니다."
                );
            }

        } catch (IOException e) {

            throw new IllegalStateException(
                    "이미지 파일을 확인하는 중 오류가 발생했습니다.",
                    e
            );
        }

        boolean valid;

        if (extension.equals(".jpg")
                || extension.equals(".jpeg")) {

            valid =
                    isJpeg(header);

        } else if (extension.equals(".png")) {

            valid =
                    isPng(header);

        } else if (extension.equals(".webp")) {

            valid =
                    isWebp(header);

        } else if (extension.equals(".heic")
                || extension.equals(".heif")) {

            valid =
                    isHeif(header);

        } else {

            valid = false;
        }

        if (!valid) {

            throw new IllegalArgumentException(
                    "파일 확장자와 실제 이미지 형식이 일치하지 않습니다."
            );
        }
    }

    private boolean isJpeg(
            byte[] header) {

        return (header[0] & 0xFF) == 0xFF
                && (header[1] & 0xFF) == 0xD8
                && (header[2] & 0xFF) == 0xFF;
    }

    private boolean isPng(
            byte[] header) {

        return (header[0] & 0xFF) == 0x89
                && header[1] == 0x50
                && header[2] == 0x4E
                && header[3] == 0x47
                && header[4] == 0x0D
                && header[5] == 0x0A
                && header[6] == 0x1A
                && header[7] == 0x0A;
    }

    private boolean isWebp(
            byte[] header) {

        return header[0] == 'R'
                && header[1] == 'I'
                && header[2] == 'F'
                && header[3] == 'F'
                && header[8] == 'W'
                && header[9] == 'E'
                && header[10] == 'B'
                && header[11] == 'P';
    }

    private boolean isHeif(
            byte[] header) {

        boolean hasFtyp =
                header[4] == 'f'
                        && header[5] == 't'
                        && header[6] == 'y'
                        && header[7] == 'p';

        if (!hasFtyp) {
            return false;
        }

        String brand =
                new String(
                        header,
                        8,
                        4
                ).toLowerCase();

        return ALLOWED_HEIF_BRANDS.contains(
                brand
        );
    }

    private String extractFilenameFromImageUrl(
            String imageUrl) {

        int slashIndex =
                imageUrl.lastIndexOf('/');

        if (slashIndex < 0
                || slashIndex == imageUrl.length() - 1) {

            throw new IllegalArgumentException(
                    "잘못된 이미지 경로입니다."
            );
        }

        return imageUrl.substring(
                slashIndex + 1
        );
    }

    // 원래 파일 이름에서 확장자 가져오기
    private String getExtension(
            String filename) {

        if (filename == null
                || filename.isBlank()) {

            throw new IllegalArgumentException(
                    "파일 이름을 확인할 수 없습니다."
            );
        }

        int dotIndex =
                filename.lastIndexOf('.');

        if (dotIndex < 0
                || dotIndex == filename.length() - 1) {

            throw new IllegalArgumentException(
                    "파일 확장자를 확인할 수 없습니다."
            );
        }

        return filename
                .substring(dotIndex)
                .toLowerCase();
    }
}