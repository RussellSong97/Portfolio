package com.cuv.domain.attachment;

import com.cuv.common.YN;
import com.cuv.domain.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    Optional<Attachment> findTopByUuidAndDelYn(@Param("uuid") String uuid, @Param("delYn") YN delYn);

    default Optional<Attachment> findByUUID(String uuid) {
        return findTopByUuidAndDelYn(uuid, YN.N);
    }

}
