package com.mlilei.bot;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * @Author lilei
 * @Description
 * @Date 2021/2/23 12:18
 */
@Component
public class BaiDuMobileOperation implements Operation {

    @Override
    public void search(WebDriver driver) {
        driver.get("http://m.baidu.com/");
        new WebDriverWait(driver, 10).until(ExpectedConditions.titleContains("百度一下"));

        final String word = SearchWord.randomWord();
        driver.findElement(By.id("index-kw")).sendKeys(word);
        delay(500);
        driver.findElement(By.id("index-bn")).click();

        new WebDriverWait(driver, 10).until(ExpectedConditions.titleContains(word));


    }


    @Override
    public void nextPage(WebDriver driver) {
        try {
            this.bottom(driver);
            new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='page-controller']")));
            driver.findElement(By.className("new-nextpage-only")).click();
        } catch (NoSuchElementException e) {
            driver.findElement(By.className("new-nextpage")).click();
        }
        delay(1000);
    }

    @Override
    public void randomVisit(WebDriver driver) {

        new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='c-result-content']")));
        final List<WebElement> elements = driver.findElements(By.className("c-result-content"));
        final int i = new Random().nextInt(elements.size());
        final WebElement webElement = elements.get(i);
        scrollIntoView(driver, webElement);
        delay(1000);
        webElement.click();
    }


}
