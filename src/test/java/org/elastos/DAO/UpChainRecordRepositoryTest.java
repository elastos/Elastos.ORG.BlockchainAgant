package org.elastos.DAO;

import org.elastos.dao.UpChainRecordRepository;
import org.elastos.dto.UpChainRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UpChainRecordRepositoryTest {
    @Autowired
    UpChainRecordRepository upChainRecordRepository;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPersistence() {
        UpChainRecord upChainRecord = new UpChainRecord();
        upChainRecord.setType(UpChainRecord.UpChainType.Deposit_Wallets_Transaction);
        upChainRecord.setTxid("47cb08a8dd166ee18c0056856ee7436bbf0a23c6745d1b112835b5f42fa0080f");
        upChainRecordRepository.save(upChainRecord);
    }

}