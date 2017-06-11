package pages;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import util.DriverFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by carl.laneave on 2/14/2017
 */
public class RefID extends DriverFactory {
    static Logger log = LogManager.getLogger();


    public void loadURL(String urls, String page) throws Exception {
        String sURL = urls;
        String newURL = "YOURURL";
        URL url = new URL(newURL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestMethod("GET");
        request.connect();
        if (request.getResponseCode() != 200) {
            throw new RuntimeException("HTTP error code:" + request.getResponseCode());
        }
        driver.get(newURL);
    }

    public void webList(String id) {
        int count = 0;
        String text = driver.findElement(By.xpath("//html")).getText();
        //while(text.contains("productSAPID:")) {
        //count+=1;
//            System.out.println(text.substring(text.indexOf("productSAPID: ") + 1));
        //  text = text.substring(text.indexOf("productSAPID: ") + "productSAPID: ".length() +7);
        //System.out.println(text.substring(text.indexOf("productSAPID: ") + 7));
//            System.out.println("Item Found");
        //}
        //System.out.println(count);
        String[] parts = text.split("\n");
        //System.out.println(parts.length);
        for (int i = 0; i < parts.length; i++) {
            //System.out.println("parts[i+2]");
           if (parts[i].contains("productSAPID")) {
                //do whatever you want
               String productSAP = parts[i];
               if(productSAP.startsWith("productSAPID")){
                   FileWriter fw = null;
                   PrintWriter pw = null;
                   try {
                       fw = new FileWriter("output.txt", true);
                       pw = new PrintWriter(fw);

                       pw.write(parts[i]+"\n");
                       pw.close();
                       fw.close();
                   } catch (IOException ex) {
                    log.error("failure");
                   }
                   }
                   log.info(parts[i]);
               };
               count+=1;
               // System.out.println(parts[i]);
            }

        /*List<WebElement> elements = driver.findElements(By.xpath("//html"));
        for (int i = 0; i < elements.size(); i++) {
            String title = elements.get(i).getAttribute("font");
            if (title.equals("productSAPID: ")) {
                System.out.println(elements.get(i).getTagName());
            }
        }*/
        }

    public void solutionOne(){
        //Identify the page - What are the contents of the page
//        List<WebElement> elements = driver.findElements(By.xpath());
    }
    //html/body/table[4]/tbody/tr[3]/td[3]/table[4]/tbody/tr[59]/td[1]/font
    public int getProductSAPId(){
        String info;
        By identifier = By.xpath("/html/body/table[4]/tbody/tr/td/font[contains(text(),'productSAPID: ')]/following-sibling::td[1]");
        info = driver.findElement(identifier).getText();
        log.info(info);
        return Integer.parseInt(driver.findElement(identifier).getText());
    }

    public void pagenationIteration() throws InterruptedException {
      waitVar.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.tagName("table")));
        List<WebElement> elements = driver.findElements(By.tagName("font"));
        for (int i = 0; i < elements.size(); i++) {
            String title = elements.get(i).getAttribute("font");
            if (title.equals("Next")) {
                elements.get(i).click();
                //break;
            }
        }

    }
}


