/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

//import ru.pentarh.HttpSession;
//import ru.pentarh.HttpSessionException;
import net.namecoin.NameCoinI2PResolver.NameCoinAtom;
import net.namecoin.NameCoinI2PResolver.NameCoinAtomException;
import net.namecoin.NameCoinI2PResolver.NameCoinI2PRecord;
import net.namecoin.NameCoinI2PResolver.NameCoinI2PRecordException;
import net.namecoin.NameCoinI2PResolver.NameCoinRecord;
import net.namecoin.NameCoinI2PResolver.NameCoinRecordException;
import net.namecoin.NameCoinI2PResolver.NameCoinDNSClient;
import net.namecoin.NameCoinI2PResolver.NameCoinDNSClientException;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.Iterator;
/**
 *
 * @author pentarh
 */

public class Main {
    
 
    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        checkNameCoinI2PRecord();
        checkNameCoinAtom();
        checkNameCoinRecord();
        checkDNSClient();
    }

    private static void checkDNSClient() {
        NameCoinDNSClient dns = new NameCoinDNSClient();
        String rec = dns.resolve("pent.bit");
        print(rec);
    }

    private static void checkNameCoinRecord() {
        print("Check NameCoinRecord");
        String a="{\n" +
        "\"name\" : \"d/pent\",\n" +
        "\"value\" : { \"ip\" : \"192.168.1.1\", \"ip6\" : \"2001:4860:0:1001::68\", \"i2p\": { \"destination\": \"s2bzxafMCmDEujaZF44c558MBqYKC95YvoTqNaqMVGIoV68BUlCQu4Z6pjmuxSptHWo38LPx55nEqLLaKFljX7yr30o-iRBwsIev5M1HeX62YVJYxTP5acUxTKaG15dLwKLXRsqeHES8g21vwbz9LsHYsDR9IOYaJFnsBIOfLK4H5GYgcdogazZcuSvVrvX7jdpleGI7g6I81vzuGD0X0NZ~tpj~gypQsomwIZUYknTiaZ37X9TV7DpRbeYkevX7qnSX3odnk007Lhgrt2vN07-DpUHQLmTgr-GbYUVT0Xq-PB5Q2Oi5Rfl1MKQGQ7WXQyVW7MwmQpwgFMMqzQM8SDHqpqrxpsv3VHZ~GIkcKU4rlPazOzxan6PLmvaPigtbKJ0MCHUwD~zHAU~26DuSkRLvquCtxKqfOWE5z8JyaN~zxHivj7X8J7YpibXJdD53RcQ7ozsz9bvOTi0kQItuLR9ROlrxEfReUmWr0VKaCgmV4OvLfyi8~034nj7NAdKeAAAA\", \"name\" : \"example.i2p\", \"b32\" : \"b4szsgxsqgzqg76y3gcxolir54pwjokjjkbane645myukzqrbyoq.b32.i2p\" }, \"map\" : { \"www\": { \"map\" : { \"\": { \"i2p\" : { \"name\" : \"sub.pent.i2p\" } } }, \"ip\" : \"192.168.1.2\", \"ip6\" : \"2001:4860:0:1001::69\", \"i2p\": { \"name\" : \"example.i2p\", \"b32\" : \"b4szsgxsqgzqg76y3gcxolir54pwjokjjkbane645myukzqrbyoq.b32.i2p\" } } } },\n" +
        "\"txid\" : \"f4d4b0849517767b2ef820a513902ee8dde87bd4794a2599198ed4f0b68a36f0\",\n" +
        "\"expires_in\" : 35753" +
        "}\n";
        print("Source JSON: " + a);
        JSONObject b=null;
        try {
            b=new JSONObject(a);
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            return;
        }
        NameCoinRecord c=new NameCoinRecord(b);
        print(c.toString());
    }


    private static void checkNameCoinI2PRecord() {
        print("Check NameCoinI2PRecord");
        String a="{ \"destination\": \"s2bzxafMCmDEujaZF44c558MBqYKC95YvoTqNaqMVGIoV68BUlCQu4Z6pjmuxSptHWo38LPx55nEqLLaKFljX7yr30o-iRBwsIev5M1HeX62YVJYxTP5acUxTKaG15dLwKLXRsqeHES8g21vwbz9LsHYsDR9IOYaJFnsBIOfLK4H5GYgcdogazZcuSvVrvX7jdpleGI7g6I81vzuGD0X0NZ~tpj~gypQsomwIZUYknTiaZ37X9TV7DpRbeYkevX7qnSX3odnk007Lhgrt2vN07-DpUHQLmTgr-GbYUVT0Xq-PB5Q2Oi5Rfl1MKQGQ7WXQyVW7MwmQpwgFMMqzQM8SDHqpqrxpsv3VHZ~GIkcKU4rlPazOzxan6PLmvaPigtbKJ0MCHUwD~zHAU~26DuSkRLvquCtxKqfOWE5z8JyaN~zxHivj7X8J7YpibXJdD53RcQ7ozsz9bvOTi0kQItuLR9ROlrxEfReUmWr0VKaCgmV4OvLfyi8~034nj7NAdKeAAAA\", \"name\" : \"example.i2p\", \"b32\" : \"b4szsgxsqgzqg76y3gcxolir54pwjokjjkbane645myukzqrbyoq.b32.i2p\" }";
        print("Source JSON: " + a);
        JSONObject b=null;
        try {
            b=new JSONObject(a);
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            return;
        }
        NameCoinI2PRecord c=new NameCoinI2PRecord(b);
        print(c.toString());
    }

    private static void checkNameCoinAtom() {
        print("Check NameCoinAtom");
         String a="{ \"ip\" : \"192.168.1.1\", \"ip6\" : \"2001:4860:0:1001::68\", \"i2p\": { \"destination\": \"s2bzxafMCmDEujaZF44c558MBqYKC95YvoTqNaqMVGIoV68BUlCQu4Z6pjmuxSptHWo38LPx55nEqLLaKFljX7yr30o-iRBwsIev5M1HeX62YVJYxTP5acUxTKaG15dLwKLXRsqeHES8g21vwbz9LsHYsDR9IOYaJFnsBIOfLK4H5GYgcdogazZcuSvVrvX7jdpleGI7g6I81vzuGD0X0NZ~tpj~gypQsomwIZUYknTiaZ37X9TV7DpRbeYkevX7qnSX3odnk007Lhgrt2vN07-DpUHQLmTgr-GbYUVT0Xq-PB5Q2Oi5Rfl1MKQGQ7WXQyVW7MwmQpwgFMMqzQM8SDHqpqrxpsv3VHZ~GIkcKU4rlPazOzxan6PLmvaPigtbKJ0MCHUwD~zHAU~26DuSkRLvquCtxKqfOWE5z8JyaN~zxHivj7X8J7YpibXJdD53RcQ7ozsz9bvOTi0kQItuLR9ROlrxEfReUmWr0VKaCgmV4OvLfyi8~034nj7NAdKeAAAA\", \"name\" : \"example.i2p\", \"b32\" : \"b4szsgxsqgzqg76y3gcxolir54pwjokjjkbane645myukzqrbyoq.b32.i2p\" }, \"map\" : { \"www\": { \"ip\" : \"192.168.1.2\", \"ip6\" : \"2001:4860:0:1001::69\", \"i2p\": { \"name\" : \"example.i2p\", \"b32\" : \"b4szsgxsqgzqg76y3gcxolir54pwjokjjkbane645myukzqrbyoq.b32.i2p\" } } } }";
       print("Source JSON: " + a);
        JSONObject b=null;
        try {
            b=new JSONObject(a);
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            return;
        }
        NameCoinAtom c=new NameCoinAtom(b,"test.bit");
        print(c.toString());
    }

    private static void print(String s) {
        System.out.println(s);
    }

}
