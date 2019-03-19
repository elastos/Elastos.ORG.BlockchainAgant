/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.util.ela;

import com.alibaba.fastjson.JSON;
import org.elastos.conf.NodeConfiguration;
import org.elastos.conf.RetCodeConfiguration;
import org.elastos.entity.ChainType;
import org.elastos.entity.Errors;
import org.elastos.entity.RawTxEntity;
import org.elastos.entity.ReturnMsgEntity;
import org.elastos.exception.ApiRequestDataException;
import org.elastos.util.HttpKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class DidNodeService {

    @Autowired
    NodeConfiguration nodeConfiguration;

    @Autowired
    RetCodeConfiguration retCodeConfiguration;

    public enum ReqMethod {
        GET,
        POST
    }

    public DidNodeService() {

    }

    private Logger logger = LoggerFactory.getLogger(DidNodeService.class);

    /**
     * check address
     *
     * @param address
     */
    private void checkAddr(String address) {
        if (!ElaKit.checkAddress(address)) {
            throw new ApiRequestDataException(Errors.ELA_ADDRESS_INVALID.val() + ":" + address);
        }
    }

    public List<Map> getUtxoListByAddr(String address) {

        checkAddr(address);

        ReturnMsgEntity msgEntity = elaReqChainData(ReqMethod.GET, nodeConfiguration.getUtxoByAddr() + "/" + address, null);
        if (msgEntity.getStatus() == retCodeConfiguration.SUCC()) {
            try {
                List<Map> data = (List<Map>) msgEntity.getResult();
                List<Map> utxoList = (List<Map>) data.get(0).get("Utxo");
                return utxoList;
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.warn(" address has no utxo yet .");
                return null;
            }
        } else {
            return null;
        }
    }

    public ReturnMsgEntity sendRawTransaction(String rawTx, ChainType type) {

        RawTxEntity entity = new RawTxEntity();
        entity.setData(rawTx);
        entity.setType(type);
        entity.setType(ChainType.MAIN_CHAIN);

        String jsonEntity = JSON.toJSONString(entity);
        System.out.println("tx send data:" + jsonEntity);
        ReturnMsgEntity msgEntity = elaReqChainData(ReqMethod.POST, nodeConfiguration.sendRawTransaction(), jsonEntity);
        return msgEntity;
    }

    public Double getBalancesByAddr(String address) {

        checkAddr(address);

        ReturnMsgEntity msgEntity = elaReqChainData(ReqMethod.GET, nodeConfiguration.getBalanceByAddr() + "/" + address, null);
        if (msgEntity.getStatus() == retCodeConfiguration.SUCC()) {
            try {
                String data = (String) msgEntity.getResult();
                Double ela = Double.valueOf(data);
                return ela;
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.warn(" address has no value yet .");
                return null;
            }
        } else {
            return null;
        }
    }

    private ReturnMsgEntity elaReqChainData(ReqMethod method, String url, String data) {
        String response;
        if (ReqMethod.GET == method) {
            String str = url;
            if (null != data) {
                str += data;
            }
            response = HttpKit.get(str);
        } else {
            response = HttpKit.post(url, data);
        }

        Map<String, Object> msg = (Map<String, Object>) JSON.parse(response);
        long status;
        if ((int) msg.get("Error") == 0) {
            status = retCodeConfiguration.SUCC();
        } else {
            status = retCodeConfiguration.PROCESS_ERROR();
        }
        return new ReturnMsgEntity().setResult(msg.get("Result")).setStatus(status);
    }
}
