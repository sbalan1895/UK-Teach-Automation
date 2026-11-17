package com.pomjobsearch;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ukteachbase.Base_JobSearch;

public class POM_MicrositeNavig extends Base_JobSearch {
	
	WebDriverWait waits;
	Actions action;
	JavascriptExecutor js;

	public POM_MicrositeNavig(WebDriver driver) {
		
//		this.driver= driver;
		this.waits = new WebDriverWait(driver, Duration.ofSeconds(50));
		this.action = new Actions(driver);
		this.js = (JavascriptExecutor) driver;

	}

	private By mainNv= By.xpath("//ul[@class='mainNav']");
	private By locations = By.xpath("//ul[@class='mainNav']/li/a[normalize-space()='Locations']");
	private By subMLocs  = By.xpath("//ul[@class='mainNav']/li[a[normalize-space()='Locations']]//ul//a");
	private By uktHome1 = By.xpath("//ul[@class='mainNav']//li/a[contains(text(),'UK Teach Home')]");

	private By uktHome = By.xpath(
		    "//a[normalize-space()='UK Teach Home' or @title='UK Teach Home' "
		  + "or @aria-label='UK Teach Home' or .//img[@alt='UK Teach Home']]");
	//========= Data holder ===========

/*	public static class PageInfo {
	    private final String name, url, title;
	    public PageInfo(String name, String url, String title) {
	        this.name = name; this.url = url; this.title = title;
	    }
	    public String getName()  { return name; }
	    public String getUrl()   { return url; }
	    public String getTitle() { return title; }
	    @Override public String toString() { return name + " -> " + url + " | " + title; }
	} */
	
	
	public static class locInfo {

		private final String name;
		private final String title;
		private final String url;

		public locInfo(String name, String title, String url ) {

			this.name = name;
			this.title = title;
			this.url = url;

		}

		public String getName() {
			return name;
		}
		public String getTitle() {
			return title;
		}
		public String getURL() {
			return url;
		}

		public String toString() {
			return name + " --> " + title + " | " + url;

		}

	}

	//========= helper =============

	private String readText(WebElement el) {

		String text = el.getText().trim();

		if(text.isEmpty()) {

			String attribTxt = el.getAttribute("textContent");
			text = attribTxt == null ? "" : attribTxt.trim().replaceAll("\\s+", " ");
		}

		return text;

	}

	private void safeClick(WebElement el) {

		try {
			if(el.isDisplayed()) {

				waits.until(ExpectedConditions.elementToBeClickable(el)).click();

			}
			else {
				js.executeScript("arguments[0].click();", el);
			}

		}
		catch (Exception e) {
			js.executeScript("arguments[0].click();", el);
		}
	}

	private void waitForPageLoad() {
		waits.until(d -> "complete".equals(js.executeScript("return document.readyState")));
	}

	private List<WebElement> openLocationMenu() {
		WebElement locaT = waits.until(ExpectedConditions.visibilityOfElementLocated(locations));
		action.moveToElement(locaT).build().perform();

		return waits.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(subMLocs));

	}

	//======================================

	public List<String> getHeaderMainNavi() {

		WebElement mainN = waits.until(ExpectedConditions.presenceOfElementLocated(mainNv));

		List<WebElement> naviLinks = mainN.findElements(By.xpath("./li/a"));

		List<String> navOptions = new ArrayList<>();

		for(WebElement headerLinks:  naviLinks ) {

			String headerText = readText(headerLinks);

			if(!headerText.isBlank()) {

				navOptions.add(headerText);
				System.out.println(headerText);
			}

		}
		return navOptions;

	}

	public List<String> getLocations() {

		List<WebElement> locSubM= openLocationMenu();

		List<String> getLocSM = new ArrayList<String>();

		for(WebElement locli : locSubM) {

			String locationli = readText(locli);

			if(!locationli.isBlank()) {
				getLocSM.add(locationli);
				System.out.println(locationli);
			}

		}
		return getLocSM;

	}
	
	//=========================
	
	// Locator for the home link: broader than before, outside ul.mainNav too
	

	// Returns true once the main-site "Locations" menu is visible again
	private boolean isHomeVisible(int seconds) {
	    try {
	        new WebDriverWait(driver, Duration.ofSeconds(seconds))
	            .until(ExpectedConditions.visibilityOfElementLocated(locations));
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}

	// Try: UK Teach Home link -> browser back -> load home URL directly
	private void returnToHome(String homeUrl) {

	    try {
	        WebElement home = new WebDriverWait(driver, Duration.ofSeconds(5))
	                .until(ExpectedConditions.presenceOfElementLocated(uktHome));
	        safeClick(home);
	        waitForPageLoad();
	        if (isHomeVisible(10)) return;
	    } catch (Exception e) {
	        System.out.println("UK Teach Home link not usable, trying browser back");
	    }

	    driver.navigate().back();
	    waitForPageLoad();
	    if (isHomeVisible(10)) return;

	    System.out.println("Back did not reach home, loading home URL directly");
	    driver.get(homeUrl);
	    waitForPageLoad();
	    waits.until(ExpectedConditions.visibilityOfElementLocated(locations));
	}

	public List<locInfo> navigateAllLocation() {

	    List<locInfo> results = new ArrayList<>();

	    String homeUrl = driver.getCurrentUrl();
	    String parentWindow = driver.getWindowHandle();

	    int locationCount = openLocationMenu().size();
	    System.out.println("Total location count is " + locationCount);

	    for (int i = 0; i < locationCount; i++) {

	        try {
	            // fresh elements every iteration to avoid stale references
	            WebElement item = openLocationMenu().get(i);
	            String name = readText(item);
	            Set<String> handlesBefore = driver.getWindowHandles();
	            String beforeUrl = driver.getCurrentUrl();

	            System.out.println("Clicking [" + (i + 1) + "/" + locationCount + "]: " + name);
	            safeClick(item);

	            // wait for a new tab OR a URL change
	            waits.until(d -> d.getWindowHandles().size() > handlesBefore.size()
	                          || !d.getCurrentUrl().equals(beforeUrl));

	            boolean newTab = driver.getWindowHandles().size() > handlesBefore.size();

	            if (newTab) {
	                for (String h : driver.getWindowHandles()) {
	                    if (!handlesBefore.contains(h)) {
	                        driver.switchTo().window(h);
	                        break;
	                    }
	                }
	            }

	            waitForPageLoad();

	            locInfo info = new locInfo(name, getPageTitle(), getPageUrl());
	            System.out.println(info);
	            results.add(info);

	            if (newTab) {
	                driver.close();
	                driver.switchTo().window(parentWindow);
	            } else {
	                returnToHome(homeUrl);
	            }

	        } catch (Exception e) {
	            // one bad county must not stop the rest
	            System.out.println("Location index " + i + " failed: " + e.getMessage());

	            // recover: close extra tabs, go back to the parent window and home
	            for (String h : driver.getWindowHandles()) {
	                if (!h.equals(parentWindow)) {
	                    driver.switchTo().window(h);
	                    driver.close();
	                }
	            }
	            driver.switchTo().window(parentWindow);
	            if (!isHomeVisible(3)) {
	                driver.get(homeUrl);
	                waitForPageLoad();
	            }
	        }

	        // always make sure we are on home before the next county
	        waits.until(ExpectedConditions.visibilityOfElementLocated(locations));
	    }

	    System.out.println("Visited " + results.size() + " of " + locationCount + " locations");
	    return results;
	}

	
	//=========================

/*	public List<locInfo> navigateAllLocation(){

		List<locInfo> results = new ArrayList<>();

		int locationCount = openLocationMenu().size();

		System.out.println("Total location count is " + locationCount);

		for(int i=0; i < locationCount; i++) {

			WebElement itemLocat = openLocationMenu().get(i);
			String text = readText(itemLocat);
			String beforeURL =  driver.getCurrentUrl();

			safeClick(itemLocat);

			waits.until(d -> !d.getCurrentUrl().equals(beforeURL));
			waitForPageLoad();

			locInfo locInfo = new locInfo(text, driver.getCurrentUrl(), driver.getTitle());
			System.out.println(locInfo);
			results.add(locInfo);

			try {
				WebElement home = waits.until(ExpectedConditions.presenceOfElementLocated(uktHome));
				safeClick(home);
			} catch (Exception e) {
				driver.navigate().back();
			}

			waits.until(ExpectedConditions.visibilityOfElementLocated(locations));
			waitForPageLoad();
		}


		return results;

	}
*/
}
