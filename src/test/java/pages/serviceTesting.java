package pages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by carl.laneave on 3/8/17.
 * Designed to setup GFS Team with Web Service Testing
 * Log:  Modified to interate through JSON File and pull data
 */
public class serviceTesting {
    String URL1 = "yoururl";
    String orders = "orders";
    String orderNumber = "orderNumber";
    String URL2 = "yoururl";
    String URL3 = "yoururl";
    String URL3b = "yoururl";
    String detailLines = "detailLines";
    String orderDate = "orderDate";

    //Connect to JSON URL API and use iterations to pull required field data
    public void URL1() throws IOException {

        try {
            URL url = new URL(URL1);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            //Create Get Request
            request.setRequestMethod("GET");
            //Set Property for JSON
            request.setRequestProperty("Accept", "application/json");

            //Error Capturing to validate web response
            if (request.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code:" + request.getResponseCode());
            }

            //Scanner to show response from server
            Scanner scan = new Scanner(url.openStream());
            String entireResponse = new String();
            while (scan.hasNext())
                entireResponse += scan.nextLine();
            //Scanner must close out request
            scan.close();

            //Pass response from webserver:  Required to deserialize and deconstruct the JSON File
            JSONObject objProd = new JSONObject(entireResponse);
            //Parse JSON by Parent JSONArray Value
            JSONArray jsonArray = objProd.getJSONArray(orders);

            //Setup Array to pass values
            ArrayList<String> myList = new ArrayList<>();
            // Iterate through JSON File
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject res = objProd.getJSONArray(orders).getJSONObject(i);
                String jsonvalue = res.getString(orderNumber);
                //append each value to array list
                myList.add(jsonvalue);
                //System.out.println("Count is: "+i);
            }
            //System.out.println(myList);
            //Disconnect request
            request.disconnect();
            URL2(myList);
        } catch (MalformedURLException e) {
            System.out.println("URL Exception");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void URL2(ArrayList<String> myList) throws Exception {
        //ArrayList<String> oldmyList = myList;

        for (String element : myList) {
            try {
                System.out.println(element);
                ArrayList<String> oldURL = getURL3(element);
                //System.out.println(element);
                URL url = new URL(URL2 + element);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();
                //Create Get Request
                request.setRequestMethod("GET");
                //Set Property for JSON
                request.setRequestProperty("Accept", "application/json");

                //Error Capturing to validate web response
                if (request.getResponseCode() != 200) {
                    throw new RuntimeException("HTTP error code:" + request.getResponseCode());
                }

                //Scanner to show response from server
                Scanner scan = new Scanner(url.openStream());
                String entireResponse = new String();
                while (scan.hasNext())
                    entireResponse += scan.nextLine();
                //Scanner must close out request
                scan.close();

                //Pass response from webserver:  Required to deserialize and deconstruct the JSON File
                JSONObject objProd = new JSONObject(entireResponse);
                //System.out.println(objProd);
                JSONArray jsonArray = objProd.getJSONArray(orders);

                //Setup Array to pass values
                ArrayList<String> newURL = new ArrayList<>();
                // Iterate through JSON File
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject res = objProd.getJSONArray(orders).getJSONObject(i);
                    String orderDate = res.getString("orderDate");
                    String orderStatus = res.getString("orderStatus");
                    newURL.add("OrderNum:" + element);
                    newURL.add("Order Date: " + orderDate);
                    newURL.add("Order Status: "+orderStatus);
                    JSONArray lineItem = res.getJSONArray("lineItem");
                    //
                    for (int n = 0; n < lineItem.length(); n++) {
                        JSONObject lineObj = lineItem.getJSONObject(n);
                        JSONArray lineStatus = lineObj.getJSONArray("lineStatus");
                        //
                        for (int m = 0; m < lineStatus.length(); m++) {
                            JSONObject lineObject = lineStatus.getJSONObject(m);
                            String lineStatusString = lineObject.getString("status");
                            String lineQuantity = lineObject.getString("quantity");
                            String lineQuantity2 = lineQuantity.replaceAll("\\+", "");
                            String lineQuantity3 = lineQuantity2.replaceAll("\\s", "");
                            //append each value to array list
                            String lineStatusString1 = convertOrderStatus(lineStatusString);
                            newURL.add("LineStatus: " + lineStatusString1);
                            newURL.add("Quantity: " + lineQuantity3);
                        }

                    }


                    //System.out.println("Count is: "+i);
                }
                //Print out different ArrayList
                System.out.println(newURL);
                System.out.println(oldURL);
                newURL.removeAll(oldURL);
                System.out.println("Differences are: " + newURL);
                //Disconnect request
                request.disconnect();
            } catch(JSONException exception){
                //Put failure here in report for missing JSON Object
                System.out.println("Error: Missing JSON Object!!");
                continue;
            }
        }
    }

    private ArrayList<String> getURL3(String element) throws IOException {


        //System.out.println(element);
        URL url = new URL(URL3 + element + URL3b);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        //Create Get Request
        request.setRequestMethod("GET");
        //Set Property for JSON
        request.setRequestProperty("Accept", "application/json");

        //Error Capturing to validate web response
        if (request.getResponseCode() != 200) {
            throw new RuntimeException("HTTP error code:" + request.getResponseCode());
        }

        //Scanner to show response from server
        Scanner scan = new Scanner(url.openStream());
        String entireResponse = new String();
        while (scan.hasNext())
            entireResponse += scan.nextLine();
        //Scanner must close out request
        scan.close();

        //Pass response from webserver:  Required to deserialize and deconstruct the JSON File
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> url3list = new ArrayList<>();
        JSONObject objProd = new JSONObject(entireResponse);
        JSONArray jsonArray = objProd.getJSONArray(orderDate);
        String orderStatus = objProd.getString("orderStatus");


        int len = jsonArray.length();
        if (jsonArray != null) {
            for (int i = 0; i < len; i++) {
                list.add(jsonArray.get(i).toString());
            }
        }

        String year = list.get(0);
        String day = list.get(1);
        String month = list.get(2);
        String correctDate = "0" + day + "/" + month + "/" + year;
        url3list.add("OrderNum:" + element);
        url3list.add("Order Date: " + correctDate);
        url3list.add("Order Status: "+orderStatus);

        JSONArray jsonArray2 = objProd.getJSONArray(detailLines);
        for (int i = 0; i < jsonArray2.length(); i++) {
            try {
                JSONObject res1 = objProd.getJSONArray(detailLines).getJSONObject(i);
                JSONArray lineStatus = res1.getJSONArray("lineStatus");
                for (int n = 0; n < lineStatus.length(); n++) {
                    JSONObject lineObject = lineStatus.getJSONObject(n);
                    int quantity = lineObject.getInt("statusQuantity");
                    String status = lineObject.getString("status");
                    url3list.add("LineStatus: " + status);
                    url3list.add("Quantity: " + quantity);
                }

            } catch (JSONException exception) {
                //Put failure here in report for missing JSON Object
                System.out.println("Error: Missing JSON Object!!");
                continue;
            }
        }
        return (url3list);
        //System.out.println(objProd);
    }

    private String convertOrderStatus(String OrderStatus) {
        String convertedOrderStatus = null;
        if (OrderStatus.equalsIgnoreCase("HLD")) {
            convertedOrderStatus = "ONHOLD";
        }
        if (OrderStatus.equalsIgnoreCase("CAP")) {
            convertedOrderStatus = "In Progress";
        }
        if (OrderStatus.equalsIgnoreCase("PIC")) {
            convertedOrderStatus ="In Progress";
        }
        if (OrderStatus.equalsIgnoreCase("PCC")) {
            convertedOrderStatus ="Shipped";
        }
        if (OrderStatus.equalsIgnoreCase("SH1")) {
            convertedOrderStatus ="In Progress";
        }
        if (OrderStatus.equalsIgnoreCase("SH2")) {
            convertedOrderStatus ="In Progress";
        }
        if (OrderStatus.equalsIgnoreCase("SH3")) {
            convertedOrderStatus ="In Progress";
        }
        if (OrderStatus.equalsIgnoreCase("SHP")) {
            convertedOrderStatus ="Shipped";
        }
        if (OrderStatus.equalsIgnoreCase("SHP - Direct Ship")) {
            convertedOrderStatus ="Shipping from Vendor";
        }
        if (OrderStatus.equalsIgnoreCase("SHN")) {
            convertedOrderStatus ="In progress";
        }
        if (OrderStatus.equalsIgnoreCase("RED")) {
            convertedOrderStatus ="Backordered";
        }
        if (OrderStatus.equalsIgnoreCase("ERR")) {
            convertedOrderStatus ="N/A -Not Displayed";
        }
        if (OrderStatus.equalsIgnoreCase("BOR")) {
            convertedOrderStatus ="Backordered";
        }
        if (OrderStatus.equalsIgnoreCase("BOR - Direct Ship")) {
            convertedOrderStatus ="Direct from Vendor";
        }
        if (OrderStatus.equalsIgnoreCase("RDD")) {
            convertedOrderStatus ="In Progress";
        }
        if (OrderStatus.equalsIgnoreCase("INV")) {
            convertedOrderStatus ="Shipped";
        }
        if (OrderStatus.equalsIgnoreCase("IPD")) {
            convertedOrderStatus ="Shipped";
        }
        if (OrderStatus.equalsIgnoreCase("ICD")) {
            convertedOrderStatus ="Shipped";
        }
        if (OrderStatus.equalsIgnoreCase("IPN")) {
            convertedOrderStatus ="Shipped";
        }
        if (OrderStatus.equalsIgnoreCase("CAN")) {
            convertedOrderStatus ="Cancelled";
        }

        return convertedOrderStatus;
    }

}

