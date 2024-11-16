package com.cuv.admin.domain.attachment;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import com.cuv.admin.domain.attachment.entity.Attachment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 파일 업로드/다운로드 서비스<br /><br />
 *
 * 필수 properties:<br />
 * {@link MultipartProperties spring.servlet.multipart.location}: 업로드 파일 임시 경로<br />
 * {@link #serviceUrl service-url}: realUrl 출력을 위한 실 도메인 주소
 *
 * @since 2022-08-05
 * @author Tae-rok Hwang
 * @version 1.2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Value("${spring.servlet.multipart.location}")
    private String multipartLocation;

    @Value("${cuv.service-url}")
    private String serviceUrl;

    /**
     * 업로드 허용 확장자, 관리자를 제외한 사용자에게 적용
     */
    private final String[] allowExtensions = new String[]{
            "gif", "jpg", "jpeg", "png", "webp",
            "doc", "docx", "ppt", "pptx", "xls", "xlsx", "pdf", "hwp", "txt"
    };

    /**
     * 단일 파일 업로드 및 DB에 저장
     *
     * @param file multipart/form-data 로 전송된 파일 스트림
     * @param source 업로드 파일의 출처, "editor" 를 넣으면 에디터에서 사용하는 파일로 인식하여 viewYn=Y 로 설정된다.
     * @param request HttpServletRequest
     * @return 업로드 파일 Entity
     * @throws IOException 파일 업로드 실패 시
     * @author Tae-rok Hwang
     */
    public Attachment uploadFile(MultipartFile file, String source, HttpServletRequest request) throws IOException {
        // 랜덤 UUID 생성
        UUID uuid = UUID.randomUUID();
        // 월별로 구분 (예: 2022/08)
        String splitDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        // 원본 파일 이름
        String fileName = file.getOriginalFilename();
        // 원본 파일 확장자
        String fileExtension = this.getExtension(fileName).toLowerCase();
        // 원본 파일을 업로드 할 디렉토리
        File originalDir = new File(multipartLocation + "/" + splitDir);
        // 원본 파일 저장 경로 (확장자 포함)
        File transferFile = new File(multipartLocation + "/" + splitDir
                + "/" + uuid + (!fileExtension.isEmpty() ? "." + fileExtension : ""));

        // 관리자를 제외하고 업로드 파일 확장자 정책 적용
        if (request.getHeader("referer") == null)
            throw new IOException("잘못된 요청입니다.");
        if (!request.getHeader("referer").contains("/admin/")) {
            if (fileExtension.isEmpty())
                throw new IOException("업로드 할 수 없는 파일입니다. (확장자가 없는 파일)");

            if (!Arrays.asList(allowExtensions).contains(fileExtension))
                throw new IOException("업로드 할 수 없는 파일입니다. (허용되지 않는 확장자: " + fileExtension + ")");
        }

        // 원본 파일을 저장할 디렉토리 생성
        if (!originalDir.exists() && !originalDir.mkdirs())
            throw new IOException("업로드 디렉토리를 생성할 수 없습니다.");

        // 업로드 파일 원본 디렉토리로 이동
        file.transferTo(transferFile);

        // Unix, Windows 경로 구분자 '/' 로 통일
        String path = transferFile.getCanonicalPath()
                .replace("\\", "/")
                .replace(multipartLocation.replace("\\", "/"), "");

        // DB Insert
        Attachment attachment = Attachment.builder()
                .uuid(uuid.toString())
                .uploadName(transferFile.getName())
                .originalName(fileName)
                .extension(fileExtension)
                .path(path)
                .realUrl(serviceUrl + "/uploads"+ path)
                .size(file.getSize())
                .source(source)
                .viewYn(YN.ofBool(source != null && source.contains("editor")))
                .build();
        attachmentRepository.save(attachment);

        return attachment;
    }

    /**
     * 다중 파일 업로드 및 DB에 저장<br />
     * 업로드 실패한 파일은 예외 처리되고, 업로드 파일 Entity 리스트에 포함되지 않는다.<br />
     * 요청 List size 와 반환 List size 를 비교하여 업로드 실패한 파일이 있는지 확인할 수 있다.
     *
     * @param files multipart/form-data 로 전송된 파일 스트림 리스트
     * @param source 업로드 파일의 출처, "editor" 를 넣으면 에디터에서 사용하는 파일로 인식하여 viewYn=Y 로 설정된다.
     * @param request HttpServletRequest
     * @return 업로드 파일 Entity 리스트
     * @throws IOException 파일 업로드 실패 시
     * @see #uploadFile(MultipartFile, String, HttpServletRequest)
     * @author Tae-rok Hwang
     */
    public List<Attachment> uploadFiles(List<MultipartFile> files,
                                        String source,
                                        HttpServletRequest request) throws IOException {

        if (files == null || files.isEmpty())
            throw new IOException("업로드 할 파일이 없습니다.");

        List<Attachment> attachments = new ArrayList<>();

        for (MultipartFile file : files)
            try {
                attachments.add(this.uploadFile(file, source, request));
            } catch (IOException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }

        if (attachments.isEmpty()) throw new IOException("파일 업로드에 실패했습니다.");

        return attachments;
    }

    /**
     * UUID 문자열로 DB 에서 업로드 파일 정보를 찾기 (viewYn: N)
     *
     * @param uuid 업로드 파일 UUID
     * @return UploadFile Entity
     * @author Tae-rok Hwang
     */
    @Transactional(readOnly = true)
    public Attachment findByUUID(String uuid) {
        return this.findByUUIDAndViewYn(uuid, YN.N);
    }

    /**
     * UUID 문자열로 DB 에서 업로드 파일 정보를 찾기
     *
     * @param uuid 업로드 파일 UUID
     * @param viewYn viewYn=Y 이면 API 에서 파일을 호출 가능
     * @return UploadFile Entity
     * @author Tae-rok Hwang
     */
    @Transactional
    public Attachment findByUUIDAndViewYn(String uuid, YN viewYn) {
        Attachment attachment = attachmentRepository.findByUUID(uuid).orElse(null);

        // 에디터가 아닌 일반 폼으로 업로드하면 viewYn=N 이므로 ID로 접근(저장 행동)했을 때 viewYn=Y 처리
        if (attachment != null && viewYn == YN.Y)
            attachment.setViewYn(YN.Y);

        return attachment;
    }

    /**
     * UUID 문자열로 DB에 업로드 파일 정보를 저장하기 위한 UploadFileDto 반환 (단일)
     *
     * @param uuid 업로드 파일 UUID
     * @return UploadFileDto (id, uuid, uploadName, originalName, extension, path, realUrl, size, regDate)
     * @author Tae-rok Hwang
     */
    @Transactional
    public AttachmentDto findUploadFileDtoByUUID(String uuid) {
        Attachment attachment = this.findByUUIDAndViewYn(uuid, YN.Y);

        return attachment != null ? attachment.toDto() : null;
    }


    /**
     * UUID 문자열로 DB에 업로드 파일 정보를 저장하기 위한 UploadFileDto 반환 (다중)
     *
     * @param uuids 업로드 파일 UUID ArrayList
     * @return UploadFileDto ArrayList (id, uuid, uploadName, originalName, extension, path, realUrl, size, regDate)
     * @author Tae-rok Hwang
     */
    @Transactional
    public List<AttachmentDto> findAllUploadFileDtoByUUID(List<String> uuids) {
        if (uuids == null || uuids.isEmpty()) return new ArrayList<>();

        return uuids.stream()
                .map(uuid -> {
                    Attachment attachment = this.findByUUIDAndViewYn(uuid, YN.Y);

                    return attachment != null ? attachment.toDto() : null;
                })
                .collect(Collectors.toList());
    }

    /**
     * UUID 문자열로 DB 에서 정보를 조회하여 파일 삭제 및 viewYn=Y<br />
     * 업로드 된 파일을 삭제하지만 DB 에는 rows 를 삭제하지 않고 viewYn=N 으로 변경하여 이력을 남긴다.
     *
     * @param uuid 삭제할 파일의 uuid
     * @return 파일 삭제 성공 여부 (true: 삭제 성공, false: 삭제 실패)
     * @author Tae-rok Hwang
     */
    @Transactional
    public boolean deleteByUUID(String uuid) {
        Attachment attachment = this.findByUUID(uuid);
        if (attachment == null) return false;

        boolean isDeleted = false;
        File file = new File(multipartLocation + attachment.getPath());

        if (file.exists() && file.isFile())
            isDeleted = file.delete();

        attachment.setDelYn(YN.Y);

        return isDeleted;
    }

    /**
     * 파일 확장자 구하기 (FilenameUtils 사용하지 않기 위함)
     *
     * @param fileName 파일 이름
     * @return 파일 확장자, 없으면 빈 문자열 반환
     * @see <a href="https://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java">...</a>
     */
    private String getExtension(String fileName) {
        String extension = "";

        if (fileName == null) return extension;

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p)
            extension = fileName.substring(i + 1);

        return extension;
    }

}
