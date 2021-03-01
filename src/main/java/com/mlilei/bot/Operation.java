package com.mlilei.bot;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @Author lilei
 * @Description
 * @Date 2021/2/23 12:18
 */
public interface Operation {


    void search(WebDriver driver);



    void nextPage(WebDriver driver);

    void randomVisit(WebDriver driver);

    default void back(WebDriver driver) {
        driver.navigate().back();
        delay(1000);
    }

    default void forward(WebDriver driver) {
        driver.navigate().forward();
        delay(1000);
    }

    default void downward(WebDriver driver){
        // 向下偏移了10000个像素，到达底部。
        String js = "var q=document.documentElement.scrollTop=300";
        ((JavascriptExecutor) driver).executeScript(js);
        delay(3000);
    }

    default void bottom(WebDriver driver){
        // 向下偏移了10000个像素，到达底部。
        String js = "var q=document.documentElement.scrollTop=100000";
        ((JavascriptExecutor) driver).executeScript(js);
        delay(1000);
    }


    default void scrollIntoView(WebDriver driver, WebElement webElement) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", webElement);
        delay(1000);
    }


    default void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ignored) {
        }
    }

}
