package org.elastos.dao;

import org.elastos.dto.UserService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface UserServiceRepository extends CrudRepository<UserService, Long> {
    Optional<UserService> findByUserIdAndServiceId(Long userId, Long serviceId);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="UPDATE UserService us SET us.rest=us.rest-1 WHERE us.id=?1 AND us.rest>0")
    int useRest(Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="UPDATE UserService us SET us.rest=us.rest+?1 WHERE us.id=?2")
    int addRest(Long rest, Long id);
}
