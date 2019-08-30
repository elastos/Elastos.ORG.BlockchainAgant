package org.elastos.service;

import com.alibaba.fastjson.JSON;
import jnr.ffi.annotations.Synchronized;
import org.elastos.conf.*;
import org.elastos.dao.DepositWalletRepository;
import org.elastos.dao.UpChainRecordRepository;
import org.elastos.dto.DepositWallet;
import org.elastos.dto.UpChainRecord;
import org.elastos.ela.Ela;
import org.elastos.entity.ChainType;
import org.elastos.entity.ReturnMsgEntity;
import org.elastos.exception.ApiInternalException;
import org.elastos.pojo.ElaHdWallet;
import org.elastos.util.ela.DidNodeService;
import org.elastos.util.ela.ElaHdSupport;
import org.elastos.util.ela.ElaTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UpChainWalletsManager implements InitializingBean {
    //Warning: do not change depositIdx!!!
    private final long depositIdx = 1L;
    private static Logger logger = LoggerFactory.getLogger(UpChainWalletsManager.class);
    private List<ElaHdWallet> walletList = new ArrayList<>();
    private DepositWallet depositWallet = null;
    private int index = 0;
    private int sum = 0;

    private boolean taskOnFlag = false;
    private boolean gatherOnFlag = false;

    @Autowired
    WalletsConfiguration walletsConfiguration;

    @Autowired
    NodeConfiguration nodeConfiguration;

    @Autowired
    BasicConfiguration basicConfiguration;

    @Autowired
    DidConfiguration didConfiguration;

    @Autowired
    RetCodeConfiguration retCodeConfiguration;

    @Autowired
    DidNodeService didNodeService;

    @Autowired
    private DepositWalletRepository depositWalletRepository;

    @Autowired
    private UpChainRecordRepository upChainRecordRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    boolean isEmptyWallets() {
        return (null == depositWallet);
    }

    public boolean isTaskOnFlag() {
        return taskOnFlag;
    }

    public void setTaskOnFlag(boolean taskOnFlag) {
        this.taskOnFlag = taskOnFlag;
    }

    public boolean isGatherOnFlag() {
        return gatherOnFlag;
    }

    public void setGatherOnFlag(boolean gatherOnFlag) {
        this.gatherOnFlag = gatherOnFlag;
    }

    void generateUpChainWallets() {
        if (null == depositWallet) {
            logger.info("Err createUpChainWallets before depositWallet initialize");
            System.out.println("Err createUpChainWallets before depositWallet initialize");
            throw new ApiInternalException("Err UpChainWalletsManager createUpChainWallets before depositWallet initialize");
        }
        String mnemonic = depositWallet.getMnemonic();

        walletList.clear();
        sum = walletsConfiguration.getSum();

        //The 1st wallet used to be deposit wallet, so we begin though 2nd.
        int max = sum + 2;
        for (int i = 2; i < max; i++) {
            String wa;
            try {
                wa = ElaHdSupport.generate(mnemonic, i);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Err createDepositWallet ElaHdSupport.generate Exception!!");
                sum = 0;
                walletList.clear();
                return;
            }
            ElaHdWallet wallet = JSON.parseObject(wa, ElaHdWallet.class);
            walletList.add(wallet);
        }
        int maxUse = depositWallet.getMaxUse();
        if (sum > maxUse) {
            depositWallet.setMaxUse(sum);
            this.saveDepositWalletToDb();
        }
    }

    void createDepositWallet() {
        String mnemonic = ElaHdSupport.generateMnemonic();

        //Default: 1st index of wallets is deposit wallet.
        String deposit;
        try {
            deposit = ElaHdSupport.generate(mnemonic, (int) depositIdx);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Err createDepositWallet ElaHdSupport.generate Exception!!");
            System.out.println("Err createDepositWallet ElaHdSupport.generate Exception!!");
            return;
        }
        ElaHdWallet wallet = JSON.parseObject(deposit, ElaHdWallet.class);

        if (null == depositWallet) {
            depositWallet = new DepositWallet();
        }
        depositWallet.setMnemonic(mnemonic);
        depositWallet.wallteToDepoist(wallet);
        depositWallet.setMaxUse(0);
    }

    void saveDepositWalletToDb() {
        if (null != depositWallet) {
            depositWallet.setId(depositIdx);
            depositWalletRepository.save(depositWallet);
        }
    }

    void getDepositWalletFromDb() {
        Optional<DepositWallet> ret = depositWalletRepository.findById(depositIdx);
        if (!ret.isPresent()) {
            logger.info("getDepositWalletFromDb depositWalletRepository.findById no data");
            System.out.println("getDepositWalletFromDb depositWalletRepository.findById no data");
            return;
        }
        depositWallet = ret.get();
    }

    public Double getRestOfDeposit() {
        return getRestOfWallet(depositWallet.getAddress());
    }

    public Double getRestOfWallet(String address) {
        if (null == address) {
            logger.error("getRestOfWalletByAddress address is null");
            System.out.println("getRestOfWalletByAddress address is null");
            return null;
        }

        List<Map> utxoList = didNodeService.getUtxoListByAddr(address);
        if (null == utxoList) {
            return 0.0;
        } else {
            return getRestFromUtxos(utxoList);
        }
    }

    public Double getRestFromUtxos(List<Map> utxoList) {
        double ela = 0.0;

        for (Map<String, Object> utxo : utxoList) {
            ela += Double.valueOf(utxo.get("Value") + "");
        }

        return ela;
    }

    public String getDepositWalletAddress() {
        if (null != depositWallet) {
            return depositWallet.getAddress();
        } else {
            return null;
        }
    }

    @Synchronized
    public ReturnMsgEntity averageRenewalUpChainWallets(Double ela) {
        ChainType chainType = nodeConfiguration.getChainType();

        ElaTransaction transaction = new ElaTransaction(chainType, "UpChainService: Transfer deposit to up chain wallets. ela:" + ela + "wallets sum:" + sum);
        transaction.setRetCodeConfiguration(retCodeConfiguration);
        transaction.setDidConfiguration(didConfiguration);
        transaction.setBasicConfiguration(basicConfiguration);
        transaction.setDidNodeService(didNodeService);

        String sendAddr = Ela.getAddressFromPrivate(depositWallet.getPrivateKey());
        transaction.addSender(sendAddr, depositWallet.getPrivateKey());

        Double fee = ela / sum;
        for (ElaHdWallet wallet : walletList) {
            transaction.addReceiver(wallet.getPublicAddress(), fee);
        }
        ReturnMsgEntity ret;
        try {
            ret = transaction.transfer();
        } catch (Exception e) {
            e.printStackTrace();
            ret = new ReturnMsgEntity().setResult("Err: averageRenewalUpChainWallets transfer failed. Error:" + e.getMessage()).setStatus(retCodeConfiguration.PROCESS_ERROR());
        }

        if (ret.getStatus() == retCodeConfiguration.SUCC()) {
            UpChainRecord upChainRecord = new UpChainRecord();
            upChainRecord.setTxid((String) ret.getResult());
            upChainRecord.setType(UpChainRecord.UpChainType.Deposit_Wallets_Transaction);
            upChainRecordRepository.save(upChainRecord);
        }

        return ret;
    }

    @Synchronized
    public ReturnMsgEntity renewalUpChainWallets() {
        if (!isTaskOnFlag()) {
            return new ReturnMsgEntity().setResult("renewalUpChainWallets stop.").setStatus(retCodeConfiguration.PROCESS_ERROR());
        }

        ElaTransaction transaction = geneElaTransaction("renewalUpChainWallets: Transfer deposit to up chain wallets");


        Double renewalValue = walletsConfiguration.getRenewalValue() * didConfiguration.getFee();
        for (ElaHdWallet wallet : walletList) {
            double fee = renewalValue - wallet.getRest();
            if (fee > 0.0) {
                transaction.addReceiver(wallet.getPublicAddress(), renewalValue);
            }
            //If the worker address is more than 100, we renewal more than one times
            if (transaction.getReceiverList().size() >= walletsConfiguration.getAddrNoPerRenewal()) {
                String sendAddr = Ela.getAddressFromPrivate(depositWallet.getPrivateKey());
                transaction.addSender(sendAddr, depositWallet.getPrivateKey());
                ReturnMsgEntity ret = transferEla(transaction, UpChainRecord.UpChainType.Deposit_Wallets_Transaction);
                transaction = geneElaTransaction("renewalUpChainWallets: Transfer deposit to up chain wallets");
                if (ret.getStatus() == retCodeConfiguration.SUCC()){
                    waitTxFinish((String)ret.getResult(), 3, 3);
                } else {
                    try {
                        TimeUnit.MINUTES.sleep(5);
                    } catch (InterruptedException e) {
                        logger.info("renewalUpChainWallets interrupted.");
                    }
                }
            }
        }

        if (transaction.getReceiverList().isEmpty()) {
            return new ReturnMsgEntity().setResult("renewalUpChainWallets no need to renewal").setStatus(retCodeConfiguration.SUCC());
        }

        String sendAddr = Ela.getAddressFromPrivate(depositWallet.getPrivateKey());
        transaction.addSender(sendAddr, depositWallet.getPrivateKey());
        ReturnMsgEntity ret = transferEla(transaction, UpChainRecord.UpChainType.Deposit_Wallets_Transaction);
        if (ret.getStatus() != retCodeConfiguration.SUCC()) {
            logger.error("Err renewalUpChainWallets transfer failed.");
        }

        return ret;
    }

    private ElaTransaction geneElaTransaction(String memo) {
        ChainType chainType = nodeConfiguration.getChainType();
        ElaTransaction transaction = new ElaTransaction(chainType, memo);
        transaction.setRetCodeConfiguration(retCodeConfiguration);
        transaction.setDidConfiguration(didConfiguration);
        transaction.setBasicConfiguration(basicConfiguration);
        transaction.setDidNodeService(didNodeService);
        return transaction;
    }


    private String waitTxFinish(String txid, int waitSum, int waitTime) {
        for (int i = 0; i < waitSum; i++) {
            try {
                TimeUnit.MINUTES.sleep(waitTime);
            } catch (InterruptedException e) {
                logger.info("waitTxFinish interrupted");
            }
            Object id = didNodeService.getTransaction(txid);
            logger.debug("waitTxFinish ing " + i);
            if (null != id) {
                return txid;
            }
        }
        return null;
    }

    private ReturnMsgEntity transferEla(ElaTransaction transaction, UpChainRecord.UpChainType type) {
        ReturnMsgEntity ret;
        try {
            ret = transaction.transfer();
        } catch (Exception e) {
            e.printStackTrace();
            ret = new ReturnMsgEntity().setResult("Err: transferEla transfer failed. Error:" + e.getMessage()).setStatus(retCodeConfiguration.PROCESS_ERROR());
        }

        if (ret.getStatus() == retCodeConfiguration.SUCC()) {
            UpChainRecord upChainRecord = new UpChainRecord();
            upChainRecord.setTxid((String) ret.getResult());
            upChainRecord.setType(type);
            upChainRecordRepository.save(upChainRecord);
            logger.debug("transferEla txid:" + ret.getResult());
        } else {
            logger.error("Err transferEla transfer failed.");
        }
        return ret;
    }

    @Synchronized
    public ReturnMsgEntity gatherUpChainWallets() {
        if(!isGatherOnFlag()){
            return new ReturnMsgEntity().setResult("gatherUpChainWallets stop.").setStatus(retCodeConfiguration.PROCESS_ERROR());
        }

        ElaTransaction transaction = geneElaTransaction("gatherUpChainWallets: Transfer up chain wallets ela to deposit address.");

        Double value = 0.0;
        for (ElaHdWallet wallet : walletList) {
            Double rest = didNodeService.getBalancesByAddr(wallet.getPublicAddress());
            if (null != rest) {
                wallet.setRest(rest);
            } else {
                rest = 0.0;
            }
            if (rest > didConfiguration.getFee()) {
                transaction.addSender(wallet.getPublicAddress(), wallet.getPrivateKey());
                value += rest;
            }

            if (transaction.getSenderList().size() >= walletsConfiguration.getAddrNoPerGather()) {
                transaction.addReceiver(depositWallet.getAddress(), value - didConfiguration.getFee());
                transferEla(transaction, UpChainRecord.UpChainType.Wallets_Deposit_Transaction);
                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {
                    logger.info("gatherUpChainWallets wait interrupted");
                }

                transaction = geneElaTransaction("gatherUpChainWallets: Transfer up chain wallets ela to deposit address.");
                value = 0.0;
            }
        }

        if (transaction.getSenderList().isEmpty()) {
            setGatherOnFlag(false);
            return new ReturnMsgEntity().setStatus(retCodeConfiguration.SUCC());
        }

        transaction.addReceiver(depositWallet.getAddress(), value - didConfiguration.getFee());

        ReturnMsgEntity ret = transferEla(transaction, UpChainRecord.UpChainType.Wallets_Deposit_Transaction);
        if (ret.getStatus() != retCodeConfiguration.SUCC()) {
            logger.error("Err gatherUpChainWallets transfer failed.");
        }
        setGatherOnFlag(false);
        return ret;
    }

    @Synchronized
    public ElaHdWallet getUpChainWallet() {
        if (0 == sum) {
            return null;
        }

        if (index >= sum) {
            index = 0;
        }
        ElaHdWallet wallet = walletList.get(index);
        index++;
        return wallet;
    }

    @Synchronized
    public double getWalletsRest() {
        double rest = 0.0;
        for (ElaHdWallet w : walletList) {
            rest += w.getRest();
        }
        return rest;
    }

    @Synchronized
    public void updateWalletsRestFromNode() {
        for (ElaHdWallet w : walletList) {
            Double rest = didNodeService.getBalancesByAddr(w.getPublicAddress());
            if (null != rest) {
                w.setRest(rest);
            } else {
                w.setRest(0.0);
            }
        }
    }
}
