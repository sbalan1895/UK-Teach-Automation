package com.baseJobApply;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SchoolLogin_Base {
	
	WebDriver driver;
	WebDriverWait waits;
	
	public void navigateUKT() {

		WebDriverManager.chromedriver().setup();
		driver= new ChromeDriver();
		driver.manage().window().maximize();
		waits= new WebDriverWait(driver, Duration.ofSeconds(30));

	}

}
