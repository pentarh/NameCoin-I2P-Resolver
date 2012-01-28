/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.namecoin.NameCoinI2PResolver;

import org.json.JSONObject;
import org.json.JSONException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
/**
 *
 * @author pentarh
 */
public class NameCoinRecord extends BasicObject {
    public String name = null;
    public String txid = null;
    public JSONObject value = null;
    private String value_s = null;
    public int expires_in = 0;
    public List<NameCoinAtom> members = new ArrayList<NameCoinAtom>();


    public NameCoinRecord(JSONObject rec) throws NameCoinRecordException {
       try {
           if (rec.has("expires_in")) this.expires_in=rec.getInt("expires_in");
           if (rec.has("name")) this.name=rec.getString("name");
           if (rec.has("txid")) this.txid=rec.getString("txid");
        } catch (JSONException e) {
            throw new NameCoinRecordException(e.getMessage());
        }
       if (rec.has("value")) {
           // Quoted Value tricks
           try {
               this.value=rec.getJSONObject("value");
           } catch (JSONException e) {
               try {
                   this.value_s=rec.getString("value");
               } catch (JSONException e1) {
                   throw new NameCoinRecordException(e1.getMessage());
               }
           }
           if (this.value == null && this.value_s!=null) {
               this.value_s = this.value_s.replaceAll("\\\"", "\"");
               try {
                   this.value=new JSONObject(this.value_s);
               } catch (JSONException e1) {
                   throw new NameCoinRecordException(e1.getMessage());
               }
           }
       }
       if (!this.name.startsWith("d/")) {
           throw new NameCoinRecordException("Invalid domain name in record: " + this.name);
       }
       if (this.expires_in < 0) {
           throw new NameCoinRecordException("Record is expired: " + this.name + "(" + this.expires_in + ")");
       }
       if (this.value == null) {
           throw new NameCoinRecordException("Value not found for: " + this.name);
       }
       // Fix name to readable format
       this.name=this.name.substring(2);
       this.name += ".bit";

       NameCoinAtom newmember = null;
       try {
           newmember=new NameCoinAtom(this.value, this.name);
           if (newmember != null) this.members.add(newmember);
       } catch (NameCoinAtomException e) {
           throw new NameCoinRecordException("Error parsing Value for " + this.name + ":" + e.getMessage());
       }
       if (newmember.hasMap()) {
           getRecursiveMap(newmember);
       }
    }

    private void getRecursiveMap(NameCoinAtom parentObj) {
        String parentName=parentObj.name;
 
        JSONObject m = parentObj.getMap();
        JSONObject v = null;
        String key[] = m.getNames(m);
        for (int i=0; i < key.length; i++) {
            if (key[i].length() == 0) {
                // Empty does not accepted in name
                continue;
            }
            try {
                v = m.getJSONObject(key[i]);
            } catch (JSONException e) {
                // something wrong with this subdomain
                continue;
            }
            NameCoinAtom newmember = null;
            try {
                newmember = new NameCoinAtom(v, clueName(key[i],parentName));
                if (newmember != null) this.members.add(newmember);
            } catch (NameCoinAtomException e) {
                // something really wrong
                continue;
            }
            if (newmember != null) {
                if (newmember.hasMap()) {
                    getRecursiveMap(newmember);
                }
            }
        }
    }

    private String clueName(String child, String parent) {
        if (child.endsWith(".@")) {
            child = child.substring(0, child.length()-3);
            parent=this.name;
        }
        return child + "." + parent;
    }

    public String toString() {
        String ret="";
        ret +="<Object NameCoinRecord>\n";
        ret +="\tname: " + this.prnProperty(this.name) + "\n";
        ret +="\ttxid: " + this.prnProperty(this.txid) + "\n";
        ret +="\tvalue: " + this.prnProperty(this.value) + "\n";
        ret +="\texpires_in: " + this.prnProperty(this.expires_in) + "\n";
        if (this.members.size() > 0) {
            ret +="\t<Members (" + this.members.size() + ")>\n";
            Iterator it = this.members.iterator();
            int cnt = 0;
            while (it.hasNext()) {
                ret += "\t\t<NameCoinRecord.members[" + cnt + "]>\n";
                NameCoinAtom m = (NameCoinAtom)it.next();
                ret += this.prnProperty(m) + "\n";
                ret += "\t\t</NameCoinRecord.members[" + cnt + "]>\n";
                cnt++;
            }
            ret +="\t</Members>\n";
        } else {
            ret +="Members (0)\n";
        }
        ret +="</Object NameCoinRecord>\n";
        return ret;
    }
}
