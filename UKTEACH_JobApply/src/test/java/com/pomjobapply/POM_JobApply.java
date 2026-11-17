package com.pomjobapply;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.baseJobApply.SchoolLogin_Base;

public class POM_JobApply extends SchoolLogin_Base{

	WebDriverWait waits;
	Actions action;
	
	public static final String UKT_URL = "https://staging-www.ukteach.com/";

	public POM_JobApply(WebDriver driver) {

//		this.driver= driver;
		this.waits= new WebDriverWait(driver, Duration.ofSeconds(30));
		this.action= new Actions(driver);
	}

	private By psCode= By.xpath("//h1[contains(text(),'Enter site passcode')]");
	private By entPsC = By.xpath("//input[@name='passcode']");
	private By clkPsC = By.xpath("//button[@type='submit']");

	private By cookRj = By.xpath("//button[contains(text(),'Reject All')]");


	public void navigateToUKTeach() {
//		driver.get(UKT_URL);
	}

	public void enterPassCode(String value) {

		try {

			WebElement passCodeHeading = waits.until(ExpectedConditions.visibilityOfElementLocated(psCode));

			if(passCodeHeading.getText().contains("Enter site passcode")) {

				WebElement enterCode = waits.until(ExpectedConditions.elementToBeClickable(entPsC));
				enterCode.sendKeys(value);

				WebElement clkSubmit = waits.until(ExpectedConditions.elementToBeClickable(clkPsC));
				clkSubmit.click();

			}
		}
		catch(Exception exc) {

			System.out.println("Passcode page is not displayed, Continuing...,");

		}

	}

	public void handleCookiesPopup() {

		WebElement chPopup = waits.until(ExpectedConditions.visibilityOfElementLocated(cookRj));

		try {
			if(chPopup.isDisplayed()) {

				action.moveToElement(chPopup).click().build().perform();

				System.out.println("Cookies constent banner dismissed using RejectAll");
			}


		}
		catch(StaleElementReferenceException | ElementClickInterceptedException e ) {

			System.out.println("Cookie banner interaction was interrupted: " + e.getClass().getSimpleName());

			throw e;
		} 

	}		


}
