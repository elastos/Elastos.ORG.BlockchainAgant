package org.elastos.DAO;

import org.elastos.DTO.DepositWallet;
import org.elastos.conf.RetCodeConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class walletRepositoryTest {

    @Autowired
    DepositWalletRepository depositWalletRepository;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPersistence() {
        DepositWallet wallet = new DepositWallet();
        wallet.setId(1L);
        wallet.setMaxUse(0);
        wallet.setMnemonic("absent cause hint unfold crumble wink word affair alone stock earth family");
        wallet.setAddress("EZdDnKBRnV8o77gjr1M3mWBLZqLA3WBjB7");
        wallet.setPublicKey("033CAE6DF91E6A77313601FEF25BAB55B0A4362E399377B0B87661AE5A2CE95A81");
        wallet.setPrivateKey("FEDF4265E4B459074754C4A09420A278C7316959A48EA964263E86DECECEF232");

        depositWalletRepository.save(wallet);
        DepositWallet wallet1=depositWalletRepository.findById(wallet.getId()).orElse(null);
        Assert.assertEquals("EZdDnKBRnV8o77gjr1M3mWBLZqLA3WBjB7", wallet1.getAddress());

    }

}