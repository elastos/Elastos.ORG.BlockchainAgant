package org.elastos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class WalltesMonitorTask {

    private static Logger logger = LoggerFactory.getLogger(WalltesMonitorTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    UpChainWalletsManager upChainWalletsManager;

    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void scanWorkerAddressRest() throws Exception {
        logger.debug("扫描开始时间：" + dateFormat.format(new Date()));
        upChainWalletsManager.updateWalletsRestFromNode();
        logger.debug("扫描结束时间：" + dateFormat.format(new Date()));
    }

    @Scheduled(initialDelay = 3 * 60 * 1000, fixedDelay = 10 * 60 * 1000)
    public void renewalWallets() throws Exception {
        logger.debug("充值开始时间：" + dateFormat.format(new Date()));
        upChainWalletsManager.renewalUpChainWallets();
        logger.debug("充值结束时间：" + dateFormat.format(new Date()));
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void gatherWallets() throws Exception {
        logger.debug("归集开始时间：" + dateFormat.format(new Date()));
        upChainWalletsManager.gatherUpChainWallets();
        logger.debug("归集结束时间：" + dateFormat.format(new Date()));
    }

}
