/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.namecoin.NameCoinI2PResolver;

/**
 *
 * @author pentarh
 */

public class NameCoinRecordException extends RuntimeException {
    public NameCoinRecordException(String message) {
        super(message);
    }

    public NameCoinRecordException(Throwable ex) {
        super(ex);
    }
}