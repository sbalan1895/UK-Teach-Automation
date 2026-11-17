package com.ukteachstepdefinition;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.junit.Assert;

import com.pomjobsearch.POM_JOBSEARCH;
import com.pomjobsearch.POM_MicroHomePage;
import com.pomjobsearch.POM_MicrositeNavig;
import com.pomjobsearch.POM_MicrositeNavig.locInfo;
import com.ukteachbase.Base_JobSearch;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MicrositeNavigation_STEPS extends Base_JobSearch{
	
	POM_MicrositeNavig pom_microNavi;
	POM_MicroHomePage pom_microHome;
	POM_JOBSEARCH pom_uktnavig;
	
	
@Given("the user navigates to the UK-Teach website")
public void the_user_navigates_to_the_uk_teach_website() {
	
	pom_uktnavig= new POM_JOBSEARCH(driver);
	
	pom_uktnavig.navigateToUKTeach();
	pom_uktnavig.enterPassCode("Tal-UKTLive@2026!");
	
	pom_microNavi = new POM_MicrositeNavig(driver);
	
	pom_microNavi.getHeaderMainNavi();
	pom_microNavi.getLocations();
	
	
}

@When("the user selects a county from the Location section")
public void the_user_selects_a_county_from_the_location_section() {

	List<locInfo> pages = pom_microNavi.navigateAllLocation();

    Assert.assertFalse("No locations found", pages.isEmpty());
    Assert.assertEquals("Not all locations were visited", 9, pages.size());   // or compare with getLocations().size()

    for (locInfo p : pages) {
        Assert.assertFalse("Title empty for " + p.getName(), p.getTitle().isBlank());
        Assert.assertFalse("URL empty for " + p.getName(), p.getURL().isBlank());
    }
}

@Then("the respective County Microsite page should open successfully")
public void the_respective_county_microsite_page_should_open_successfully() {

	pom_microHome= new POM_MicroHomePage(driver);
	
	pom_microHome.findYourJobsPage();

}

@When("the user navigates to the Blogs page")
public void the_user_navigates_to_the_blogs_page() {

	pom_microHome.blogsPage();
}

@Then("the Blogs page should be displayed successfully")
public void the_blogs_page_should_be_displayed_successfully() {

	@Nullable
	String currentUrlJ = driver.getCurrentUrl();
	String pageTitleJ = driver.getTitle();
	
	System.out.println("Blogs Page URL is: " + currentUrlJ);
	System.out.println("Blogs pageTitle is: " + pageTitleJ);
}

@When("the user navigates to the About Us page")
public void the_user_navigates_to_the_about_us_page() {
	
	pom_microHome.aboutUsPage();

}

@Then("the About Us page should be displayed successfully")
public void the_about_us_page_should_be_displayed_successfully() {

	@Nullable
	String currentUrlAb = driver.getCurrentUrl();
	String pageTitleAb = driver.getTitle();
	
	System.out.println("About_Us Page URL is: " + currentUrlAb);
	System.out.println("About_Us pageTitle is: " + pageTitleAb);

}

@Then("the user should be able to navigate successfully across all Microsite pages")
public void the_user_should_be_able_to_navigate_successfully_across_all_microsite_pages() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
}

	

}
