package com.tests;

import java.io.File;
import java.util.Properties;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.driver.Driver;
import com.pages.Login_page;
import com.utilities.ReadPropertiesFile;
import com.utilities.SeleniumUtilities;

public class LoginTest extends Driver {

	public static final String filename = null;
	public Login_page LoginPage = new Login_page();
	public ReadPropertiesFile readfile = new ReadPropertiesFile();
	public Properties prop = readfile.readPropertiesFile(filename);
	public SeleniumUtilities selUtils = new SeleniumUtilities();
	
	@BeforeClass
	public void initialization() {
		Driver.init(prop.getProperty("Browser"));
		LoginPage.navigateTo_URL();		
		Assert.assertEquals(Driver.driver.getTitle(), "Swag Labs");
		selUtils.clearScreenshotsDirectory();
	}

	@DataProvider(name = "loginUsername")
	public Object[][] loginUsername() {
		return new Object[][] { { "standard_user" }};
	}

	@Test(priority = 0, dataProvider="loginUsername")
	public void Login_Test_TC1_2_3(String username) {
		boolean flag_Username = LoginPage.username_field();
		boolean flag_Password = LoginPage.password_field();
		boolean flag_Login_Button = LoginPage.password_field();
		Assert.assertEquals(flag_Username, true);
		Assert.assertEquals(flag_Password, true);
		Assert.assertEquals(flag_Login_Button, true);
		LoginPage.enter_Username(username);
		LoginPage.enter_password(prop.getProperty("Password"));
		LoginPage.login_button_click();
		LoginPage.logoTest();
		LoginPage.hamburger_icon_click();
		LoginPage.logout_click();
	}
	
	@Test(priority = 1)
	public void Login_Test_TC8() throws InterruptedException {
		LoginPage.username_clear();
		LoginPage.password_clear();
		LoginPage.login_button_click();
		String error = LoginPage.getErrorMessage();
		System.out.println(error);
		Assert.assertEquals(error,"Epic sadface: Username is required");
		LoginPage.errorMessage_close();
	}
	
	@Test(priority = 2)
	public void Login_Test_TC4() {
		LoginPage.username_clear();
		boolean flag_Username = LoginPage.username_field();
		boolean flag_Password = LoginPage.password_field();
		boolean flag_Login_Button = LoginPage.password_field();
		Assert.assertEquals(flag_Username, true);
		Assert.assertEquals(flag_Password, true);
		Assert.assertEquals(flag_Login_Button, true);
		LoginPage.enter_Username("invalidUN");
		LoginPage.enter_password(prop.getProperty("Password"));
		LoginPage.login_button_click();
		String error = LoginPage.getErrorMessage();
		System.out.println(error);
		LoginPage.errorMessage_close();
		Assert.assertEquals(error,"Epic sadface: Username and password do not match any user in this service");
	}
		
	@Test(priority = 3)
	public void Login_Test_TC9() {
		LoginPage.username_clear();		
		LoginPage.enter_Username("invalidUN");
		LoginPage.password_clear();
		LoginPage.enter_password(prop.getProperty("Password"));
		String type = LoginPage.getPropertyOfPassword("type");
		Assert.assertEquals(type,"password");
	}
	
	@Test(priority = 4,dataProvider="loginUsername")
	public void Login_Test_TC16(String username) {
		LoginPage.username_clear();
		LoginPage.enter_Username(username);
		LoginPage.password_clear();
		LoginPage.enter_password(prop.getProperty("Password"));
		LoginPage.login_button_click();
		LoginPage.logoTest();
		driver.navigate().back();
		boolean flag_Username = LoginPage.username_field();
		boolean flag_Password = LoginPage.password_field();
		Assert.assertEquals(flag_Username, true);
		Assert.assertEquals(flag_Password, true);
	}

	@AfterTest
	public void quit() {
		Driver.driver.quit();
	}

	@AfterMethod
	public void tearDown(ITestResult result) {
		if (ITestResult.FAILURE == result.getStatus()) {
			try {				
				TakesScreenshot ts = (TakesScreenshot) driver;
				File source = ts.getScreenshotAs(OutputType.FILE);
				FileHandler.copy(source, new File("./Screenshots/" + result.getName() + ".png"));
				System.out.println("Screenshot taken");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}