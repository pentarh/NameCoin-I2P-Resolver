/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.namecoin.NameCoinI2PResolver;

/**
 *
 * @author pentarh
 */

public class NameCoinI2PRecordException extends RuntimeException {
    public NameCoinI2PRecordException(String message) {
        super(message);
    }

    public NameCoinI2PRecordException(Throwable ex) {
        super(ex);
    }
}