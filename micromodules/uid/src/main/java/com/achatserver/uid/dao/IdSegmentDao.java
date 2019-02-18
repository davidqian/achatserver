package com.achatserver.uid.dao;

import com.achatserver.uid.pojo.IdSegment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface IdSegmentDao extends JpaSpecificationExecutor<IdSegment>, JpaRepository<IdSegment, String> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select t from IdSegment t where t.type =?1 ")
    public IdSegment queryByType( String type );

    @Modifying
    @Query("update IdSegment t set t.maxId = t.maxId + t.step where t.type =?1")
    public int addMaxIdByType(String type);
}
