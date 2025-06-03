package Utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import base.BaseClass;
import base.Constants;
import base.ConfigReaderUtility;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

//import com.sun.java.swing.plaf.windows.resources.windows;

public class UtilFile {
	public static Logger log = LogManager.getLogger(UtilFile.class);
	WebDriver driver;
	WebDriver getNewDriver;
	String[] arrayOfFileNameForExternalUser;
	String[] arrayOfFileNameForInternalUser;
	SoftAssert softAssert = new SoftAssert();

	public UtilFile(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	
	public WebElement highLight(final By elementLocator) throws InterruptedException {
		final WebElement elem = driver.findElement(elementLocator);
		final JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", elem, " border: 3px solid purple;");
		Thread.sleep(50);
		// js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
		// elem, "");
		return elem;
	}

	
	
	public void rightclickOpenWindow(WebElement elementLocator) {
		Actions actions = new Actions(driver);
		actions.contextClick(elementLocator).sendKeys(Keys.CONTROL).click(elementLocator).perform();
		pause(1000);
	}

	public void rightclickOpenWindows(WebElement elementLocator) {
		Actions act = new Actions(driver);
		act.contextClick(elementLocator).perform(); // right click
		act.sendKeys(Keys.ARROW_DOWN).click(elementLocator).perform();
	}

	public void doubleclick(WebElement elementLocator) {
		Actions actions = new Actions(driver);
		actions.doubleClick(elementLocator).perform();
	}

	public void rightclick(WebElement elementLocator) {
		Actions actions = new Actions(driver);
		actions.contextClick(elementLocator).perform();
	}

	public void datepick() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy");
		LocalDateTime now = LocalDateTime.now();
		String d = dtf.format(now);
		System.out.println(d);
	}

	// Get The Current Day
	public String getCurrentDay() {
		// Create a Calendar Object
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

		// Get Current Day as a number
		int todayInt = calendar.get(Calendar.DAY_OF_MONTH);

		// Integer to String Conversion
		String todayStr = Integer.toString(todayInt);
		System.out.println("Today Str: " + todayStr + "\n");

		return todayStr;
	}

	public String getNextYear() {
		Calendar now = Calendar.getInstance();
		String year = String.valueOf(now.get(Calendar.YEAR));

		Integer x = Integer.valueOf(year);
		int y = x + 1;

		String NextYear = Integer.toString(y);
		return NextYear;
	}

	public void navigateLink(String url) {
		driver.navigate().to(url);
	}

	public void toSelectXpathBasedONString(String str, String tagname) {

		String xpath = "(//" + tagname + "[text()=" + "'" + str + "'])[last()]";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", driver.findElement(By.xpath(xpath)));
		
	}
	
	public boolean BooleanIfXpathPresentsInPDSPageOnly(String fieldName) {
		
		
		String xpath = "//*[@data-field-id='"+ fieldName + "']//div[@class='slds-form-element slds-hint-parent test-id__output-root slds-form-element_readonly slds-form-element_stacked']";

		boolean isEditable = false;

		try {
		    isEditable = driver.findElement(By.xpath(xpath)).isEnabled();
		    if (isEditable) {
		        System.out.println("The field is editable.");
		    } else {
		        System.out.println("The field is not editable.");
		    }
		}
		
		
		catch (NoSuchElementException e) {
		    System.out.println("Element not found. Returning false.");
		}

       finally {
			
			
		}
		
		return isEditable;
		
		
	}
	

	public  WebElement toGetWebelementXpathBasedONString(String str, String tagname) {

		String xpath = "//" + tagname + "[text()=" + "'" + str + "']";
		return driver.findElement(By.xpath(xpath));
	}

	public String togetStringXpathBasedONString(String str, String tagname) {

		String xpath = "//" + tagname + "[text()=" + "'" + str + "']";
		return driver.findElement(By.xpath(xpath)).getText();
	}

	public void openNewTabWithLink(String url) throws InterruptedException {
		((JavascriptExecutor) driver).executeScript("window.open('');");
		this.tabHandels(1);
		driver.get(url);

	}

	public String getTitle() {
		return driver.getTitle();
	}

	public void tomorrowdatepicker(String value) {
		// This are the columns of the from date picker table
		List<WebElement> columns = driver.findElements(By.tagName("td"));

		// DatePicker is a table. Thus we can navigate to each cell
		// and if a cell matches with the current date then we will click it.
		for (WebElement cell : columns) {

			System.out.println(cell.getText());

			/*
			 * //If you want to click 18th Date if (cell.getText().equals("18")) {
			 */
			// Select Today's Date
			if (cell.getText().equals(value)) {
				cell.click();
				break;
			}
		}
	}

	public void Refresh() {
		driver.navigate().refresh();
	}

	public void Refreshing() throws InterruptedException {
		driver.get(driver.getCurrentUrl());
		Thread.sleep(3000);
		driver.navigate().to(driver.getCurrentUrl());

	}

	public void navigateBackToPreviousPage() {
		driver.navigate().back();
	}

	public void CloseTheBrowser() {
		driver.close();
	}

	public void pause(Integer milliseconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void SelectByText(WebElement ele, String value) {
		try {
			Select select = new Select(ele);
			select.selectByVisibleText(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SelectDropdownDownValue(List<WebElement> ele, String value) {
		List<WebElement> eledata = ele;
		int count = eledata.size();
		for (int i = 0; i < count; i++) {
			if (eledata.get(i).getText().equalsIgnoreCase(value)) {
				System.out.println(eledata.get(i).getText());
				eledata.get(i).click();
				break;
			}
		}
	}

	public void SelectDropdownDownValueByUsingContains(List<WebElement> ele, String value) {
		List<WebElement> eledata = ele;
		int count = eledata.size();
		for (int i = 0; i < count; i++) {
			if (eledata.get(i).getText().contains(value)) {
				System.out.println(eledata.get(i).getText());
				eledata.get(i).click();
				break;
			}
		}
	}

	public void SelectDropdownDownValueByUsingStartsWith(List<WebElement> ele, String value) {
		List<WebElement> eledata = ele;
		int count = eledata.size();
		for (int i = 0; i < count; i++) {
			if (eledata.get(i).getText().startsWith(value)) {
				System.out.println(eledata.get(i).getText());
				eledata.get(i).click();
				break;
			}
		}
	}

	public void SelectDropdownDownValueByUsingJavaScript(List<WebElement> ele, String value) {
		List<WebElement> eledata = ele;
		int count = eledata.size();
		for (int i = 0; i < count; i++) {
			if (eledata.get(i).getText().contains(value)) {
				System.out.println(eledata.get(i).getText());
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].click();", eledata.get(i));
				break;
			}
		}
	}

	public void toClickOnMoreButtonIfPresents(List<WebElement> ele, String value, WebElement toClick)
			throws InterruptedException {

		int count = ele.size();
		for (int i = 0; i < count; i++) {

			if (ele.get(i).getText().equalsIgnoreCase(value)) {
				System.out.println(ele.get(i).getText());
				break;

			} else {
				toClick.click();
			}
		}
	}

	public void SelectDropdownDownValueByUsingJavaScriptBySpacifyingValues(List<WebElement> ele, String value,
			WebElement toClick) throws InterruptedException {

		int count2 = ele.size();
		for (int i = 0; i < count2; i++) {

			if (ele.get(i).getText().equalsIgnoreCase(value)) {
				System.out.println(ele.get(i).getText());
				Thread.sleep(3000);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].click();", ele.get(i));
				break;
//			}
//			}
			}
		}
	}
//		int i = 0;
//	
////		while(eledata.get(i).getText().equalsIgnoreCase(value) !=  eledata.listIterator(count).toString().equalsIgnoreCase(value)){
////			
////		while(eledata.get(i).getText().equalsIgnoreCase(value)) {
////			
////			System.out.println(eledata.get(i).getText());
////			
////			System.out.println(eledata.listIterator(count).toString())); 
////			toClick.click();
////			
////			
////			
////			
////		}
//		 
//        // Condition check whether there is element in List
//        // using hasNext() which holds true till
//        // there is single element in List
//		
//	         ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", toClick); 
//	        	   toClick.click();
//	        	   ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", toClick); 
//	        	   toClick.click();
//	}

	public void MouseOver(WebElement locatorfrom, WebElement locatorto) {
		// Actions class method to drag and drop
		Actions builder = new Actions(driver);
		// Perform drag and drop
		builder.moveToElement(locatorfrom).moveToElement(locatorto).click().build().perform();
	}

	public void scrolltobottom() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	public void scrollTop() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,-300)");
	}

	public void scrollDown() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,5000)");
	}

	public void scrollToView(WebElement locator) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", locator);
	}

	public void scrollToViewByActionClass(WebElement locator) {
		Actions actions = new Actions(driver);
		actions.moveToElement(locator);
		actions.perform();
	}

	public void switchToIframe(WebElement locator) {
		driver.switchTo().frame(locator);
	}

	public void switchToIframes() {
		List<WebElement> data = driver.findElements(By.tagName("iframe"));
		int count = data.size();
		System.out.println(count);
		for (int i = 0; i < count; i++) {
			driver.switchTo().frame(i);
		}

	}

	public void pageload() {
		// Sleep until the div we want is visible or 5 seconds is over
		long end = System.currentTimeMillis() + 5000;
		while (System.currentTimeMillis() < end) {
			// Browsers which render content (such as Firefox and IE) return
			// "RenderedWebElements"
			WebElement resultsDiv = driver.findElement(By.className("gac_m"));

			// If results have been returned, the results are displayed in a drop down.
			if (resultsDiv.isDisplayed()) {
				break;
			}
		}
	}

	static String locatelement = "//div[@class='rt-tbody']/div[4]/div/div";

	public void verifydatainfor(String value) {
		List<WebElement> listdata = driver.findElements(By.xpath(locatelement));

		// List<String> list=new ArrayList<String>();

		int count = listdata.size();
		System.out.println("******************************");
		for (int i = 0; i < count; i++) {
			listdata.get(i).getText();
			if (listdata.get(i).getText().contains(value)) {
				System.out.println(value + " is Present in webtable");
			}
		}
	}

	public void verifydata(List<WebElement> locator, String value) throws InterruptedException {
		List<WebElement> Autocompletelists = locator;
		int count = Autocompletelists.size();
		for (int i = 0; i < count; i++) {
			String actualdata = Autocompletelists.get(i).getText();
			System.out.println(actualdata);
			if (actualdata.equals(value)) {
				Actions actions = new Actions(driver);
				Thread.sleep(2000);
				actions.moveToElement(Autocompletelists.get(i)).click().build().perform();
				break;
			}
		}
	}

	public void JavascriptExecutor(WebElement ele) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", ele);
	}

	public boolean JavascriptExecutorToCheckElementIsPresent(WebElement ele) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Boolean isElementPresent = (Boolean) js.executeScript("return arguments != null;", ele);
		return isElementPresent;
	}

	public void JavascriptExecutorToSendValues(WebElement element, String value) {

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].value='" + value + "';", element);

	}

	public void actionClick(WebElement ele) {

		Actions actions = new Actions(driver);
		actions.moveToElement(ele).click().build().perform();
	}

	public void actionSendKeys(WebElement ele, String keys) {

		Actions actions = new Actions(driver);
		actions.sendKeys(ele, keys).perform();
	}

	public void VerifyWebElement(WebElement ele, String value) throws InterruptedException {
		String parentwindow = driver.getWindowHandle();// parent id
		System.out.println(parentwindow);

		Set<String> allwindows = driver.getWindowHandles(); // parent id+ child id
		System.out.println(allwindows);

		for (String child : allwindows) {
			if (!child.equalsIgnoreCase(parentwindow)) {
				driver.switchTo().window(child);
				Thread.sleep(2000);
				scrolltobottom();
				String expectedText = ele.getText();
				Assert.assertEquals(expectedText, value);
				Thread.sleep(2000);
				driver.close();
				break;
			}
		}
		driver.switchTo().window(parentwindow);
	}

	public void WindowHandle(WebElement ele, String value) throws InterruptedException {
		String parentwindow = driver.getWindowHandle();
		Set<String> allwindows = driver.getWindowHandles();
		for (String child : allwindows) {
			if (!child.equalsIgnoreCase(parentwindow)) {
				driver.switchTo().window(child);
				Thread.sleep(2000);
				/*
				 * scrolltobottom(); String expectedText = ele.getText();
				 * Assert.assertEquals(expectedText, value);
				 */
				ele.sendKeys(value);
				Thread.sleep(2000);
				driver.close();
				break;
			}
		}
		driver.switchTo().window(parentwindow);
	}

	public void VerifyTermsPrivacyLinks(WebElement ele, String value) {
		String parentwindow = driver.getWindowHandle();
		Set<String> allwindows = driver.getWindowHandles();
		for (String child : allwindows) {
			if (!child.equalsIgnoreCase(parentwindow)) {
				driver.switchTo().window(child);
				String webText = ele.getText();
				Assert.assertEquals(webText, value);
				driver.close();
				break;
			}
		}
		driver.switchTo().window(parentwindow);
	}

	public void SearchSites(WebElement ele, String value) {
		String parentwindow = driver.getWindowHandle();
		Set<String> allwindows = driver.getWindowHandles();
		for (String child : allwindows) {
			if (!child.equalsIgnoreCase(parentwindow)) {
				driver.switchTo().window(child);
				ele.sendKeys(value);
				break;
			}
		}
	}

	public void CloseChild() {
		String parentwindow = driver.getWindowHandle();
		Set<String> allwindows = driver.getWindowHandles();
		for (String child : allwindows) {
			if (!child.equalsIgnoreCase(parentwindow)) {
				driver.switchTo().window(child);
				driver.close();
				break;
			}
		}
	}

	public void InputClick(WebElement ele, WebElement wb, String value) {

		String MainWindow = driver.getWindowHandle();
		String Childwindow = MainWindow;
		System.out.println(Childwindow);
		String ChildWindow1 = Childwindow;
		System.out.println(ChildWindow1);
		String ChildWindow2 = ChildWindow1;
		System.out.println(ChildWindow2);

		for (String Handle : driver.getWindowHandles()) {
			if (!Childwindow.equals(Handle)) {
				Childwindow = Handle;
				System.out.println(Childwindow);
				driver.switchTo().window(Childwindow);
				ele.sendKeys(value);
				wb.click();
				break;
			}
		}
	}

	static String locator = "//div[@id='myInputautocomplete-list']/div";

	public void verifyautoselect() {
		List<WebElement> Autocompletelists = driver.findElements(By.xpath(locator));
		int count = Autocompletelists.size();
		for (int i = 0; i < count; i++) {
			String actualdata = Autocompletelists.get(i).getText();
			System.out.println(actualdata);
			if (actualdata.equals("Pizza")) {

				System.out.println("Pizza displayed");
				Assert.assertEquals(actualdata, "Pizza");
			}
			if (actualdata.equals("Pepperoni")) {

				System.out.println("Pepperoni displayed");
				Assert.assertEquals(actualdata, "Pepperoni");
			}
			if (actualdata.equals("Pancakes")) {

				System.out.println("Pancakes displayed");
				Assert.assertEquals(actualdata, "Pancakes");
			}
		}
	}

	// Alerts

	public void switchToAlerts() {

		driver.switchTo().alert();
	}

	public void acceptAlerts() {

		driver.switchTo().alert().accept();
	}

	public void dismissAlerts() {

		driver.switchTo().alert().dismiss();
	}

	public String getTextFromAlert() {

		String text = driver.switchTo().alert().getText();
		return text;
	}

	public void switchoutIframe() {

		driver.switchTo().defaultContent();
	}

	public void waitForElementToAppear(WebElement elementToAppear) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.visibilityOfAllElements(elementToAppear));

	}

	public void waitAndRefreshUntillElementFount(String xpathElement) {

		// Define the timeout in seconds
		int timeout = 30;
		boolean elementFound = false;
		// Disable implicit wait
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

		while (timeout > 0) {
			try {
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
				WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathElement)));
				elementFound = true;
				System.out.println("Element found!");
				break;
			} catch (Exception e) {
				System.out.println("Element not found, refreshing the page...");
				driver.navigate().refresh();
				timeout -= 5; // Decrease the timeout by the wait time
			}
		}
		// Re-enable implicit wait if needed
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
	}

	public void waitUntilTitleVisible(String title) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.titleContains(title));

	}

	public void fluentWaitForElementToAppear(WebElement elementToAppear) {

		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(25))
				.pollingEvery(Duration.ofSeconds(1));
		wait.until(ExpectedConditions.visibilityOfAllElements(elementToAppear));
	}

	public void toCheckEditableElementsInPage(List<WebElement> myElements, String attributes) {

		for (WebElement e : myElements) {
			String text = e.getAttribute(attributes);
			System.out.println("Found : " + text);

			if (!text.isEmpty()) {
				System.out.println("Field is editable");
				e.isDisplayed();
				System.out.println("Text case pass:");
				Assert.assertTrue(true);
				break;
			}

			else {
				Assert.assertFalse(false);
			}

		}

	}

	public void tabHandels(int index) throws InterruptedException {
		Set<String> windowHandles = driver.getWindowHandles();
//		Iterator<String> iterator = windowHandles.iterator();
//		String parentWindow = iterator.next();
//		String childWindow = iterator.next();
//		
//		driver.switchTo().window(childWindow);

		driver.switchTo().window((String) windowHandles.toArray()[index]);

	}

	public static void switchToTabByTitle(WebDriver driver, String desiredTitle) {
		List<String> windowHandles = new ArrayList<>(driver.getWindowHandles());

		for (String handle : windowHandles) {
			driver.switchTo().window(handle);
			String title = driver.getTitle();
			if (title.startsWith(desiredTitle)) {
				// Found the desired tab, perform actions or return
				return;
			}
		}
	}

	public void tabThreeHandels() throws InterruptedException {
		Set<String> windowHandles = driver.getWindowHandles();
		Iterator<String> iterator = windowHandles.iterator();
		String parentWindow = iterator.next();
		String childWindow = iterator.next();
		String thirdWindow = iterator.next();
		driver.switchTo().window(thirdWindow);

	}

	public String[] iteratingWithListToGetTextOfFIleNameForExternalUser(List<WebElement> ele) {

		List<WebElement> eledata = ele;
		int count = eledata.size();
		List<String> list = new ArrayList<String>();

		for (int i = 0; i < count; i++) {

			if (ele.size() > 0) {
				String fileName = eledata.get(i).getText();
				list.add(fileName);

			}

			// Size of list
			int list_size = list.size();
			// Creating string array
			String[] arrayOfFileNameForExternalUser = new String[list_size];
			// Converting to string array
			list.toArray(arrayOfFileNameForExternalUser);

			// Printing the string array
			for (int i1 = 0; i1 < arrayOfFileNameForExternalUser.length; i1++) {
				System.out.println(arrayOfFileNameForExternalUser[i1]);
			}
		}
		return arrayOfFileNameForExternalUser;
	}

	public String[] iteratingWithListToGetTextOfFIleNameForInternalUser(List<WebElement> ele) {

		List<WebElement> eledata = ele;
		int count = eledata.size();

		List<String> list = new ArrayList<String>();

		for (int i = 0; i < count; i++) {

			if (ele.size() > 0) {
				String fileName = eledata.get(i).getText();
				list.add(fileName);

			}
			// Size of list
			int list_size = list.size();
			// Creating string array
			String[] arrayOfFileNameForInternalUser = new String[list_size];
			// Converting to string array
			list.toArray(arrayOfFileNameForInternalUser);

			// Printing the string array
			for (int i1 = 0; i1 < arrayOfFileNameForInternalUser.length; i1++) {
				System.out.println(arrayOfFileNameForInternalUser[i1]);
			}
		}
		return arrayOfFileNameForInternalUser;
	}

	public void waitUntillPageLoads() throws InterruptedException {

		JavascriptExecutor j = (JavascriptExecutor) driver;
		if (j.executeScript("return document.readyState").toString().equals("complete")) {
			System.out.println("Page has loaded");
		}

	}

	public void waitUntillImage() throws InterruptedException {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		Object result = js.executeScript("return document.readyState;");
		System.out.println("DOM status: " + result);

		if (result.equals("complete")) {
			result = js.executeScript("return document.images.length;");
			int imagesCount = Integer.parseInt(result.toString());
			boolean allLoaded = false;

			while (!allLoaded) {
				int count = 0;
				for (int i = 0; i < imagesCount; i++) {
					result = js.executeScript("return document.images[" + i + "].complete;");
					boolean loaded = (Boolean) result;
					if (loaded) {
						count++;
					}
				}

				if (count == imagesCount) {
					System.out.println("All images are loaded!");
					// Proceed with further actions
					allLoaded = true;
				} else {
					// Wait or retry (you can add a sleep here)

					Thread.sleep(3000);
				}
			}

		}
	}

	public void deleteFiles(String FilePath) throws IOException {

		File directory = new File(FilePath);
		FileUtils.cleanDirectory(directory);
		System.out.println("Deleting the previous downloaded files");

	}

	public boolean isFileAvailable(String FilePath, String expectedFileName) throws IOException {

		File folder = new File(FilePath);
		File[] listofFiles = folder.listFiles();

		boolean isFileAvailable = false;

		for (File listofFile : listofFiles) {

			if (listofFile.isFile()) {

				String fileName = listofFile.getName();
				System.out.println(fileName);

				if (fileName.matches(expectedFileName)) {

					isFileAvailable = true;

				}

			}

		}
		return isFileAvailable;

	}

	public void getDataFromDownloadedPDF(String FileName, String ExpectedStringValidation) throws IOException {

		String FilePath = Constants.filePath + "\\" + FileName + ".pdf";
		File file = new File(FilePath);

		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(25))
				.pollingEvery(Duration.ofMillis(100));
		wait.until(x -> file.exists());

		try {

			PDDocument document = PDDocument.load(new File(FilePath));

			PDFTextStripper pdfStripper = new PDFTextStripper();
			String text = pdfStripper.getText(document);

			System.out.println("Document Title --> " + document.getDocumentInformation().getTitle());
			System.out.println("Document Subject --> " + document.getDocumentInformation().getSubject());
			System.out.println("Total number of Pages --> " + document.getNumberOfPages());
			System.out.println("Is this document encrypted --> " + document.isEncrypted());
			System.out.println("Document Author --> " + document.getDocumentInformation().getAuthor());
			System.out.println("Document Created --> " + document.getDocumentInformation().getCreationDate());

			// Validate the PDF content
			if (text.contains(ExpectedStringValidation)) {

				System.out.println("PDF validation passed.");
				Assert.assertTrue(true);

			} else {

				System.out.println("PDF validation failed.");
				Assert.assertFalse(true);
				Assert.fail("expected text is not present in the pdf");
			}

			// Clean up
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void getDataFromPDFOpenedInAnotherTab(String ExpectedStringValidation)
			throws IOException, URISyntaxException, InterruptedException {

		try {

//			Set <String> windowHandles = driver.getWindowHandles();
//			Iterator <String> it = windowHandles.iterator();
//			
//			String parentWindow = it.next();
//			
//			String childWindow = it.next();
//			
//			driver.switchTo().window(childWindow);

			this.tabHandels(1);

			String OpenedPDFUrl = driver.getCurrentUrl();
			System.out.println(OpenedPDFUrl);

			URI uri = new URI(OpenedPDFUrl);
			PDDocument document = PDDocument.load(uri.toURL().openStream());
			PDFTextStripper pdfStripper = new PDFTextStripper();
			String pdfText = pdfStripper.getText(document);

			System.out.println("Document Title --> " + document.getDocumentInformation().getTitle());
			System.out.println("Document Subject --> " + document.getDocumentInformation().getSubject());
			System.out.println("Total number of Pages --> " + document.getNumberOfPages());
			System.out.println("Is this document encrypted --> " + document.isEncrypted());
			System.out.println("Document Author --> " + document.getDocumentInformation().getAuthor());
			System.out.println("Document Created --> " + document.getDocumentInformation().getCreationDate());

			// Validate the PDF content
			if (pdfText.contains(ExpectedStringValidation)) {

				System.out.println("PDF validation passed.");

				Assert.assertTrue(true, "expected text is present in the pdf");

			} else {

				System.out.println("PDF validation failed.");

				Assert.fail("expected text is not present in the pdf");
			}

			// Clean up
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void listOfItemsInOroniteConnectRecentlyViewed(List<WebElement> ele, String str) {

		List<WebElement> eledata = ele;
		int count = eledata.size();
		for (int i = 0; i < count; i++) {
			if (eledata.get(i).getText().contains(str)) {
				System.out.println(eledata.get(i).getText());
				Assert.assertTrue(true);
				break;
			}
		}

	}

	public void toSelectTheRecentlyViewedItemsFromRecentlyViewedItemsOroniteConnectHomePage(List<WebElement> ele,
			String str) {

		List<WebElement> eledata = ele;
		int count = eledata.size();
		for (int i = 0; i < count; i++) {
			if (eledata.get(i).getText().contains(str)) {
				System.out.println(eledata.get(i).getText());
				eledata.get(i).click();
				break;

			}
		}

	}

	public boolean compareLists(List<String> list1, List<String> list2) {
		if (list1.size() != list2.size()) {
			System.out.println("Files are not matching test failed");
			return false; // Lists have different sizes

		}

		if (list1.containsAll(list2)) {
			System.out.println("All elements of list2 are in list1");
		} else {
			System.out.println("Not all elements of list2 are in list1");
		}

		List<String> differences = list1.stream().filter(element -> !list2.contains(element))
				.collect(Collectors.toList());

		System.out.println("Differences: " + differences);

		System.out.println("Files are matching test passed");
		return true; // All elements match
	}

	public void switchToTabByTitle(String tabTitle) {
		Set<String> handles = driver.getWindowHandles();
		for (String handle : handles) {
			driver.switchTo().window(handle);
			System.out.println(driver.getTitle());
			if (driver.getTitle().startsWith(tabTitle)) {
				break; // Found the desired tab
			}
		}
	}

	public void switchToTabByUrl(String tabUrl) {
		Set<String> handles = driver.getWindowHandles();
		for (String handle : handles) {
			driver.switchTo().window(handle);
			if (driver.getCurrentUrl().equals(tabUrl)) {
				break; // Found the desired tab
			}
		}
	}

	public void brokenLinkInWebPages()
			throws MalformedURLException, IOException, URISyntaxException, InterruptedException {
		List<String> hrefList = new ArrayList<>();
		trustAllCertificates();
		List<WebElement> elementList = driver.findElements(By.tagName("a"));
		System.out.println("Total number of webpage links present in this page" + elementList.size());
		List<WebElement> imageList = driver.findElements(By.tagName("img"));
		System.out.println("Total number of image links present in this page" + imageList.size());
		elementList.addAll(imageList);

		int count = elementList.size();
		for (int i = 0; i < count; i++) {

			WebElement element = elementList.get(i);
			String href = element.getAttribute("href");
			if (href != null && !href.isEmpty() && !href.contains("javascript")) {
				hrefList.add(href);
			}

		}

		try (FileWriter writer = new FileWriter(Constants.brokenLinksTextFile, true)) { // 'true' to append to the file
			for (String url : hrefList) {
				try {
					URI uri = new URI(url);
					URL urlobj = uri.toURL();

					HttpURLConnection huc = (HttpURLConnection) urlobj.openConnection();
					huc.setRequestMethod("HEAD");
					huc.connect();
					int responseCode = huc.getResponseCode();

					// Check if the response code indicates an error
					if (responseCode >= 400) {
						writer.write(url + System.lineSeparator());
						System.out.println("Broken link: " + url + " - Response code: " + responseCode);
					} else {
						System.out.println(url);
						driver.navigate().to(url);
						Thread.sleep(3000);
						String pageTitle = driver.getTitle();
						System.out.println(pageTitle);
						if (pageTitle.contains("Error")) {
							writer.write(url + System.lineSeparator());
							System.out.println("Broken link: " + url);
							log.info("Broken link: " + url);
						}
					}
				} catch (StaleElementReferenceException | IOException e) {
					System.out.println("Exception while checking link: " + url);
					e.printStackTrace();
				}
			}
			System.out.println("URLs have been written to " + Constants.brokenLinksTextFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void brokenLinkInWebPage() throws MalformedURLException, IOException, URISyntaxException {

		trustAllCertificates();
		List<WebElement> elementList = driver.findElements(By.tagName("a"));
		System.out.println("Total number of webpage links present in this page" + elementList.size());
		List<WebElement> imageList = driver.findElements(By.tagName("img"));
		System.out.println("Total number of image links present in this page" + imageList.size());
		elementList.addAll(imageList);

		for (WebElement element : elementList) {
			String url = element.getAttribute("href");

			if (url == null) {
				url = element.getAttribute("src");
			}
			int attempts = 0;
			if (url != null && !url.contains("javascript")) {
				try {
					while (attempts < 3) {
						URI uri = new URI(url);
						URL urlobj = uri.toURL();

						HttpURLConnection connection = (HttpURLConnection) urlobj.openConnection();
						connection.setRequestMethod("HEAD");
						connection.connect();
						// connection.setRequestMethod("HEAD");
						connection.setConnectTimeout(3000);
						int responseCode = connection.getResponseCode();

						if (responseCode >= 400) {
							System.out.println("Broken link: " + url + " - Response code: " + responseCode);
						} else {
							// Navigate to the link and check the page title for error indicators
							driver.navigate().to(url);

							String pageTitle = driver.getTitle();
							System.out.println(pageTitle);
							if (pageTitle.contains("Error") || pageTitle.contains("Not Found")
									|| pageTitle.contains("Invalid Page")) {
								System.out.println("Broken link: " + url + " - Page title indicates error");
							}
							driver.navigate().back();
						}
					}

				} catch (StaleElementReferenceException | IOException e) {
					System.out.println("Exception while checking link: " + url);
					e.printStackTrace();
					attempts++;
				}
			}
		}

	}

	private static void trustAllCertificates() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void takeScreenshot(String imgName) throws IOException, InterruptedException {

		String splitName[] = imgName.split("_");
		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
				.takeScreenshot(driver);
		System.out.println(splitName[1]);

		if (splitName[1].equalsIgnoreCase("BeforeDeployement")) {
			// Specify your custom screenshot file name
			String customScreenshotName = imgName + ".png";

			Thread.sleep(3000);
			// Save the screenshot
			ImageIO.write(screenshot.getImage(), "png",
					new File(Constants.filePathToStoreScreenshotBeforeDeployement + "/" + customScreenshotName));
			System.out.println("Full-page screenshot saved as " + customScreenshotName);

		}

		else if (splitName[1].equalsIgnoreCase("AfterDeployement")) {

			// removeTheFilesFromCompare(Constants.filePathToStoreScreenshotAfterDeployement);
			// Specify your custom screenshot file name
			String customScreenshotName = imgName + ".png";
			Thread.sleep(3000);
			// Save the screenshot
			ImageIO.write(screenshot.getImage(), "png",
					new File(Constants.filePathToStoreScreenshotAfterDeployement + "/" + customScreenshotName));
			System.out.println("Full-page screenshot saved as " + customScreenshotName);

		}

	}

	public void compareTheImages(String imgName) {

		String splitName[] = imgName.split("_");

		System.out.println(splitName[0]);
		String pathOfComparisionImage = Constants.filePathToStoreComparisionImage + "/" + splitName[0] + ".png";

		String beforeImagePath = Constants.filePathToStoreScreenshotBeforeDeployement + "//" + splitName[0]
				+ "_BeforeDeployement.png";
		String afterImagePath = Constants.filePathToStoreScreenshotAfterDeployement + "//" + splitName[0]
				+ "_AfterDeployement.png";

		try {
			// Load "before" and "after" screenshots
			BufferedImage imageBefore = ImageIO.read(new File(beforeImagePath));
			BufferedImage imageAfter = ImageIO.read(new File(afterImagePath));

			// Ensure same dimensions (resize if needed)
			int width = Math.min(imageBefore.getWidth(), imageAfter.getWidth());
			int height = Math.min(imageBefore.getHeight(), imageAfter.getHeight());

			// Create a composite image for highlighting differences
			BufferedImage compositeImage = new BufferedImage(width, height * 3, BufferedImage.TYPE_INT_RGB);

			// Iterate through pixels and compare RGB values
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int rgbBefore = imageBefore.getRGB(x, y);
					int rgbAfter = imageAfter.getRGB(x, y);

					// Calculate difference (Euclidean distance or absolute difference)
					int diff = Math.abs(rgbBefore - rgbAfter);

					System.out.println("diff:" + diff);

					// Set a threshold (adjust as needed)
					int threshold = 30;

					// Highlight differing pixels
					if (diff > threshold) {
						compositeImage.setRGB(x, y, rgbAfter); // Use "after" color
					} else {
						compositeImage.setRGB(x, y, rgbBefore); // Use "before" color
					}
				}
			}

			// Save the composite image with highlighted differences
			ImageIO.write(compositeImage, "png", new File(pathOfComparisionImage));

			System.out.println("Image comparison completed!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void compareTheImagesPY(String imgName) throws IOException {

		String splitName[] = imgName.split("_");

		System.out.println(splitName[0]);
		String pathOfComparisionImage = Constants.filePathToStoreComparisionImage + "/" + splitName[0] + ".png";

		String beforeImagePath = Constants.filePathToStoreScreenshotBeforeDeployement + "//" + splitName[0]
				+ "_BeforeDeployement.png";
		String afterImagePath = Constants.filePathToStoreScreenshotAfterDeployement + "//" + splitName[0]
				+ "_AfterDeployement.png";

		// removeTheFilesFromCompare(Constants.filePathToStoreComparisionImage);

		BufferedImage imageBefore = ImageIO.read(new File(beforeImagePath));
		BufferedImage imageAfter = ImageIO.read(new File(afterImagePath));

		ImageDiffer imgDiff = new ImageDiffer();
		ImageDiff diff = imgDiff.makeDiff(imageBefore, imageAfter);

		int totalPixels = imageBefore.getWidth() * imageBefore.getHeight();

		// Set the tolerance level (% of the total pixels)
		int tolerance = (int) (totalPixels * 0.01);

		if (diff.getDiffSize() > tolerance) {
			System.out.println("Images are different!");
			BufferedImage diffImage = diff.getMarkedImage();
			ImageIO.write(diffImage, "png", new File(pathOfComparisionImage));
		} else {
			System.out.println("Images are similar");
		}

	}

//	public void compareTheImagesII(String imgName) throws IOException {
//
//		String splitName[] = imgName.split("_");
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//
//		// Load your original images (image1 and image2) using OpenCV
//		String image1Path = Constants.filePathToStoreScreenshotBeforeDeployement + "//" + splitName[0]
//				+ "_BeforeDeployement.png";
//		String image2Path = Constants.filePathToStoreScreenshotAfterDeployement + "//" + splitName[0]
//				+ "_AfterDeployement.png";
//
//		Mat image1 = Imgcodecs.imread(image1Path);
//		Mat image2 = Imgcodecs.imread(image2Path);
//
//		// Convert images to grayscale
//		Mat grayImage1 = new Mat();
//		Mat grayImage2 = new Mat();
//		Imgproc.cvtColor(image1, grayImage1, Imgproc.COLOR_BGR2GRAY);
//		Imgproc.cvtColor(image2, grayImage2, Imgproc.COLOR_BGR2GRAY);
//
//		// Calculate absolute difference between the two grayscale images
//		Mat diff = new Mat();
//		Core.absdiff(grayImage1, grayImage2, diff);
//
//		// Threshold the difference image to highlight differences
//		Mat thresholdedDiff = new Mat();
//		Imgproc.threshold(diff, thresholdedDiff, 30, 255, Imgproc.THRESH_BINARY);
//
//		// Find contours of differences
//		List<MatOfPoint> contours = new ArrayList<>();
//		Mat hierarchy = new Mat();
//		Imgproc.findContours(thresholdedDiff, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//
//		// Draw rectangles around differing regions
//		for (MatOfPoint contour : contours) {
//			Rect boundingRect = Imgproc.boundingRect(contour);
//			Imgproc.rectangle(image1, boundingRect.tl(), boundingRect.br(), new Scalar(0, 0, 255), 2);
//		}
//
//		// Save the highlighted image
//		String outputImagePath = Constants.filePathToStoreComparisionImage + "/" + splitName[0] + ".png";
//		Imgcodecs.imwrite(outputImagePath, image1);
//		System.out.println("Differences highlighted and saved as " + outputImagePath);
//	}

	public void compareTheScreenshotsBeforeAndAfterDeployement(String imgName) throws IOException {

		try {

			String splitName[] = imgName.split("_");

			System.out.println(splitName[0]);
			String pathOfComparisionImage = Constants.filePathToStoreComparisionImage + "/" + splitName[0] + ".png";

			String beforeImagePath = Constants.filePathToStoreScreenshotBeforeDeployement + "//" + splitName[0]
					+ "_BeforeDeployement.png";
			String afterImagePath = Constants.filePathToStoreScreenshotAfterDeployement + "//" + splitName[0]
					+ "_AfterDeployement.png";

			// removeTheFilesFromCompare(Constants.filePathToStoreComparisionImage);

			BufferedImage img1 = ImageIO.read(new File(beforeImagePath));
			BufferedImage img2 = ImageIO.read(new File(afterImagePath));

			if (img1 == null || img2 == null) {
				System.out.println("One of the images could not be loaded.");
				return;
			}

			// Check if images have the same dimensions
			if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
				System.out.println("Images do not have the same dimensions.");
				return;
			}

			// Compare the images
			BufferedImage diff = getDifferenceImage(img1, img2);

			// Save the difference image
			ImageIO.write(diff, "png", new File(pathOfComparisionImage));
			System.out.println("Differences found and highlighted in highlighted_diff.png");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static BufferedImage getDifferenceImage(BufferedImage img1, BufferedImage img2) {
		final int TOLERANCE = 50;
		int width = img1.getWidth();
		int height = img1.getHeight();
		BufferedImage diff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = diff.createGraphics();
		g.drawImage(img1, 0, 0, null);

		// Compare pixel by pixel with tolerance
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb1 = img1.getRGB(x, y);
				int rgb2 = img2.getRGB(x, y);
				if (!areColorsSimilar(rgb1, rgb2, TOLERANCE)) {
					// Highlight differences in red
					diff.setRGB(x, y, Color.RED.getRGB());
				}
			}
		}
		g.dispose();
		return diff;
	}

	private static boolean areColorsSimilar(int rgb1, int rgb2, int tolerance) {
		Color color1 = new Color(rgb1);
		Color color2 = new Color(rgb2);
		int rDiff = Math.abs(color1.getRed() - color2.getRed());
		int gDiff = Math.abs(color1.getGreen() - color2.getGreen());
		int bDiff = Math.abs(color1.getBlue() - color2.getBlue());
		return rDiff <= tolerance && gDiff <= tolerance && bDiff <= tolerance;
	}

	public void removeTheFilesFromCompare(String path) throws IOException {

		Files.walk(Paths.get(path)).filter(Files::isRegularFile).forEach(file -> {
			try {
				Files.delete(file);
			} catch (IOException e) {
				System.err.println("Error deleting file: " + file);
			}
		});

	}

	public void clickOnCanvas(int xOffset, int yOffset) {
		// WebElement canvas=
		// BaseClass.getdriver().findElement(By.xpath("(//canvas)[last()]"));
		// Actions actions = new Actions(driver);
		// actions.moveToElement(canvas, xOffset, yOffset).click().perform();

		WebElement canvas = BaseClass.getdriver().findElement(By.xpath("//div[@class='spreadsheet-grid']//canvas"));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		String script = "var canvas = arguments[0];" + "var rect = canvas.getBoundingClientRect();"
				+ "var x = arguments[1];" + "var y = arguments[2];" + "var clickEvent = new MouseEvent('click', {"
				+ "    view: window," + "    bubbles: true," + "    cancelable: true," + "    clientX: rect.left + x,"
				+ "    clientY: rect.top + y" + "});" + "canvas.dispatchEvent(clickEvent);";
		js.executeScript(script, canvas, xOffset, yOffset);
	}

	public void getNewDriverSalesforceInstance() throws IOException, InterruptedException {

//		 String parentWindow = driver.getWindowHandle();
//
//	        // Open a new window
//	        ((JavascriptExecutor) driver).executeScript("window.open('');");
//
//	        // Switch to the new window
//	        for (String windowHandle : driver.getWindowHandles()) {
//	            if (!windowHandle.equals(parentWindow)) {
//	                driver.switchTo().window(windowHandle);
//	                break;
//	            }
//	        }

		// Navigate to a new URL in the new window
		try {
			driver.navigate().to("https://test.salesforce.com/");
		} catch (Exception e) {

			driver.navigate().to("https://test.salesforce.com/");
		}
	}

	public void newTabWithSameUrl() throws IOException {

		String currentUrl = driver.getCurrentUrl();

		this.pause(3000);

		driver.switchTo().newWindow(WindowType.TAB);

		driver.get(currentUrl);

	}
	
	

}
