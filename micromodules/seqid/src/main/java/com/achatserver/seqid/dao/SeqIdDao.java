package com.achatserver.seqid.dao;

import com.achatserver.seqid.pojo.SeqIdSegment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface SeqIdDao extends JpaSpecificationExecutor<SeqIdSegment>, JpaRepository<SeqIdSegment, String> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select t from SeqIdSegment t where t.seqId =?1 ")
    public SeqIdSegment queryBySeqId( long seqId );

    @Modifying
    @Query("update SeqIdSegment t set t.maxSeqId = t.maxSeqId + t.step where t.seqId =?1")
    public int addMaxIdBySeqId(long seqId);
}
