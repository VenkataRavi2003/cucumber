package stepDefinitions;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

public class MainStepsForGreenKart {
	
	public WebDriver wbDriver;
	private String greenKartURL = "https://rahulshettyacademy.com/seleniumPractise/#/";
	
	@Given("user is on the Greenkart Landing Page")
	public void user_is_on_the_greenkart_landing_page() {
	    WebDriverManager.chromedriver().setup();
	    wbDriver = new ChromeDriver();
	    wbDriver.get(greenKartURL);
	    wbDriver.manage().window().maximize();
	    wbDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
	}
	@When("user searched for {string} product And selects {string} quantity")
	public void user_searched_for_product_and_selects_quantity(String productName, String quantity) {
	    wbDriver.findElement(By.xpath("//input[@class='search-keyword']")).sendKeys(productName);
	    if(Integer.parseInt(quantity) > 1) {
	    	for(int i=0;i<Integer.parseInt(quantity);++i) {
	    		wbDriver.findElement(By.xpath("//a[@class='increment']")).click();
	    	}
	    }
	    
	}
	@When("clicks on AddToCart button")
	public void clicks_on_add_to_cart_button() {
	   wbDriver.findElement(By.xpath("//button[text()='ADD TO CART']")).click();
	}
	@When("user clicks on cart button")
	public void user_clicks_on_cart_button() {
	    wbDriver.findElement(By.xpath("//a[@class='cart-icon']")).click();
	}
	@When("clicks on Proceed to checkout button")
	public void clicks_on_proceed_to_checkout_button() {
	   wbDriver.findElement(By.xpath("//button[text()='PROCEED TO CHECKOUT']")).click();
	   
	}
	@When("clicks on Place Order Button")
	public void clicks_on_place_order_button() {
	   wbDriver.findElement(By.xpath("//button[text() = 'Place Order']")).click();
	}
	@When("selects the country and Agree to the Terms and Conditions")
	public void selects_the_country_and_agree_to_the_terms_and_conditions() {
	   WebElement drpDown = wbDriver.findElement(By.xpath("//select[@style='width: 200px;']"));
	   Select countryDrpDownSelct = new Select(drpDown);
	   countryDrpDownSelct.selectByVisibleText("India");
	}
	@When("cicks on Proceed button")
	public void cicks_on_proceed_button() {
	    wbDriver.findElement(By.xpath("//input[@class='chkAgree']")).click();
	    wbDriver.findElement(By.xpath("//button[text()='Proceed']")).click();
	}
	@Then("users sees order placed successfully message on screen")
	public void users_sees_order_placed_successfully_message_on_screen() {
	   String msg = wbDriver.findElement(By.xpath("//div[@class='wrapperThree']")).getText();
	   System.out.println(msg);
	}
	@And("quits the browser")
	public void quits_the_browser() {
		wbDriver.quit();
	}
}
