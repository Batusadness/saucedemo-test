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
import java.sql.Driver;
import java.util.concurrent.TimeUnit;

public class sauceCheckoutDDT {
    @Test
    public void checkout_ddt() {
        WebDriver driver;
        String baseUrl = "https://www.saucedemo.com/";

        WebDriverManager.chromedriver().setup();
        ChromeOptions opt = new ChromeOptions();
        opt.setHeadless(false);

        String fileDri = System.getProperty("user.dir") + "/src/test/data/Co-data-test.csv";

        try (CSVReader reader = new CSVReader(new FileReader(fileDri))){
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null){
                String username = nextLine[0];
                String password = nextLine[1];
                String status = nextLine[2];
                String First = nextLine[3];
                String last = nextLine[4];
                String postal = nextLine[5];

                driver = new ChromeDriver(opt);
                driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
                driver.manage().window().maximize();
                driver.get(baseUrl);
                driver.findElement(By.id("user-name")).sendKeys(username);
                driver.findElement(By.id("password")).sendKeys(password);
                driver.findElement(By.xpath("//*[@id=\'login-button\']")).click();
                driver.findElement(By.xpath("//*[@id=\'header_container\']/div[2]/span"));
                String judul = driver.findElement(By.xpath("//*[@id=\'header_container\']/div[2]/span")).getText();
                Assert.assertEquals(judul, "Products");
                driver.findElement(By.xpath("//*[@id=\'add-to-cart-sauce-labs-backpack\']")).click();
                driver.findElement(By.xpath("//*[@id=\'add-to-cart-sauce-labs-fleece-jacket\']")).click();
                driver.findElement(By.xpath("//*[@id=\'shopping_cart_container\']/a")).click();
                String JudulCart = driver.findElement(By.xpath("//*[@id=\'header_container\']/div[2]/span")).getText();
                Assert.assertEquals(JudulCart,"Your Cart");
                driver.findElement(By.xpath("//*[@id=\'checkout\']")).click();
                String JudulCheckout = driver.findElement(By.xpath("//*[@id=\'header_container\']/div[2]/span")).getText();
                Assert.assertEquals(JudulCheckout,"Checkout: Your Information");
                driver.findElement(By.id("first-name")).sendKeys(First);
                driver.findElement(By.id("last-name")).sendKeys(last);

                if (status.equals("success")){
                    driver.findElement(By.id("postal-code")).sendKeys("postal");
                    driver.findElement(By.xpath("//*[@id=\'continue\']")).click();
                    String overviews = driver.findElement(By.xpath("//*[@id=\'header_container\']/div[2]/span")).getText();
                    Assert.assertEquals(overviews,"Checkout: Overview");
                    driver.findElement(By.xpath("//*[@id=\"finish\"]")).click();
                    String completedCo = driver.findElement(By.xpath("//*[@id=\'checkout_complete_container\']/h2")).getText();
                    Assert.assertEquals(completedCo,"Thank you for your order!");
                    driver.findElement(By.xpath("//*[@id=\'back-to-products\']")).click();
                }else {
//                    driver.findElement(By.id("postal-code")).sendKeys("");
                    driver.findElement(By.xpath("//*[@id=\'continue\']")).click();
                    String warningText = driver.findElement(By.xpath("//h3[contains(text(),\"Error\")]")).getText();
                    Assert.assertEquals(warningText, "Error: Postal Code is required");
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