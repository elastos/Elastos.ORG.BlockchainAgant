package org.elastos.dao;

import org.elastos.dto.UpChainRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpChainRecordRepository extends CrudRepository<UpChainRecord, Long> {
}
