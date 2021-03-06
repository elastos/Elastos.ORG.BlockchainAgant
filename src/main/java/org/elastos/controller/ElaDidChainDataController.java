/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.elastos.annotation.Access;
import org.elastos.conf.RetCodeConfiguration;
import org.elastos.entity.ReturnMsgEntity;
import org.elastos.service.ElaDidChainDataService;
import org.elastos.service.UpChainWalletsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/1/blockagent/upchain")
public class ElaDidChainDataController {
    private static Logger logger = LoggerFactory.getLogger(ElaDidChainDataController.class);

    @Autowired
    RetCodeConfiguration retCodeConfiguration;

    @Autowired
    private ElaDidChainDataService elaDidChainDataService;

    @Autowired
    private UpChainWalletsManager upChainWalletsManager;

    @RequestMapping(value = "rest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getRest() {
        ReturnMsgEntity ret = elaDidChainDataService.getRest();
        return JSON.toJSONString(ret);
    }

    @RequestMapping(value = "deposit/address", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getDepositAddress() {
        ReturnMsgEntity ret = elaDidChainDataService.getDepositAddress();
        return JSON.toJSONString(ret);
    }

    @PostMapping(value = "deposit/gather", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String gatherAllWallet(@RequestAttribute String reqBody) {
        elaDidChainDataService.setServiceOnFlag(false);
        upChainWalletsManager.setTaskOnFlag(false);
        upChainWalletsManager.setGatherOnFlag(true);
        return JSON.toJSONString(new ReturnMsgEntity().setStatus(retCodeConfiguration.SUCC()));
    }

    @PostMapping(value = "switch", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String renewalTaskStop(@RequestAttribute String reqBody) {
        JSONObject obj = JSON.parseObject(reqBody);
        boolean serviceFlag = obj.getBooleanValue("service");
        boolean taskFlag = obj.getBooleanValue("task");
        elaDidChainDataService.setServiceOnFlag(serviceFlag);
        upChainWalletsManager.setTaskOnFlag(taskFlag);
        Boolean gatherFlag = obj.getBoolean("gather");
        if (null == gatherFlag) {
            upChainWalletsManager.setGatherOnFlag(false);
        } else {
            upChainWalletsManager.setGatherOnFlag(gatherFlag);
        }
        return JSON.toJSONString(new ReturnMsgEntity().setStatus(retCodeConfiguration.SUCC()));
    }

    @PostMapping(value = "wallets/renewal", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String sendRenewalUpChainWallets(@RequestAttribute String reqBody) {
        JSONObject map =  JSON.parseObject(reqBody);
        Double ela = map.getDouble("ela");
        ReturnMsgEntity ret = elaDidChainDataService.renewalUpChainWallets(ela);
        return JSON.toJSONString(ret);
    }

    @RequestMapping(value = "data", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @Access
    public String sendRawDataOnChain(@RequestAttribute String reqBody) {
        ReturnMsgEntity ret = elaDidChainDataService.sendRawDataOnChain(reqBody);
        return JSON.toJSONString(ret);
    }

    @RequestMapping(value = "data/dst", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @Access
    public String sendRawDataOnChainAddress(@RequestAttribute String reqBody) {
        JSONObject obj = JSON.parseObject(reqBody);
        String data = obj.getString("data");
        String address = obj.getString("address");

        ReturnMsgEntity ret = elaDidChainDataService.sendRawDataOnChain(data, address);
        return JSON.toJSONString(ret);
    }

    @RequestMapping(value = "echo", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String echo(@RequestAttribute String reqBody) {
        logger.info("chain agent echo data:" + reqBody);
        return reqBody;
    }
}
