/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.namecoin.NameCoinI2PResolver;

/**
 *
 * @author pentarh
 */

public class NameCoinDNSClientException extends RuntimeException {
    public NameCoinDNSClientException(String message) {
        super(message);
    }

    public NameCoinDNSClientException(Throwable ex) {
        super(ex);
    }
}
