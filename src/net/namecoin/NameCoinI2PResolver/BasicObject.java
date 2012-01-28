/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.namecoin.NameCoinI2PResolver;

/**
 *
 * @author pentarh
 */
public class BasicObject {
    protected String prnProperty (Object prop) {
        if (prop == null) return "NULL";
        return prop.toString();
    }
}
