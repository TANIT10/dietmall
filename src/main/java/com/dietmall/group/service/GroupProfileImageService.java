package com.dietmall.group.service;

import java.io.IOException;
import java.io.InputStream;
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
public class GroupProfileImageService {

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of(
                    "jpg",
                    "jpeg",
                    "png",
                    "webp"
            );


    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of(
                    "image/jpeg",
                    "image/png",
                    "image/webp"
            );


    private static final long MAX_FILE_SIZE =
            20L * 1024L * 1024L;


    private final Path groupProfileUploadPath;


    public GroupProfileImageService(
            @Value("${app.upload-dir}") String uploadDir) {

        this.groupProfileUploadPath =
                Paths.get(
                        uploadDir,
                        "group-profiles"
                )
                .toAbsolutePath()
                .normalize();
    }


    public String save(
            MultipartFile file) {

        validateFile(
                file
        );


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
                    groupProfileUploadPath
            );


            Path targetPath =
                    groupProfileUploadPath
                            .resolve(savedFileName)
                            .normalize();


            if (!targetPath.startsWith(
                    groupProfileUploadPath)) {

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
                    "프로필 사진 저장에 실패했습니다.",
                    e
            );
        }
    }


    public Path getFilePath(
            String savedFileName) {

        if (savedFileName == null
                || savedFileName.isBlank()) {

            throw new IllegalArgumentException(
                    "파일 이름이 올바르지 않습니다."
            );
        }


        Path filePath =
                groupProfileUploadPath
                        .resolve(savedFileName)
                        .normalize();


        if (!filePath.startsWith(
                groupProfileUploadPath)) {

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
                getFilePath(
                        savedFileName
                );


        try {

            Files.deleteIfExists(
                    filePath
            );

        } catch (IOException e) {

            throw new IllegalStateException(
                    "프로필 사진 삭제에 실패했습니다.",
                    e
            );
        }
    }


    private void validateFile(
            MultipartFile file) {

        if (file == null
                || file.isEmpty()) {

            throw new IllegalArgumentException(
                    "프로필 사진 파일은 필수입니다."
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
                    "jpg, jpeg, png, webp 파일만 업로드할 수 있습니다."
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
                new byte[12];


        try (
                InputStream inputStream =
                        file.getInputStream()
        ) {

            int read =
                    inputStream.read(
                            header
                    );


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


        if (extension.equals("jpg")
                || extension.equals("jpeg")) {

            valid =
                    isJpeg(
                            header
                    );

        } else if (extension.equals("png")) {

            valid =
                    isPng(
                            header
                    );

        } else if (extension.equals("webp")) {

            valid =
                    isWebp(
                            header
                    );

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