package pages;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.*;
import util.DriverFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by carl.laneave on 10/21/16.
 */
public class JSON_Compare extends DriverFactory {
    public String json;
    static Logger log = LogManager.getLogger();
    String prod = "www.";

    public void production_JSON(String urls, String environment) throws Exception {
        String sURL = urls;
        String newURL = "https://"+prod+urls;
        String env= environment;
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
        compare_JSON(objProd, sURL, env);
    }

    public void compare_JSON(JSONObject objProd, String urls, String env) throws Exception{
        String sURL = urls;
        String newURL = "https://"+env+urls;
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
        JSONCompareResult result = JSONCompare.compareJSON(objProd,objTest,JSONCompareMode.NON_EXTENSIBLE);
        for(FieldComparisonFailure x:result.getFieldFailures())
            log.error("\nField is: "+x.getField()+"\n Expected: "+x.getExpected()+"\n Actual: "+x.getActual()+"\n");
        JSONAssert.assertEquals(objProd,objTest,JSONCompareMode.NON_EXTENSIBLE);

    }

}

