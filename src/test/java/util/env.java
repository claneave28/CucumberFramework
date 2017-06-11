package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

/**
 * Created by carl.laneave on 6/14/2016.
 *
 * -Added FocusNewWindow and ParentWindow - 6/17/2016 cl
 * -Added WaitforPageLoad - 6/17/2016 cl
 * -Added Logging for error handling - 6/20/2016 cl
 * -Added ClickAttempt to handle multiple DOM and StaleElement issues on Page  -6/21/2016 cl
 * -Added  Universal find search result to loop through Div tables and find matching values - 6/22/2016 cl
 * -Modified FindSearch to be more robust  - 6/23/2016 cl
 * -Modified ClickAttempt to handle all clicks and added implict waits to function - 06/24/2016 cl
 * -Added nothing, just a test - 8/5/2016 AH
 *
 */
public class env extends DriverFactory {

    static Logger log = LogManager.getLogger();

    public void focusNewWindow() {
        waitForPageLoad();
        // String parentHandle = driver.getWindowHandle();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
        //driver.close();
        // driver.switchTo().window(parentHandle);
    }

    public void focusParentWindow() {
        waitForPageLoad();
        String parentHandle = driver.getWindowHandle();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
        driver.close();
        driver.switchTo().window(parentHandle);
    }

    public void closePopUpWindow() {
        waitForPageLoad();
        driver.close();
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    public void waitForPageLoad() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        //Initially below given if condition will check ready state of page.
        if (js.executeScript("return document.readyState").toString().equals("complete")) {
            System.out.println("Page Is loaded.");
            return;
        }
        //This loop will rotate for 25 times to check If page Is ready after every 1 second.
        //You can replace your value with 25 If you wants to Increase or decrease wait time.
        for (int i = 0; i < 25; i++) {
            try { // need to update and remove
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("Exception error: " + e);
            }
            //To check page ready state.
            if (js.executeScript("return document.readyState").toString().equals("complete")) {
                break;
            }
        }
    }
    public void waitForAjax() {
        new WebDriverWait(driver, 180).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                return (Boolean) js.executeScript("return !!window.jQuery && window.jQuery.active == 0");
            }
        });
    }

    public void clickAttempt (By by) {
        for (int retries = 0;;retries++) {
            try {
                waitForPageLoad();
                driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
                //WebElement element = driver.findElement(by);
                waitVar.until(ExpectedConditions.elementToBeClickable(by));
                driver.findElement(by).click();
                log.info("Success in Element Click");
                waitForPageLoad();
                break;
            } catch (StaleElementReferenceException e) {
                if (retries <= 2) {
                    log.error("Stale Element Reference Exception Retry #"+retries+" Element:"+by);
                    continue;
                } else {
                    throw e;
                }
            } catch (NoSuchElementException e) {
                if (retries <= 2) {
                    log.error("No Such Element Exception Retry #"+retries);
                    continue;
                } else {
                    throw e;
                }
            }
            catch (WebDriverException e) {
                if (retries <= 2) {
                    log.error("Webdriver Exception Retry #"+retries);
                    if (driver instanceof JavascriptExecutor) {
                        WebElement element = driver.findElement(by);
                        ((JavascriptExecutor) driver)
                                .executeScript("arguments[0].scrollIntoView(true);", element);
                    }
                    continue;
                } else {
                    throw e;
                }
            }
        }
    }

    public void FindSearchResult(String searchTxt, String searchBox, String searchResults){
        String sColValue = searchTxt;
        boolean meetCriteria = false;
        WebElement htmltable=driver.findElement(By.xpath("//*[@"+searchBox+"]/div/div/table"));
        List<WebElement> rows=htmltable.findElements(By.tagName("tr"));

        for (int rnum=0;rnum<rows.size();rnum++) {
            List<WebElement>
                    columns=rows.get(rnum).findElements(By.tagName("td"));
                    String sValue = null;
            try {
                sValue = driver.findElement(By.xpath("//*[@" + searchResults + "" + rnum + "']/td[1]")).getText().trim();

            }catch(WebDriverException e){
                log.error("Object is missing: "+e);
                fail("Object not found: No search results ");
            }
            try {
                sValue = driver.findElement(By.xpath("//*[@" + searchResults + "" + rnum + "']/td[1]")).getText().trim();
            }catch(NullPointerException e){
                log.error("Object is null: "+e);
                fail("Object not found: No Search Results");
            }
                if (sValue.equalsIgnoreCase(sColValue)) {
                // If the sValue match with the description, it will initiate one more inner loop for all the columns of 'cnum' row
                    for (int cnum=1;cnum<columns.size();cnum++) {
                        String sRowValue = driver.findElement(By.xpath("//*[@"+searchResults+""+rnum+"']/td["+cnum+"]")).getText().trim();
                        System.out.println("Row Value is: " +sRowValue);
                        By foundSearch = By.xpath("//*[@"+searchResults+""+rnum+"']/td["+cnum+"]");
                        clickAttempt(foundSearch);
                        waitForPageLoad();
                        meetCriteria = true;
                        }
                    break;
                }
        }
                 if(meetCriteria) {
                     log.info("Search item found successfully");
                     System.out.println("Success!");
                 }   else{
                    log.error("No Element Found for Search Criteria!");
                    fail("No Element found matching Search Criteria");
        }
    }
    public void checkAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 1);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
            //exception handling
        }
    }

    public void radioButtonSelector(By radioid){
    WebElement radioBtn = driver.findElement(radioid);
        waitForPageLoad();
        waitVar.until(ExpectedConditions.elementToBeClickable(radioBtn));
        radioBtn.click();
    }

    public void verifyDateCurrent(String effectiveDate){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        if(effectiveDate.equalsIgnoreCase(currentDate)){
            System.out.println("Success");
            log.info("Date Compare Success! Effective Date:" +effectiveDate+ " matches Current Date: "+currentDate);
        }
        else{
            log.info("Date Compare Fail! Effective Date:" +effectiveDate+ " does not match Current Date: "+currentDate);
            fail("Do not match");
        }
    }

    public void verifyDateEndOfYear(String expDate){
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String yDate = dateFormat.format(date);
        String yearDate = "12/31/"+yDate;
        if(expDate.equalsIgnoreCase(yearDate)){
            System.out.println("Success");
            log.info("Date Compare Success! Exp Date:" +expDate+ " matches Current Date: "+yearDate);
        }
        else{
            log.info("Date Compare Fail! Exp Date:" +expDate+ " does not match Current Date: "+yearDate);
            fail("Do not match");
        }
    }
    public void excelWrite(By infoTitle, By info, int rowNum) throws Exception {
        waitForPageLoad();
        String column = driver.findElement(infoTitle).getText();
        String[] columnheadtexts = column.split(":");
        String columnHeader =columnheadtexts[0];
        String excel = driver.findElement(info).getText();
        log.info("column value is: "+column);
        log.info("columnheader value is: "+columnHeader);
        log.info("excel value is: "+excel);
        int row = rowNum;
        if(excel.equalsIgnoreCase(""))
        {
            String excelValue= "N/A";
            writeExcel(columnHeader, row, excelValue);
        }
        else
        {
            String excelValue = excel;
            writeExcel(columnHeader, row, excelValue);
        }
    }

    public void writeExcel (String columnHeader, int row, String excelValue) throws Exception {
        waitForPageLoad();
        File src = new File("src/test/resources/amps.xlsx");
        FileInputStream fis = new FileInputStream(src);
        XSSFWorkbook srcBook = new XSSFWorkbook(fis);
        XSSFSheet sourceSheet = srcBook.getSheetAt(0);
        XSSFRow sourceRow = sourceSheet.getRow(0);


        int numColumn = -1;
        for (int cn = 0; cn < sourceRow.getLastCellNum(); cn++) {
            Cell c = sourceRow.getCell(cn);
            if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
                continue;
            }
            if (c.getCellType() == Cell.CELL_TYPE_STRING) {
                String text = c.getStringCellValue();
                if (columnHeader.equalsIgnoreCase(text)) {
                    numColumn = cn;
                    XSSFRow sourceRow1 = sourceSheet.getRow(row);
                    XSSFCell cellResults = sourceRow1.createCell(numColumn);
                    cellResults.setCellType(cellResults.CELL_TYPE_STRING);
                    cellResults.setCellValue(excelValue);
                    FileOutputStream fos = new FileOutputStream("src/test/resources/amps.xlsx");
                    srcBook.write(fos);
                    fos.close();
                    log.info("The column Number is: " + numColumn + " for: " + columnHeader);
                    break;
                }
            }
        }
        if (numColumn == -1) {
            log.error("Missing column header for: " + columnHeader);
            Assert.fail("Missing Column Header!!");
            throw new Exception("There was no matching columns");
        }
    }
//    public String toAlphabetic(int i) {
//        if( i<0 ) {
//            Assert.fail("Invalid Value!");
//            return "-"+toAlphabetic(-i-1);
//        }
//
//        int quot = i/26;
//        int rem = i%26;
//        char letter = (char)((int)'A' + rem);
//        if( quot == 0 ) {
//            System.out.println(letter);
//            log.info("Letter is: " +letter);
//            String ColumnLetter = Character.toString(letter);
//            //writeExcel(ColumnLetter);
//            return ""+letter;
//
//        } else {
//            Assert.fail("Invalid input!");
//            return toAlphabetic(quot-1) + letter;
//        }
//    }
    }



