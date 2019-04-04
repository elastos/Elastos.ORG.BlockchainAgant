package org.elastos.dao;

import org.elastos.dto.ServiceAccessKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
@Transactional
public interface ServiceAccessKeyRepository extends CrudRepository<ServiceAccessKey, Long> {
    Optional<ServiceAccessKey> findByKeyId(String keyId);

    Iterable<ServiceAccessKey> findAllByUserServiceId(Long userServiceId);

    void deleteByKeyId(String keyId);

}
