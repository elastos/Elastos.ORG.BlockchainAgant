/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.controller;

import com.alibaba.fastjson.JSON;
import org.elastos.annotation.Access;
import org.elastos.entity.ReturnMsgEntity;
import org.elastos.service.ElaDidChainDataService;
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
    private ElaDidChainDataService didChainService;

    @RequestMapping(value = "rest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getRest() {
        ReturnMsgEntity ret = didChainService.getRest();
        return JSON.toJSONString(ret);
    }

    @RequestMapping(value = "deposit/address", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getDepositAddress() {
        ReturnMsgEntity ret = didChainService.getDepositAddress();
        return JSON.toJSONString(ret);
    }

    @PostMapping(value = "wallets/renewal", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String sendRenewalUpChainWallets(@RequestAttribute String reqBody) {
        Map<String, String> map = (Map<String, String>) JSON.parse(reqBody);
        Double ela = Double.valueOf(map.get("ela"));
        ReturnMsgEntity ret = didChainService.renewalUpChainWallets(ela);
        return JSON.toJSONString(ret);
    }

    @RequestMapping(value = "data", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @Access
    public String sendRawDataOnChain(@RequestAttribute String reqBody, @RequestAttribute Long userServiceId) {
        ReturnMsgEntity ret = didChainService.sendRawDataOnChain(reqBody, userServiceId);
        return JSON.toJSONString(ret);
    }

    @RequestMapping(value = "echo", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String echo(@RequestAttribute String reqBody) {
        logger.info("chain agent echo data:" + reqBody);
        return reqBody;
    }
}
