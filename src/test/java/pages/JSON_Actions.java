package pages;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.skyscreamer.jsonassert.*;
import util.DriverFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by carl.laneave on 10/21/16.
 */
public class JSON_Actions extends DriverFactory {
    public String json;
    static Logger log = LogManager.getLogger();

    public void production_JSON(String urls, String environment, String environment2) throws Exception {
        String sURL = urls;
        String newURL = "https://" + environment2+"." + urls;
        String env = environment;
        URL url = new URL(newURL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestMethod("GET");
        request.setRequestProperty("Accept", "application/json");
        if (request.getResponseCode() != 200) {
            throw new RuntimeException("HTTP error code:" + request.getResponseCode());

        }
        //Scanner to show response from server -- need to write to log file
        Scanner scan = new Scanner(url.openStream());
        String entireResponse = new String();
        while (scan.hasNext())
            entireResponse += scan.nextLine();
        System.out.println("Response: " + entireResponse);
        scan.close();
        JSONObject objProd = new JSONObject(entireResponse);
        //log.info("Production JSON is: "+objProd);
        request.disconnect();
        compare_JSON(objProd, sURL, env, environment2);
    }

    public void compare_JSON(JSONObject objProd, String urls, String env, String environment2) throws Exception {
        String sURL = urls;
        String newURL = "https://" + env+"." + urls;
        URL url = new URL(newURL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestMethod("GET");
        request.setRequestProperty("Accept", "application/json");
        if (request.getResponseCode() != 200) {
            throw new RuntimeException("HTTP error code:" + request.getResponseCode());

        }

        //Scanner to show response from server -- need to write to log file
        Scanner scan = new Scanner(url.openStream());
        String entireResponse = new String();
        while (scan.hasNext())
            entireResponse += scan.nextLine();
        System.out.println("Response: " + entireResponse);
        scan.close();
        JSONObject objTest = new JSONObject(entireResponse);
        //log.info("Dev JSON is: "+objTest);
        request.disconnect();
//        try {
//           JSONCompare.compareJSON(objProd,objTest,JSONCompareMode.NON_EXTENSIBLE);
//           // JSONAssert.assertEquals(sobjProd, sobjTest, JSONCompareMode.NON_EXTENSIBLE);
//        } catch(AssertionError e){
//            log.error("\n error! "+e+"\n");
//            Assert.fail("\n Error: \n"+e);
//
//        }
        JSONCompareResult result = JSONCompare.compareJSON(objProd, objTest, JSONCompareMode.STRICT_ORDER);
        for (FieldComparisonFailure x : result.getFieldFailures())
            log.error("\nField is: " + x.getField() + "\n Environment "+env+": " + x.getExpected() + "\n Environment "+environment2+ ": "+ x.getActual() + "\n");
        JSONAssert.assertEquals(objProd, objTest, JSONCompareMode.STRICT);

    }

    public void fieldVerify_JSON(String value) throws IOException {
        //String sURL = urls;
        String newURL = "https://www.fishersci.com/us/en/catalog/service/search/products?keyword=29442384";
        URL url = new URL(newURL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestMethod("GET");
        request.setRequestProperty("Accept", "application/json");
        if (request.getResponseCode() != 200) {
            throw new RuntimeException("HTTP error code:" + request.getResponseCode());

        }
        //Scanner to show response from server -- need to write to log file
        Scanner scan = new Scanner(url.openStream());
        String entireResponse = new String();
        while (scan.hasNext())
            entireResponse += scan.nextLine();
        //System.out.println("Response: " + entireResponse);
        scan.close();
        JSONObject objProd = new JSONObject(entireResponse);
        //log.info("Production JSON is: "+objProd);
        //Pass this validation in through excel
        Object test = objProd.get("productResults");
        JSONArray arr = objProd.getJSONArray("productResults");
        for (int i = 0; i < arr.length(); i++) {
            //we can pass this validation in through excel
            JSONArray item = arr.getJSONObject(i).getJSONArray("itemCatalogCNo");
            String itemno = item.toString().replaceAll("[\\[\\]\"]", "");
            log.info(itemno.length());
            if(value.equalsIgnoreCase(itemno)){
                log.info("The value pulled from the DB is: "+value);
                log.info("The value found on the JSON file for itemCatalogCNO is: "+itemno);
                log.info("Pass, this works!");
            }
            else{
                log.info("The passed value is: "+value);
                log.info("The value from JSON is: "+itemno);
                Assert.fail("didnt work bud");
            }
        }
        request.disconnect();

    }

}
