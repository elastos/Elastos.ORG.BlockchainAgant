package org.elastos.DAO;

import org.elastos.DTO.DepositWallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositWalletRepository extends CrudRepository<DepositWallet, Long> {
}
