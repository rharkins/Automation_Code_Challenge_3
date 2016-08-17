package com.stgconsulting;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by Richard Harkins on 7/5/2016.
 */

public class WebPageTest {

    private WebDriver driver;
    static String baseWebPageURL = "http://www.skiutah.com";
    private boolean browserStarted = false;

    public void startBrowser()
    {
        // Firefoxdriver settings
        //
        File pathToBinary = new File("C:\\Users\\Richard Harkins\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
        FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        driver = new FirefoxDriver(ffBinary,firefoxProfile);
        //driver = new FirefoxDriver();

        // Chromedriver settings
        //File file = new File("C:\\ChromeDriver\\chromedriver.exe");
        //System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");
        //driver = new ChromeDriver();

        // Set all new windows to maximize
        driver.manage().window().maximize();
        // Set an implicit wait of 10 seconds to handle delays in loading and finding elements
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }

    private void VerifyPageTitle(String webPageURL, String titleStringToTest)
    {
        // startBrowser
        if (browserStarted == false)
        {
            startBrowser();
            browserStarted = true;
        }
        // Open Webpage URL
        driver.get(webPageURL);
        // Get page title of current page
        String pageTitle = driver.getTitle();
        // Print page title of current page
        System.out.println("Page title of current page is: " + pageTitle);
        // Print title string to test
        System.out.println("Title String to Test is: " + titleStringToTest);
        // Test that the titleStringToTest = title of current page
        Assert.assertTrue(pageTitle.equals(titleStringToTest), "Current Page Title is not equal to the expected page title value");
        // If there is no Assertion Error, Print out that the Current Page Title = Expected Page Title
        System.out.println("Current Page Title = Expected Page Title");

    }

    private void VerifyNavigation(String navigationMenu)
    {
        // Build CSS Selector based on navigation menu user wants to click on
        String cssSelectorText = "a[title='" + navigationMenu + "']";
        // Find menu WebElement based on CSS Selector
        WebElement navigationMenuWebElement = driver.findElement(By.cssSelector(cssSelectorText));
        // Get href attributte from menu WebElement
        String navigationMenuURL = navigationMenuWebElement.getAttribute("href");
        // Navigate to href and validate page title
        VerifyPageTitle(navigationMenuURL, "Ski and Snowboard The Greatest Snow on Earth® - Ski Utah");
    }

    private void SubMenuNavigation(String navigationMenu, String navigationSubMenu) throws InterruptedException {
        // Build CSS Selector based on navigation menu user wants to click on
        String cssSelectorTextNavigationMenu = "a[title='" + navigationMenu + "']";
        // Find menu WebElement based on CSS Selector
        Boolean isPresent = driver.findElements(By.cssSelector(cssSelectorTextNavigationMenu)).size() == 1;
        // Check if navigation menu item exists
        if (isPresent)
        {
            // Get navigation menu WebElement
            WebElement navigationMenuWebElement = driver.findElement(By.cssSelector(cssSelectorTextNavigationMenu));
            // Get href attributte from navigation menu WebElement
            String navigationMenuURL = navigationMenuWebElement.getAttribute("href");
            //Create Actions object
            Actions mouseHover = new Actions(driver);
            // Move to navigation menu WebElement to initiate a hover event
            mouseHover.moveToElement(navigationMenuWebElement).perform();
            //String cssSelectorTextSubMenu = "a[title='" + navigationSubMenu + "']";
            // Build navigation submenu xpath to anchor tag
            String xpathSelectorTextSubmenu = "//a[.='" + navigationSubMenu + "']";
            //WebElement navigationSubMenuWebElement = driver.findElement(By.linkText(navigationSubMenu));
            // Get navigation submenu WebElement
            WebElement navigationSubMenuWebElement = driver.findElement(By.xpath(xpathSelectorTextSubmenu));
            // Check if navigation submenu exists
            Assert.assertTrue(navigationSubMenuWebElement.isEnabled(), (navigationSubMenu + " navigation submenu does not exist on this page"));
            // Click on navigation submenu WebElement
            navigationSubMenuWebElement.click();
            //mouseHover.perform();
            // Navigate to href and validate page title
            //VerifyPageTitle(navigationMenuURL, "Ski and Snowboard The Greatest Snow on Earth® - Ski Utah");
        }
        else
        {
            // Print message indicating that the navigation menu passed in to this method does not exist on the page
            System.out.println(navigationMenu + " navigation menu does not exist on this page");
        }
    }

    @Test
    private void TestLauncher() throws InterruptedException {
        VerifyPageTitle(baseWebPageURL, "Ski Utah - Ski Utah");
        //VerifyNavigation("Deals");
        SubMenuNavigation("Explore", "Stories - Photos - Videos");

    }
}
