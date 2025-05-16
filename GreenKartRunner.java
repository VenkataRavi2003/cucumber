package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
//html , xml,json,junit,extent
@CucumberOptions(features = "src/test/resources/features",
		glue = "stepDefinitions",
		monochrome = true,
		dryRun=false,
		plugin={"html:reports/cucumber.html",
		"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"})
public class GreenKartRunner extends AbstractTestNGCucumberTests {

}
