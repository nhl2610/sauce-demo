package pom.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pom.common.DriverManager;
import pom.common.WebUI;

import java.time.Duration;

public class InventoryPage {
    private final By inventoryContainer = By.id("inventory_container");
    private final By cartLink = By.id("shopping_cart_container");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");

    public InventoryPage() {}

    public boolean isLoaded() {
        try {
            WebUI.getWebElement(inventoryContainer);
            return WebUI.getWebElement(cartLink).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public long measureLoginToInventory() {
        WebDriver d = DriverManager.getDriver();

        long start = System.currentTimeMillis();
        new WebDriverWait(d, Duration.ofSeconds(30))
                .until(ExpectedConditions.visibilityOfElementLocated(cartLink));

        long end = System.currentTimeMillis();
        long elapsed = end - start;
        System.out.println("[Login→Cart visible] took ~" + elapsed + " ms");
        return elapsed;
    }


    public InventoryPage addItemToCart(String productName) {
        String addBtn = String.format(
                "//div[normalize-space()='%s']/ancestor::div[@class='inventory_item']//button[normalize-space()='Add to cart']",
                productName
        );
        WebUI.clickElement(By.xpath(addBtn));
        return this;
    }

    public InventoryPage removeItemToCart(String productName) {
        String addBtn = String.format(
                "//div[normalize-space()='%s']/ancestor::div[@class='inventory_item']//button[normalize-space()='Remove']",
                productName
        );
        WebUI.clickElement(By.xpath(addBtn));
        return this;
    }

    public boolean isItemAddToCart(String productName) {
        String addBtn = String.format(
                "//div[normalize-space()='%s']/ancestor::div[@class='inventory_item']//button[normalize-space()='Add to cart']",
                productName
        );
        return WebUI.getWebElements(By.xpath(addBtn)).size() > 0;
    }

    public CartPage goToCart() {
        WebUI.clickElement(cartLink);
        return new CartPage();
    }

    public int getCartBadgeCount() {
        if (!WebUI.checkElementExist(cartBadge)) return 0;
        String txt = WebUI.getWebElement(cartBadge).getText().trim();
        try {
            return Integer.parseInt(txt);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}