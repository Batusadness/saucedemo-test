package saucedemo;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class sauceLoginDDT {
@Test
    public void login_ddt(){
    WebDriver driver;
    String baseUrl ="https://www.saucedemo.com/";

    WebDriverManager.chromedriver().setup();
    ChromeOptions opt = new ChromeOptions();
    opt.setHeadless(false);

    String fileDri = System.getProperty("user.dir")+"/src/test/data/data-test.csv";

    try (CSVReader reader = new CSVReader(new FileReader(fileDri))){
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null){
            String username = nextLine[0];
            String password = nextLine[1];
            String status = nextLine[2];

            driver = new ChromeDriver(opt);
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.get(baseUrl);
            driver.findElement(By.id("user-name")).sendKeys(username);
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.xpath("//*[@id=\'login-button\']")).click();

            if (status.equals("success")){
                driver.findElement(By.xpath("//*[@id=\'header_container\']/div[2]/span"));
                String judul = driver.findElement(By.xpath("//*[@id=\'header_container\']/div[2]/span")).getText();
                Assert.assertEquals(judul, "Products");
            } else if (status.equals("failed")) {
                String errorFailed = driver.findElement(By.xpath("//*[@id=\'login_button_container\']/div/form/div[3]/h3")).getText();
                Assert.assertEquals(errorFailed, "Epic sadface: Username and password do not match any user in this service");
            }else {
                String errorLock = driver.findElement(By.xpath("//*[@id=\'login_button_container\']/div/form/div[3]/h3")).getText();
                Assert.assertEquals(errorLock,"Epic sadface: Sorry, this user has been locked out.");
            }

            driver.close();
        }
    } catch (CsvValidationException e) {
        throw new RuntimeException(e);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
}
