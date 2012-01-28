/**
 * Copyright 2010 Aleksey Krivosheev (paradoxs.mail@gmail.com)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package net.namecoin.NameCoinI2PResolver;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.DataOutputStream;
import java.io.BufferedReader;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Manages the HTTP machinery for accessing the Bitcoin server.
 * 
 * PLEASE NOTE that it doesn't do https, only http!
 */
public class HttpSession {
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String POST_CONTENT_TYPE = "text/plain";

//    private HttpClient       client = null;
    private String                 uri = null;
    private String              user = null;
    private String             password = null;


    public HttpSession(String uri, String user, String password) {
        this.uri = uri;
        this.user = user;
        this.password = password;
    }

    public HttpSession(String uri) {
        this.uri = uri;
    }

    public String executePost(String postdata) throws HttpSessionException {
        URL url;
        HttpURLConnection connection = null;
        try {
          //Create connection
          url = new URL(this.uri);
          connection = (HttpURLConnection)url.openConnection();
          connection.setRequestMethod("POST");
          connection.setRequestProperty("Content-Type", POST_CONTENT_TYPE);
          connection.setRequestProperty("User-Agent", "java");

          if (!this.user.isEmpty() && !this.password.isEmpty()) {
                String authString = this.user + ":" + this.password;
                String authStringEnc = Base64.encodeBytes(authString.getBytes());
                connection.setRequestProperty("Authorization", "Basic " + authStringEnc);
          }

          connection.setUseCaches (false);
          connection.setDoInput(true);
          connection.setDoOutput(true);

          //Send request
          DataOutputStream wr = new DataOutputStream (
                      connection.getOutputStream ());
          wr.writeBytes (postdata);
          wr.flush ();
          wr.close ();

          //Get Response
          InputStream is = connection.getInputStream();
          BufferedReader rd = new BufferedReader(new InputStreamReader(is));
          String line;
          StringBuffer response = new StringBuffer();
          while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
          }

          rd.close();

          if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
              throw new HttpSessionException("Server returned: " + connection.getResponseMessage());
          }

          return response.toString();

        } catch (Exception e) {
            throw new HttpSessionException(e.getMessage());
        } finally {
          if(connection != null) {
            connection.disconnect();
          }
        }
  }

    public JSONObject sendAndReceive(JSONObject message) throws HttpSessionException {
        String resp = executePost(message.toString());

        try {
            JSONTokener tokener = new JSONTokener(resp);
            Object rawResponseMessage = tokener.nextValue();
            JSONObject response = (JSONObject) rawResponseMessage;

            if (response == null) {
                throw new HttpSessionException("Invalid response type");
            }
            return response;
        } catch (JSONException e) {
            throw new HttpSessionException(e.getMessage());
        } 
    }    

}
