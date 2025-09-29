package pom.page;

import org.openqa.selenium.By;
import pom.common.WebUI;

public class InventoryPage {
    private final By inventoryContainer = By.id("inventory_container");
    private final By cartLink = By.id("shopping_cart_container");
    private final By cartBadge = By.cssSelector(".shopping_cart_badge");

    public InventoryPage() {}
    public boolean isLoaded() {
        try {
            WebUI.getWebElement(inventoryContainer);
            return WebUI.getCurrentUrl().contains("inventory.html");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitUntilLoaded(int timeout) {
        long start = System.nanoTime();
        try {
            WebUI.waitForElementVisible(inventoryContainer, timeout);
            return WebUI.getCurrentUrl().contains("inventory.html");
        } catch (Exception e) {
            return false;
        } finally {
            long end = System.nanoTime();
            long ms = (end - start) / 1_000_000;
            System.out.println("[InventoryPage] Load wait took ~" + ms + " ms");
        }
    }
    public InventoryPage addItemToCart(String productName) {
        String addBtn = String.format(
                "//div[normalize-space()='%s']/ancestor::div[@class='inventory_item']//button[normalize-space()='Add to cart']",
                productName
        );
        WebUI.clickElement(By.xpath(addBtn));
        return this;
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