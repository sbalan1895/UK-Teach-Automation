package com.pomjobsearch;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ukteachbase.Base_JobSearch;

public class POM_MicroHomePage extends Base_JobSearch{

//	public class POM_MicroHomePage extends Base_JobSearch {

		WebDriverWait waits;
		Actions action;
		JavascriptExecutor js;

		public POM_MicroHomePage(WebDriver driver) {
			this.waits = new WebDriverWait(driver, Duration.ofSeconds(50));
			this.action = new Actions(driver);
			this.js = (JavascriptExecutor) driver;
		}

		private By findJobs = By.xpath("//ul[@class='mainNav']//li//a[contains(text(),'Find Your Job')]");
		private By jobC     = By.xpath("//h2[@class='job-count-heading']");
		private By blogs    = By.xpath("//ul[@class='mainNav']//li//a[contains(text(),'Blogs')]");
		private By aboutUs  = By.xpath("//ul[@class='mainNav']//li//a[contains(text(),'About Us')]");
		private By aboutCont = By.xpath("//div[@class='banner-block__content']//p");

		// ================== Reusable helpers ==================

		private String readText(WebElement el) {
			String text = el.getText().trim();
			if (text.isEmpty()) {
				String attribTxt = el.getAttribute("textContent");
				text = attribTxt == null ? "" : attribTxt.trim().replaceAll("\\s+", " ");
			}
			return text;
		}

		private void waitForPageLoad() {
			waits.until(d -> "complete".equals(js.executeScript("return document.readyState")));
		}

		// Reusable: hover + click a nav link, wait for load
		private void clickNavLink(By locator) {
			WebElement el = waits.until(ExpectedConditions.elementToBeClickable(locator));
			action.moveToElement(el).click().build().perform();
			waitForPageLoad();
		}

		// Reusable: print URL and title for any page
	/*	private void logPageDetails(String pageName) {
			System.out.println(pageName + " page URL is: " + getPageUrl());
			System.out.println(pageName + " page Title is: " + getPageTitle());
		}

		// Only needed here if you did NOT add these to Base_JobSearch
		public String getPageTitle() { return driver.getTitle(); }
		public String getPageUrl()   { return driver.getCurrentUrl(); }
	*/
		// ================== Page methods ==================

		public void findYourJobsPage() {
			clickNavLink(findJobs);

			WebElement jobsC = waits.until(ExpectedConditions.visibilityOfElementLocated(jobC));
			String formatTime = LocalDateTime.now()
					.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

			System.out.println("Total Job count: " + readText(jobsC) + " | CurrentTime: " + formatTime);
			logPageDetails("FindYourJob");
		}

		public void blogsPage() {
			clickNavLink(blogs);
			logPageDetails("Blogs");
		}

		public void aboutUsPage() {
			clickNavLink(aboutUs);
			logPageDetails("AboutUs");
		}
	}
	

