/**
 * Copyright (c) 2017-2018 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.elastos.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * clark
 *
 * 9/3/18
 *
 */
@Component
@ConfigurationProperties("basic")
public class BasicConfiguration {
    private long ONE_ELA;
    private double FEE;
    private double CROSS_CHAIN_FEE;

    public double CROSS_CHAIN_FEE(){
        return CROSS_CHAIN_FEE;
    }

    public void setCrossChain_FEE(double CROSS_CHAIN_FEE) {
        this.CROSS_CHAIN_FEE = CROSS_CHAIN_FEE;
    }

    public long ONE_ELA() {
        return ONE_ELA;

    }

    public void setONE_ELA(long ONE_ELA) {
        this.ONE_ELA = ONE_ELA;
    }

    public double FEE() {
        return FEE;
    }

    public void setFEE(double FEE) {
        this.FEE = FEE;
    }
}
