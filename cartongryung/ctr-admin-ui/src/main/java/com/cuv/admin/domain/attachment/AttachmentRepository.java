package com.cuv.admin.domain.attachment;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 첨부파일 Repository
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    Optional<Attachment> findTopByUuidAndDelYn(@Param("uuid") String uuid, @Param("delYn") YN delYn);

    default Optional<Attachment> findByUUID(String uuid) {
        return findTopByUuidAndDelYn(uuid, YN.N);
    }

    @Query("SELECT a.realUrl FROM Attachment a WHERE a.uuid = :uuid")
    String findRealUrlByUUID(@Param("uuid") String uuid );

}
