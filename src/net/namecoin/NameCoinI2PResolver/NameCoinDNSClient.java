/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.namecoin.NameCoinI2PResolver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.UUID;
import java.util.Iterator;
import java.util.Properties;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;

//import ru.paradoxs.bitcoin.client.exceptions.BitcoinClientException;

/**
 *
 * @author pentarh
 */
public class NameCoinDNSClient {

    protected String host = "";
    protected String user = "";
    protected String password ="";
    protected int port = 8332;
    protected Properties properties;
    protected Boolean configurationLoaded = false;
    protected Boolean enabled = false;
    protected File _appdir;
    protected final Boolean logging = true;

    protected void logger(String severenity, String mess) {
        if (logging) {
            System.out.println("NameCoinDNSClient["+severenity+"]: " + mess);
        }
    }

    protected void Configuration() {
        properties = new Properties();
        configurationLoaded = false;

        File configFile = new File(_appdir, "namecoin.config");
        if (configFile.exists()) {
            logger("debug","Loading config file <" + configFile.getAbsolutePath() + ">");

            try {
                FileInputStream in=new FileInputStream(configFile);
                properties.load(in);
                in.close();
                configurationLoaded = true;
            } catch (IOException e) {
                logger("error","Error loading configuration file <" + configFile.getAbsolutePath() + ">");
            }
        }
        populateConfig();
    }

    protected void populateConfig() {
        if (configurationLoaded){
            this.host=this.properties.getProperty("host");
            try {
                this.port=new Integer(this.properties.getProperty("port"));
            } catch (NumberFormatException e) {
                logger("error","Invalid port in configuration : " + properties.getProperty("port"));
                return;
            }
            this.user=this.properties.getProperty("user");
            this.password=this.properties.getProperty("password");
            if (properties.getProperty("enabled").equals("1")) {
                if (this.host != null & this.port != 0 && this.user != null && this.password!=null) {
                    enabled=true;
                }
            }
        }
    }

    public NameCoinDNSClient() {
        super();
        logger("debug","Initializing");
        _appdir=new File(System.getProperty("user.dir"));
        Configuration();
        if (!configurationLoaded) {
            logger("warn","Initialized with errors");
        }
        if (!enabled) {
            logger("warn","Disabling .bit name resolve");
        } else {
            logger("debug", "Initialized success");
        }
    }

    public NameCoinDNSClient(Boolean silent) {
        super();
    }

    protected JSONObject createRequest(String functionName, JSONArray parameters) throws JSONException {
        JSONObject request = new JSONObject();
        request.put("jsonrpc", "2.0");
        request.put("id",      UUID.randomUUID().toString());
        request.put("method", functionName);
        request.put("params",  parameters);

        return request;
    }

    protected JSONObject createRequest(String functionName) throws JSONException {
        return createRequest(functionName, new JSONArray());
    }

    protected JSONObject executeRequest(JSONObject request) throws NameCoinDNSClientException {
        HttpSession sess = new HttpSession("http://" + this.host + ":" + this.port + "/",this.user,this.password);
        JSONObject response = null;
        try {
            response = sess.sendAndReceive(request);
        } catch (HttpSessionException e) {
            throw new NameCoinDNSClientException(e.getMessage());
        }
        return response;
    }

    protected JSONObject runDNSRequest(String domain) throws NameCoinDNSClientException {

        domain=domain.substring(0, domain.length()-4);
        domain="d/" + domain;
        JSONArray params = new JSONArray();
        params.put(domain);
        params.put(1);
        JSONObject request = null;
        JSONObject response = null;
        JSONObject result =null;
        JSONArray result_arr = null;
        try {
            request=createRequest("name_scan",params);
        } catch (JSONException e) {
            throw new NameCoinDNSClientException(e.getMessage());
        }
        response = executeRequest(request);
        try {
            result_arr = response.getJSONArray("result");
            result = result_arr.getJSONObject(0);
        } catch (JSONException e) {
            throw new NameCoinDNSClientException(e.getMessage());
        }
        if (result != null) return result;
        throw new NameCoinDNSClientException("Result was null");
    }

    protected NameCoinRecord resolveRoot(String domain) throws NameCoinDNSClientException {
        JSONObject response = runDNSRequest(domain);
        NameCoinRecord rec =null;
        try {
            rec = new NameCoinRecord(response);
        } catch (NameCoinRecordException e) {
            throw new NameCoinDNSClientException(e.getMessage());
        }
        if (rec == null) {
            throw new NameCoinDNSClientException("Error resolving");
        }
        return rec;
    }

    public String resolve(String domain) throws NameCoinDNSClientException {
        if (!enabled) return domain;
        logger("debug","resolve: " + domain);
        domain = domain.toLowerCase();
        if (!domain.endsWith(".bit")) {
            logger("error","resolve error: Invalid domain" + domain);
            throw new NameCoinDNSClientException("Invalid domain: " + domain);
        }

        String[] parts = domain.split("\\.");
        String top = parts[parts.length-2] + "." + parts[parts.length-1];
        NameCoinRecord rec = resolveRoot(top);

        // Root match
        if (parts.length == 2) {
            if (rec.members.size() > 0) {
                NameCoinAtom member = rec.members.get(0);
                String record = getI2PRecord(member);
                if (record != null) {
                    logger("info","resolve(" + domain + ")[1] = " + record);
                    return record;
                }
            } else {
              logger("warn","resolve error: Empty DNS record" + domain);
              throw new NameCoinDNSClientException("Empty DNS record: " + domain);
            }
        }

        Iterator it=rec.members.iterator();
        while (it.hasNext()) {
            NameCoinAtom member =  (NameCoinAtom)it.next();
//            System.out.println("Tree search: " + member.name );
            // Exact tree match
            if (member.name.equals(domain)) {
 //               System.out.println("Tree match");
                String record = getI2PRecord(member);
                if (record != null) {
                   logger("info","resolve(" + domain + ")[2] = " + record);
                    return record;
                }
            }
            // Wildcard tree match
            for (int i=0; i <=parts.length-2 ; i++) {
                String Wildcard="*."+implodeArray(parts,".",i);
//                System.out.println("Wildcard search: " + Wildcard );
                if (member.name.equals(Wildcard)) {
//                    System.out.println("Wildcard match");
                    String record = getI2PRecord(member);
                    if (record != null) {
                        logger("info","resolve(" + domain + ")[3] = " + record);
                        return record;
                    }
                }
            }
        }
        logger("info","resolve error: I2P record not found for" + domain);
        throw new NameCoinDNSClientException("I2P record not found for " + domain);
    }

    public String implodeArray(String[] inputArray, String glueString, int start) {
        String output = "";
        if (inputArray.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(inputArray[start]);
            for (int i=start+1; i<inputArray.length; i++) {
                sb.append(glueString);
                sb.append(inputArray[i]);
            }
            output = sb.toString();
        }
        return output;
    }


    public String getI2PRecord (NameCoinAtom rec) {
        NameCoinI2PRecord i2p = rec.getI2P();
        if (i2p == null) {
            return null;
        }
        if (i2p.destination != null) {
            return i2p.destination;
        }
        if (i2p.b32 != null) {
            return i2p.b32;
        }
        if (i2p.name != null) {
            return i2p.name;
        }
        return null;
    }

}
