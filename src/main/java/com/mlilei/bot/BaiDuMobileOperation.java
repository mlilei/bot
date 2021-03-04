package com.mlilei.bot;

import org.apache.commons.lang3.RandomUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Author lilei
 * @Description
 * @Date 2021/2/23 12:18
 */
@Component
public class BaiDuMobileOperation implements Operation {

    @Override
    public void search(WebDriver driver, Map<String, String> params) {
        driver.get("http://m.baidu.com/");
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("index-kw")));
        final String word = SearchWord.randomWord();
        driver.findElement(By.id("index-kw")).sendKeys(word);
        delay(500);
        driver.findElement(By.id("index-bn")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("results")));
    }


    @Override
    public void nextPage(WebDriver driver, Map<String, String> params) {
        //是否存在new-nextpage-only
        final String pageSource = driver.getPageSource();
        final Document document = Jsoup.parse(pageSource);
        final Elements elements = document.getElementsByClass("new-nextpage-only");

        //下一页WebElement
        final WebElement nextPage;
        if (null != elements && elements.size() > 0) {
            nextPage = driver.findElement(By.className("new-nextpage-only"));
        } else {
            nextPage = driver.findElement(By.className("new-nextpage"));
        }

        //页面定位到元素
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", nextPage);
        delay(1000);

        //下一页
        nextPage.click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("results")));
    }

    @Override
    public void randomVisit(WebDriver driver, Map<String, String> params) {
        //获取所有搜索结果
        final List<WebElement> elements = driver.findElements(By.className("c-result-content"));
        if (CollectionUtils.isEmpty(elements)) {
            return;
        }

        //随机选一个结果
        final int i = RandomUtils.nextInt(0, elements.size());
        final WebElement webElement = elements.get(i);

        //页面定位到元素
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", webElement);
        delay(1000);

        //点击进去
        webElement.click();

    }
}
