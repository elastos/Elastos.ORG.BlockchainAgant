package org.elastos.DAO;

import org.elastos.DTO.UpChainRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpChainRecordRepository extends CrudRepository<UpChainRecord, Long> {
}
