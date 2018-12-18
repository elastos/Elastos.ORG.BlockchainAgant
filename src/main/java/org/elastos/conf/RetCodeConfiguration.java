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
@ConfigurationProperties("retcode")
public class RetCodeConfiguration {

    private long SUCC             ;
    private long BAD_REQUEST      ;
    private long NOT_FOUND        ;
    private long INTERNAL_ERROR   ;
    private long PROCESS_ERROR    ;

    public long PROCESS_ERROR() {
        return PROCESS_ERROR;
    }
    public void setPROCESS_ERROR(long PROCESS_ERROR) {
        this.PROCESS_ERROR = PROCESS_ERROR;
    }
    public long SUCC() {
        return SUCC;
    }

    public void setSUCC(long SUCC) {
        this.SUCC = SUCC;
    }

    public long BAD_REQUEST() {
        return BAD_REQUEST;
    }

    public void setBAD_REQUEST(long BAD_REQUEST) {
        this.BAD_REQUEST = BAD_REQUEST;
    }

    public long NOT_FOUND() {
        return NOT_FOUND;
    }

    public void setNOT_FOUND(long NOT_FOUND) {
        this.NOT_FOUND = NOT_FOUND;
    }

    public long INTERNAL_ERROR() {
        return INTERNAL_ERROR;
    }

    public void setINTERNAL_ERROR(long INTERNAL_ERROR) {
        this.INTERNAL_ERROR = INTERNAL_ERROR;
    }
}
