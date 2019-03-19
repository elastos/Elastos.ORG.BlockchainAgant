package org.elastos.service;

import org.elastos.dao.ServiceAccessKeyRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.elastos.util.AccessKeyUtil;

@Service
public class InitializingService implements InitializingBean {

    @Autowired
    UpChainWalletsManager upChainWalletsManager;

    @Autowired
    ServiceAccessKeyRepository serviceAccessKeyRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("------------In PlatformInitialization----------------");
        AccessKeyUtil.setServiceAccessKeyRepository(serviceAccessKeyRepository);
        System.out.println("------------Out PlatformInitialization----------------");
        System.out.println("------------In UpChainWalletsManager----------------");
        //Get Deposit
        upChainWalletsManager.getDepositWalletFromDb();
        if (upChainWalletsManager.isEmptyWallets()) {
            upChainWalletsManager.createDepositWallet();
            upChainWalletsManager.saveDepositWalletToDb();
        }

        upChainWalletsManager.generateUpChainWallets();

        System.out.println("------------Out UpChainWalletsManager----------------");
    }

}
