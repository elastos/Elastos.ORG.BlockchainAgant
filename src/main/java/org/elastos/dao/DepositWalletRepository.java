package org.elastos.dao;

import org.elastos.dto.DepositWallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositWalletRepository extends CrudRepository<DepositWallet, Long> {
}
