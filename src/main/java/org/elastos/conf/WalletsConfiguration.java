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
    private int threshold;
    private int sumPerGather;

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getSumPerGather() {
        return sumPerGather;
    }

    public void setSumPerGather(int sumPerGather) {
        this.sumPerGather = sumPerGather;
    }
}
