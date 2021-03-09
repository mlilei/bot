package com.mlilei.bot;

import com.deep007.goniub.selenium.mitm.GoniubChromeDriver;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Map;

/**
 * @Author lilei
 * @Description
 * @Date 2021/2/23 12:18
 */
public interface Operation {


    void search(WebDriver driver, Map<String, String> params);


    void nextPage(WebDriver driver, Map<String, String> params);

    void randomVisit(WebDriver driver, Map<String, String> params);

    default void back(WebDriver driver, Map<String, String> params) {
        driver.navigate().back();
        delay(1000);
    }

    default void forward(WebDriver driver, Map<String, String> params) {
        driver.navigate().forward();
        delay(1000);
    }

    /**
     * @param driver
     * @param params
     */
    default void downward(WebDriver driver, Map<String, String> params) {
        int size = Integer.parseInt(params.getOrDefault("size", "400"));
        size = size * RandomUtils.nextInt(100, 150) / 100;
        String js = "window.scrollBy(0," + size + ")";
        ((JavascriptExecutor) driver).executeScript(js);
        delay(1000);
    }

    /**
     * 到当前页最底部
     *
     * @param driver
     * @param params
     */
    default void bottom(WebDriver driver, Map<String, String> params) {
        String js = "var q=document.documentElement.scrollTop=1000000";
        ((JavascriptExecutor) driver).executeScript(js);
        delay(1000);
    }

    /**
     * 延迟，附带1.0-1.5的随机
     *
     * @param millis 延迟时间
     */
    default void delay(int millis) {
        try {
            Thread.sleep((long) millis * RandomUtils.nextInt(100, 150) / 100);
        } catch (Exception ignored) {
        }
    }

    /**
     * 延迟，附带1.0-1.5的随机
     *
     * @param driver
     * @param params 延迟参数【millis】，默认 1000
     */
    default void delay(GoniubChromeDriver driver, Map<String, String> params) {
        final String millis = params.getOrDefault("millis", "1000");
        delay(Integer.parseInt(millis));
    }

    boolean checkPage(GoniubChromeDriver driver);
}
