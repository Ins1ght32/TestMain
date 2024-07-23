package com.mycompany.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Integration UI test for PHP App.
 */
public class AppTest {
    WebDriver driver;
    WebDriverWait wait;
    String url = "http://192.168.1.188"; // Update this URL to your local server address
    String validQuery = "safe search";
    String invalidQuery = "<script>alert('XSS');</script>";

    @Before
    public void setUp() {
        driver = new HtmlUnitDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testSearchWithValidQuery() throws InterruptedException {
        // Get web page
        driver.get(url);
        // Wait until page is loaded or timeout error
        wait.until(ExpectedConditions.titleContains("Search"));

        // Enter input
        driver.findElement(By.name("query")).sendKeys(validQuery);
        // Click submit
        driver.findElement(By.tagName("button")).click();

        // Check result: Expecting to be on the welcome page
        boolean isWelcomePage = wait.until(ExpectedConditions.titleContains("Welcome"));
        String displayedQuery = driver.findElement(By.tagName("p")).getText();

        assertTrue(isWelcomePage);
        assertTrue(displayedQuery.contains(validQuery));
    }

    @Test
    public void testSearchWithInvalidQuery() throws InterruptedException {
        // Get web page
        driver.get(url);
        // Wait until page is loaded or timeout error
        wait.until(ExpectedConditions.titleContains("Search"));

        // Enter input
        driver.findElement(By.name("query")).sendKeys(invalidQuery);
        // Click submit
        driver.findElement(By.tagName("button")).click();

        // Check result: Expecting to be redirected back to the search page
        boolean isSearchPage = wait.until(ExpectedConditions.titleContains("Search"));

        assertTrue(isSearchPage);
    }
}
