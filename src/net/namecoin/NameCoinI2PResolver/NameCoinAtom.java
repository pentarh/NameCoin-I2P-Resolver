/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.namecoin.NameCoinI2PResolver;
import org.json.JSONObject;
import org.json.JSONException;
/*import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.InetAddress;
import java.net.Inet6Address;
 *
 */

/**
 *
 * @author pentarh
 */
public class NameCoinAtom extends BasicObject {
//    private String ip = null;
//    private String ip6 = null;
    public String name = null;
    private JSONObject i2p_s = null;
    private NameCoinI2PRecord i2p = null;
    private String alias = null;
    private JSONObject map = null;

    public NameCoinAtom(JSONObject rec, String name) throws NameCoinAtomException {
        this.name = name;
        try {

            //if (rec.has("ip")) this.ip=rec.getString("ip");
            if (rec.has("alias")) this.alias=rec.getString("alias");
            if (rec.has("map")) this.map=rec.getJSONObject("map");
            //if (rec.has("ip6")) this.ip6=rec.getString("ip6");
            if (rec.has("i2p")) this.i2p_s=rec.getJSONObject("i2p");
        } catch (JSONException e) {
            throw new NameCoinAtomException(e.getMessage());
        }
        verifyObject();
    }

    public NameCoinI2PRecord getI2P() {
        return this.i2p;
    }

    public JSONObject getMap() {
        return this.map;
    }

    public Boolean hasMap() {
        return this.map != null;
    }

    private void verifyObject() {
        //verifyIp();
        //verifyIp6();
        verifyI2P();
        verifyMap();
    }

    private void verifyMap() {

    }
/*
    private void verifyIp() {
        if (this.ip != null) {
            if (this.ip.length()>0) {
                Pattern p=Pattern.compile("^\\d+{1,3}\\.\\d+{1,3}\\.\\d+{1,3}\\.\\d+{1,3}$");
                Matcher m=p.matcher(this.ip);
                if (!m.find()) {
                    this.ip = null;
                }
            }
        }
    }

    private void verifyIp6() {
        if (this.ip6 != null) {
            if (this.ip6.length()>0) {
                try {
                    InetAddress a=InetAddress.getByName(this.ip6);
                    if (a instanceof Inet6Address) {
                        a=null;
                    } else {
                        this.ip6=null;
                    }
                } catch (Exception e) {
                    this.ip6=null;
                }
            }
        }
    }
 *
 */
    
    private void verifyI2P() {
        if (this.i2p_s == null) return;
        try {
            this.i2p = new NameCoinI2PRecord(this.i2p_s);
        } catch (NameCoinI2PRecordException e) {
            this.i2p=null;
            this.i2p_s=null;            
        }
    }

    public String toString() {
        String ret = "";
        ret+="<Object NameCoinAtom>\n";
        ret +="\tname: " + this.prnProperty(this.name) + "\n";
        ret +="\ti2p_s: " + this.prnProperty(this.i2p_s) + "\n";
        ret +="\ti2p: " + this.prnProperty(this.i2p) + "\n";
        ret +="\talias: " + this.prnProperty(this.alias) + "\n";
        ret +="\tmap: " + this.prnProperty(this.map) + "\n";
        ret+="</Object NameCoinAtom>\n";
        return ret;
    }

    
}
