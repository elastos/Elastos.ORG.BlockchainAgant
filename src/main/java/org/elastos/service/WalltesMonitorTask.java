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


    //Send Our data to BChain every 10min
    @Scheduled(fixedRate = 10*60*1000)
    public void afterPropertiesSet() throws Exception {
        System.out.println("扫描开始时间：" + dateFormat.format(new Date()));
        upChainWalletsManager.updateWalletsRestFromNode();
        System.out.println("扫描结束时间：" + dateFormat.format(new Date()));
        upChainWalletsManager.renewalUpChainWallets();
    }

}
