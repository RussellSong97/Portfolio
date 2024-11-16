package com.cuv.admin.domain.attachment;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.entity.Attachment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 파일 업로드/다운로드 REST API<br /><br />
 *
 * 필수 properties:<br />
 * {@link MultipartProperties spring.servlet.multipart.location}: 업로드 파일 임시 경로<br />
 * {@link AttachmentService serviceUrl service-url}: realUrl 출력을 위한 실 도메인 주소
 *
 * @since 2022-08-05
 * @author Tae-rok Hwang
 * @version 1.1
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AttachmentRestController {

    private final AttachmentService attachmentService;

    @Value("${spring.servlet.multipart.location}")
    private String multipartLocation;

    /**
     * 파일 업로드<br />
     * 모든 파일 업로드가 성공했을 경우 code=200 반환<br />
     * 일부 파일 업로드가 실패했을 경우 code=200, message="PARTIAL SUCCESS" 반환<br />
     * 파일 업로드에 실패했을 경우 code=500 반환<br />
     * 파일 업로드 성공 여부는 요청 List size 와 반환 List size 를 비교하여 확인한다.
     *
     * @param files multipart/form-data 로 전송된 파일 스트림
     * @param source 업로드 파일의 출처, "editor" 를 넣으면 에디터에서 사용하는 파일로 인식하여 view=Y 로 설정된다.
     * @param request HttpServletRequest
     * @return JSONResponse 업로드 결과 정보
     * @author Tae-rok Hwang
     */
    @PostMapping("/api/file/uploads")
    public JSONResponse<?> uploadFiles(@RequestParam("files") List<MultipartFile> files,
                                       @RequestParam(value = "source", required = false) String source,
                                       HttpServletRequest request) {

        try {
            if (files.isEmpty()) return new JSONResponse<>(400, "NO FILES");

            List<Attachment> attachmentLists = attachmentService.uploadFiles(files, source, request);
            String message = files.size() == attachmentLists.size() ? "SUCCESS" : "PARTIAL SUCCESS";

            // DTO로 변환하여 반환
            return new JSONResponse<>(200, message, attachmentLists.stream().map(Attachment::toDto));
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
    }

    /**
     * 파일 다운로드
     *
     * @param uuid 다운로드 할 파일의 uuid
     * @return DB에 파일 정보가 없거나 viewYn=Y 아니면 404 (Not Found)<br />
     * DB에 파일 정보는 있지만 실제 파일이 없는 경우 410 (Gone)<br />
     * 성공하면 200 (OK) 와 함께 파일 스트림 출력 (Content-Disposition: attachment; filename="파일명")
     * @author Tae-rok Hwang
     */
    @GetMapping("/api/file/download/{uuid}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("uuid") String uuid) {
        Attachment attachment = attachmentService.findByUUID(uuid);

        // DB에 파일 정보가 없거나 viewYn=Y 아니면 404
        if (attachment == null || attachment.getViewYn() != YN.Y)
            return ResponseEntity.notFound().build();

        try {
            Path path = Paths.get(multipartLocation + attachment.getPath());

            // DB에 파일 정보는 있지만 실제 파일이 없는 경우 410 (Gone)
            if (!Files.exists(path)) return ResponseEntity.status(410).build();

            Resource resource = new InputStreamResource(Files.newInputStream(path));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                    .header(HttpHeaders.CONTENT_LENGTH, attachment.getSize().toString())
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.builder("attachment")
                                    .filename(attachment.getOriginalName(), StandardCharsets.UTF_8)
                                    .build().toString())
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .header(HttpHeaders.PRAGMA, "no-cache")
                    .header(HttpHeaders.EXPIRES, "0")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 파일 미리보기 (이미지)<br />
     * MIME-Type 이 image/* 또는 application/pdf 인 경우에만 미리보기 가능 (브라우저에서 지원하는 경우)
     *
     * @param uuid 미리보기 할 파일의 uuid
     * @param size 이미지일 때 출력하기 위한 썸네일의 너비<br />
     * @return DB에 파일 정보가 없거나 viewYn=Y 아니면 404 (Not Found)<br />
     * DB에 파일 정보는 있지만 실제 파일이 없는 경우 410 (Gone)<br />
     * 이미지 또는 PDF 형식이 아니면 미리보기 불가 415 (Unsupported Media Type)<br />
     * 성공하면 200 (OK) 와 함께 파일 스트림 출력
     * @author Tae-rok Hwang
     */
    @GetMapping("/api/file/view/{uuid}")
    public ResponseEntity<byte[]> fileView(@PathVariable("uuid") String uuid, @RequestParam(value = "size",required = false) Integer size) {
        Attachment attachment = attachmentService.findByUUID(uuid);

        // DB에 파일 정보가 없거나 viewYn=Y 아니면 404
        if (attachment == null || attachment.getViewYn() != YN.Y)
            return ResponseEntity.notFound().build();

        try {
            Path path = Paths.get(multipartLocation + attachment.getPath());

            // DB에 파일 정보는 있지만 실제 파일이 없는 경우 410 (Gone)
            if (!Files.exists(path)) return ResponseEntity.status(410).build();

            // 이미지 또는 PDF 형식이 아니면 미리보기 불가 처리
            if (Files.probeContentType(path) == null ||
                    (!Files.probeContentType(path).startsWith("image/") && !Files.probeContentType(path).startsWith("application/pdf")))
                return ResponseEntity.status(415).build();

            // 출력 버퍼 스트림
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // 이미지 너비가 지정되어 있으면 크기에 맞춰 썸네일 생성
            if (size != null && size > 0) {
                try {
                    BufferedImage bufferedImage = Thumbnails.of(path.toFile()).width(size).asBufferedImage();
                    ImageIO.write(bufferedImage, "JPEG", outputStream); // 썸네일 이미지 출력 버퍼에 저장
                } catch (IOException e) {
                    log.warn("썸네일 생성 실패: UUID={}, size={}", uuid, size);
                }
            }

            // 썸네일 요청 하지 않거나 썸네일 생성 실패했다면 원본 이미지로
            if (outputStream.size() < 1)
                Files.copy(path, outputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                    .header(HttpHeaders.CONTENT_LENGTH, "" + outputStream.size())
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 파일 삭제<br />
     * 업로드 된 파일을 삭제하지만 DB 에는 rows 를 삭제하지 않고 viewYn=N 으로 변경하여 이력을 남긴다.
     *
     * @param uuid 삭제할 파일의 uuid
     * @return 200 (OK) 반환. 단, 파일 삭제에 실패했을 경우 data 값이 false 로 반환된다.
     * @author Tae-rok Hwang
     */
    @DeleteMapping("/api/file/delete/{uuid}")
    public JSONResponse<?> fileDelete(@PathVariable("uuid") String uuid) {
        try {
            boolean isDeleted = attachmentService.deleteByUUID(uuid); // 파일 삭제 여부

            return new JSONResponse<>(200, "SUCCESS", isDeleted);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
    }

}
