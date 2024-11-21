package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;

public class LoginTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:/Users/Ina/Desktop/chromedriver-win64-131/chromedriver-win64/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-search-engine-choice-screen");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        driver.get("https://sweetshop.netlify.app/login");
    }

    @DataProvider(name = "loginData")
    public Object[][] loginData() throws IOException {
        utils.ExcelUtils.setExcelFile("C:\\Users\\IdeaProjects\\Login_with_Excel\\testdata.xlsx", "testData");

        int columnCount = utils.ExcelUtils.getRowCount();
        Object[][] loginData = new Object[columnCount][2];

        for (int i = 0; i < columnCount; i++) {
            loginData[i][0] = utils.ExcelUtils.getCellData(i, 0); // E-mail address
            loginData[i][1] = utils.ExcelUtils.getCellData(i, 1); // Password
        }
        return loginData;
    }

    @Test(dataProvider = "loginData")
    public void loginTest(String emailAddress, String password) {
        // Locates the fields and button
        WebElement emailField = driver.findElement(By.id("exampleInputEmail"));
        WebElement passwordField = driver.findElement(By.id("exampleInputPassword"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit'].btn.btn-primary"));

        // Inputs and clears e-mail address and password
        emailField.clear();
        emailField.sendKeys(emailAddress);
        passwordField.clear();
        passwordField.sendKeys(password);

        // Clicks login
        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            // Checks for successful login
            WebElement welcomeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='lead' and contains(text(), 'Welcome back')]")));
            Assert.assertTrue(welcomeElement.isDisplayed(), "Login was successful but welcome element not found.");
            System.out.println("Try: Login was successful.");
        } catch (TimeoutException e) {
            // Handles login failure
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.invalid-feedback.invalid-email")));
            Assert.assertTrue(errorMessage.isDisplayed(), "Error message is not displayed for failed login.");
            Assert.assertEquals(errorMessage.getText(), "Please enter a valid email address.", "Error message text does not match.");
            System.out.println("Catch: Login failed. Error message verified.");
        }

        // Resets the state for the next test
        driver.get("https://sweetshop.netlify.app/login");
    }

    public static void timeout(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }
    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
