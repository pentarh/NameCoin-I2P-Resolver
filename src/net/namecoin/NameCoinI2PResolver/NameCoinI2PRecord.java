/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.namecoin.NameCoinI2PResolver;

import org.json.JSONObject;
import org.json.JSONException;

/**
 *
 * @author pentarh
 */
public class NameCoinI2PRecord extends BasicObject {
    public String destination;
    public String name;
    public String b32;

    public NameCoinI2PRecord(JSONObject rec) throws NameCoinI2PRecordException {
        try {
            if (rec.has("destination")) {
                this.destination=rec.getString("destination");
            }
            if (rec.has("name")) {
                this.name=rec.getString("name");
            }
            if (rec.has("b32")) {
                this.b32=rec.getString("b32");
            }
        } catch (JSONException e) {
            throw new NameCoinI2PRecordException(e.getMessage());
        }
    }

    public String getDestination() {
        return this.destination;
    }

    public String getName() {
        return this.name;
    }

    public String betB32() {
        return this.b32;
    }

    public String toString() {
        String ret="";
        ret+="<Object NameCoinI2PRecord>\n";
        ret +="\tdestination: " + this.prnProperty(this.destination) + "\n";
        ret +="\tname: " + this.prnProperty(this.name) + "\n";
        ret +="\tb32: " + this.prnProperty(this.b32) + "\n";
        ret+="</Object NameCoinI2PRecord>\n";
        return ret;
    }
}
