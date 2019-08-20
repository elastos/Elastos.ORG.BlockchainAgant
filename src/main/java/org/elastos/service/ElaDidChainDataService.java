package org.elastos.service;

import org.elastos.conf.*;
import org.elastos.dao.UpChainRecordRepository;
import org.elastos.dao.UserServiceRepository;
import org.elastos.dto.UpChainRecord;
import org.elastos.ela.Ela;
import org.elastos.entity.ChainType;
import org.elastos.entity.ReturnMsgEntity;
import org.elastos.pojo.ElaHdWallet;
import org.elastos.util.ela.DidNodeService;
import org.elastos.util.ela.ElaTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class ElaDidChainDataService {

    private static Logger logger = LoggerFactory.getLogger(ElaDidChainDataService.class);

    @Autowired
    NodeConfiguration nodeConfiguration;

    @Autowired
    WalletsConfiguration walletsConfiguration;

    @Autowired
    DidConfiguration didConfiguration;

    @Autowired
    BasicConfiguration basicConfiguration;

    @Autowired
    RetCodeConfiguration retCodeConfiguration;

    @Autowired
    DidNodeService didNodeService;

    @Autowired
    UpChainWalletsManager upChainWalletsManager;

    @Autowired
    private UpChainRecordRepository upChainRecordRepository;

    @Autowired
    UserServiceRepository userServiceRepository;

    private boolean taskOnFlag = true;

    public boolean isTaskOnFlag() {
        return taskOnFlag;
    }

    public void setTaskOnFlag(boolean taskOnFlag) {
        this.taskOnFlag = taskOnFlag;
    }

    //1. 用户获取充值钱包地址。
    public ReturnMsgEntity getDepositAddress() {
        String addr = upChainWalletsManager.getDepositWalletAddress();
        if (null != addr) {
            ReturnMsgEntity ret = new ReturnMsgEntity().setResult(addr).setStatus(retCodeConfiguration.SUCC());
            return ret;
        } else {
            ReturnMsgEntity ret = new ReturnMsgEntity().setResult("Err: The deposit wallet create failed!").setStatus(retCodeConfiguration.INTERNAL_ERROR());
            return ret;
        }
    }

    //2.将充值钱包钱转入上链钱包
    public ReturnMsgEntity renewalUpChainWallets(Double ela) {
        if (null == ela) {
            ReturnMsgEntity ret = new ReturnMsgEntity().setResult("There is no number of ela in parameter").setStatus(retCodeConfiguration.BAD_REQUEST());
            return ret;
        }

        Long lMoney = Math.round(ela * basicConfiguration.ONE_ELA());
        if (lMoney < basicConfiguration.ONE_ELA()) {
            ReturnMsgEntity ret = new ReturnMsgEntity().setResult("There should be at least 10 ela for sending to up chain wallets").setStatus(retCodeConfiguration.BAD_REQUEST());
            return ret;
        }

        Double depositRest = upChainWalletsManager.getRestOfDeposit();//from chain sela
        if (null == depositRest) {
            logger.error("renewalUpChainWallets getRestOfDeposit failed");
            System.out.println("renewalUpChainWallets getRestOfDeposit failed");
            ReturnMsgEntity ret = new ReturnMsgEntity().setResult("Err: The deposit wallet get rest failed!").setStatus(retCodeConfiguration.INTERNAL_ERROR());
            return ret;
        }

        Long lDepositRest = Math.round(depositRest * basicConfiguration.ONE_ELA());
        if (lDepositRest < lMoney) {
            ReturnMsgEntity ret = new ReturnMsgEntity().setResult("There is not enough ela in deposit wallet.").setStatus(retCodeConfiguration.BAD_REQUEST());
            return ret;
        }

        ReturnMsgEntity ret = upChainWalletsManager.averageRenewalUpChainWallets(ela);
        return ret;
    }


    //3. 使用上链钱包进行上链记录
//    public ReturnMsgEntity sendRawDataOnChain(String data, Long userServiceId) {
    public ReturnMsgEntity sendRawDataOnChain(String data) {
        if(!isTaskOnFlag()){
            return new ReturnMsgEntity().setResult("Up chain service is not on").setStatus(retCodeConfiguration.PROCESS_ERROR());
        }
//        int r = userServiceRepository.useRest(userServiceId);
//        if (1 != r) {
//            logger.error("sendRawDataOnChain no rest");
//            System.out.println("sendRawDataOnChain no rest");
//            return new ReturnMsgEntity().setStatus(retCodeConfiguration.BAD_REQUEST()).setResult("用户无此服务");
//        }


        //Pack data to tx for record.
        ChainType chainType = nodeConfiguration.getChainType();

        ElaTransaction transaction = new ElaTransaction(chainType, data);
        transaction.setRetCodeConfiguration(retCodeConfiguration);
        transaction.setDidConfiguration(didConfiguration);
        transaction.setBasicConfiguration(basicConfiguration);
        transaction.setDidNodeService(didNodeService);

        ElaHdWallet wallet = upChainWalletsManager.getUpChainWallet();

        String sendAddr = Ela.getAddressFromPrivate(wallet.getPrivateKey());
        transaction.addSender(sendAddr, wallet.getPrivateKey());
        //Transfer ela to sender itself. the only record payment is miner FEE.
        transaction.addReceiver(sendAddr, didConfiguration.getFee());
        ReturnMsgEntity ret;
        try {
            ret = transaction.transfer();
        } catch (Exception e) {
            e.printStackTrace();
            ret = new ReturnMsgEntity().setResult("Err: sendRawDataOnChain transfer failed. Error:" + e.getMessage()).setStatus(retCodeConfiguration.PROCESS_ERROR());
        }

        if (ret.getStatus() == retCodeConfiguration.SUCC()) {
            UpChainRecord upChainRecord = new UpChainRecord();
            upChainRecord.setTxid((String) ret.getResult());
            upChainRecord.setType(UpChainRecord.UpChainType.Raw_Data_Up_Chain);
            upChainRecordRepository.save(upChainRecord);
        } else {
            //Up chain failed, make the rest back.
//            userServiceRepository.addRest(1L, userServiceId);
            logger.error("Err sendRawDataOnChain upChainData failed. result:" + ret.getResult());
            System.out.println("Err sendRawDataOnChain upChainData failed." + ret.getResult());
        }
        return ret;
    }

    //获取所有余额信息
    public ReturnMsgEntity getRest() {
        Map<String, Double> data = new HashMap<>();
        Double depositRest = upChainWalletsManager.getRestOfDeposit();//from chain sela
        if (null == depositRest) {
            logger.error("ReturnMsgEntity getRestOfDeposit failed");
            System.out.println("ReturnMsgEntity getRestOfDeposit failed");
        } else {
            data.put("DepositRest", depositRest);
        }

        double rest = upChainWalletsManager.getWalletsRest();
        data.put("WorkingWalltesRest", rest);
        ReturnMsgEntity ret = new ReturnMsgEntity().setResult(data).setStatus(retCodeConfiguration.SUCC());
        return ret;
    }

    //
    public ReturnMsgEntity transferEla(String srcPrivateKey, String dstAddr, Double ela) {

        ChainType chainType = nodeConfiguration.getChainType();

        ElaTransaction transaction = new ElaTransaction(chainType, "");
        transaction.setRetCodeConfiguration(retCodeConfiguration);
        transaction.setDidConfiguration(didConfiguration);
        transaction.setBasicConfiguration(basicConfiguration);
        transaction.setDidNodeService(didNodeService);


        String sendAddr = Ela.getAddressFromPrivate(srcPrivateKey);
        transaction.addSender(sendAddr, srcPrivateKey);
        //Transfer ela to sender itself. the only record payment is miner FEE.
        transaction.addReceiver(dstAddr, ela);
        ReturnMsgEntity ret;
        try {
            ret = transaction.transfer();
        } catch (Exception e) {
            e.printStackTrace();
            ret = new ReturnMsgEntity().setResult("Err: sendRawDataOnChain transfer failed. Error:" + e.getMessage()).setStatus(retCodeConfiguration.PROCESS_ERROR());
        }

        return ret;
    }


}
