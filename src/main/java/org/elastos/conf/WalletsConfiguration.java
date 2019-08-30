/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("wallets")
public class WalletsConfiguration {
    private int sum;
    private int renewalValue;
    private int addrNoPerRenewal;
    private int addrNoPerGather;

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getRenewalValue() {
        return renewalValue;
    }

    public void setRenewalValue(int renewalValue) {
        this.renewalValue = renewalValue;
    }

    public int getAddrNoPerRenewal() {
        return addrNoPerRenewal;
    }

    public void setAddrNoPerRenewal(int addrNoPerRenewal) {
        this.addrNoPerRenewal = addrNoPerRenewal;
    }

    public int getAddrNoPerGather() {
        return addrNoPerGather;
    }

    public void setAddrNoPerGather(int addrNoPerGather) {
        this.addrNoPerGather = addrNoPerGather;
    }
}
