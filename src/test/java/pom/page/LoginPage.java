package pom.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pom.common.DriverManager;
import pom.common.WebUI;

import java.time.Duration;

public class LoginPage {
    private By username = By.xpath("//input[@id='user-name']");
    private By password = By.xpath("//input[@id='password']");
    private By btnLogin = By.xpath("//input[@id='login-button']");
    private final By txtErrorMsg = By.xpath("//h3[@data-test='error']");
    public LoginPage() {}

    public LoginPage enterUsername(String user) {
        WebUI.setText(username, user);
        return this;
    }

    public LoginPage enterPassword(String pass) {
        WebUI.setText(password, pass);
        return this;
    }

    public void submit() {
        WebUI.clickElement(btnLogin);
    }

    public long measureLoginToInventoryTime() {
        WebDriver d = DriverManager.getDriver();

        long start = System.currentTimeMillis();
        WebUI.clickElement(btnLogin);
        InventoryPage inventoryPage = new InventoryPage();
        new WebDriverWait(d, Duration.ofSeconds(30))
                .until(ExpectedConditions.visibilityOfElementLocated(inventoryPage.getInventoryContainer()));

        long end = System.currentTimeMillis();
        long elapsed = end - start;
        System.out.println("[Login→Cart visible] took ~" + elapsed + " ms");
        return elapsed;
    }

    public InventoryPage loginAs(String user, String pass) {
        enterUsername(user);
        enterPassword(pass);
        submit();
        return new InventoryPage();
    }

    public boolean isErrorVisible() {
        return WebUI.checkElementExist(txtErrorMsg);
    }

    public String getErrorText() {
        return WebUI.getElementText(txtErrorMsg);
    }
}
