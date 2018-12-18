package org.elastos.service;

import org.elastos.conf.RetCodeConfiguration;
import org.elastos.entity.ReturnMsgEntity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElaDidChainDataServiceTest {

    @Autowired
    ElaDidChainDataService elaDidChainDataService;

    @Autowired
    RetCodeConfiguration retCodeConfiguration;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getDepositAddress() throws Exception {
        ReturnMsgEntity ret = elaDidChainDataService.getDepositAddress();
        Assert.assertEquals(ret.getStatus(), (Long)retCodeConfiguration.BAD_REQUEST());
    }

    @Test
    public void testSendDepositToUpChainWallets_failed() throws Exception {
        ReturnMsgEntity ret = elaDidChainDataService.renewalUpChainWallets(1.0);
        Assert.assertEquals(ret.getStatus(), (Long)retCodeConfiguration.BAD_REQUEST());
        System.out.println(ret.getResult());

        ret = elaDidChainDataService.renewalUpChainWallets(10.0);
        Assert.assertEquals(ret.getStatus(), (Long)retCodeConfiguration.BAD_REQUEST());
        System.out.println(ret.getResult());
    }

    @Test
    public void testSendDepositToUpChainWallets_success() throws Exception {
        ReturnMsgEntity ret;
//        ReturnMsgEntity ret =  elaDidChainDataService.transferEla(
//                "17f9885d36ce7c646cd1d613708e9b375f81b81309fbdfbd922d0cd72faadb1b",
//                "EZdDnKBRnV8o77gjr1M3mWBLZqLA3WBjB7",
//                10.0);
//        Assert.assertEquals(ret.getStatus(), (Long)retCodeConfiguration.SUCC());
        TimeUnit.MINUTES.sleep(4);
                ret = elaDidChainDataService.renewalUpChainWallets(10.0);
        Assert.assertEquals(ret.getStatus(), (Long)retCodeConfiguration.SUCC());
    }

    @Test
    public void sendRawDataOnChain() throws Exception {
    }

    @Test
    public void transferEla() throws Exception {
        ReturnMsgEntity ret =  elaDidChainDataService.transferEla(
                "17f9885d36ce7c646cd1d613708e9b375f81b81309fbdfbd922d0cd72faadb1b",
                "EZdDnKBRnV8o77gjr1M3mWBLZqLA3WBjB7",
                1.0);
        Assert.assertEquals(ret.getStatus(), (Long)retCodeConfiguration.SUCC());
        System.out.println(ret.getResult());
    }

}