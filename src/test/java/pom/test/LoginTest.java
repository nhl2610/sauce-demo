package pom.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import pom.common.BaseSetup;
import pom.common.DriverManager;
import pom.page.InventoryPage;
import pom.page.LoginPage;

public class LoginTest extends BaseSetup {
    @Test(dataProvider = "loginCases", dataProviderClass = AccountProvider.class)
    public void loginEdgeCases(String user, String pass, boolean expectSuccess, String expectedErrorPart, String variant) throws InterruptedException {
        LoginPage page = new LoginPage();
        InventoryPage inventory = page.loginAs(user, pass);

        if (expectSuccess) {
            boolean loaded;
            switch (variant) {
                case "PERF":
                    long timeForLoad = inventory.measureLoginToInventory();
                    Assert.assertTrue(timeForLoad > 5000, "Inventory should need more time to load for performance_glitch_user with extended timeout");
                    break;

                case "VISUAL":
                    loaded = inventory.isLoaded();
                    Assert.assertTrue(loaded, "Inventory should load for visual_user");
                    break;

                case "PROBLEM":
                    loaded = inventory.isLoaded();
                    Assert.assertTrue(loaded, "Inventory should load for problem_user");
                    inventory.addItemToCart("Sauce Labs Backpack");
                    inventory.removeItemToCart("Sauce Labs Backpack");
                    Assert.assertFalse(inventory.isItemAddToCart("Sauce Labs Backpack"), "Product cannot be removed");
                    break;

                case "ERROR":
                    loaded = inventory.isLoaded();
                    Assert.assertTrue(loaded, "Inventory should load for error_user");
                    inventory.addItemToCart("Sauce Labs Backpack");
                    inventory.removeItemToCart("Sauce Labs Backpack");
                    Assert.assertFalse(inventory.isItemAddToCart("Sauce Labs Backpack"), "Product cannot be removed");
                    break;

                default:
                    Assert.assertTrue(inventory.isLoaded(), "Inventory page should be loaded after valid login");
            }

        } else {
            Assert.assertTrue(page.isErrorVisible(), "Expected error to be visible");
            if (expectedErrorPart != null) {
                Assert.assertTrue(page.getErrorText().toLowerCase().contains(expectedErrorPart.toLowerCase()),
                        "Error text should contain: " + expectedErrorPart + " | actual: " + page.getErrorText());
            }
        }
    }

}