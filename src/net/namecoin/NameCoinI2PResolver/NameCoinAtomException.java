/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.namecoin.NameCoinI2PResolver;

/**
 *
 * @author pentarh
 */
public class NameCoinAtomException extends RuntimeException {
    public NameCoinAtomException(String message) {
        super(message);
    }

    public NameCoinAtomException(Throwable ex) {
        super(ex);
    }
}