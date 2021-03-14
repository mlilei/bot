package com.mlilei.bot;

import com.deep007.goniub.selenium.mitm.GoniubChromeDriver;
import com.mlilei.bot.helper.ConfigHelper;
import com.mlilei.bot.helper.WordHelper;
import org.apache.commons.lang3.RandomUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author lilei
 * @Description
 * @Date 2021/2/23 12:18
 */
@Component
public class BaiDuMobileOperation implements Operation {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaiDuMobileOperation.class);

    @Override
    public void search(WebDriver driver, Map<String, String> params) {
        LOGGER.info("打开 https://m.baidu.com");
        driver.get("https://m.baidu.com");
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.presenceOfElementLocated(By.id("index-kw")));
        final String word = WordHelper.randomWord();
        LOGGER.info("输入搜索词 {}", word);
        driver.findElement(By.id("index-kw")).sendKeys(word);
        delay(1000);
        LOGGER.info("搜索");
        driver.findElement(By.id("index-bn")).click();
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.presenceOfElementLocated(By.id("results")));
        LOGGER.info("搜索完成 结果数量={}", driver.findElements(By.id("results")).size());
    }


    @Override
    public void back(WebDriver driver, Map<String, String> params) {
        if (!driver.getCurrentUrl().startsWith("https://m.baidu.com/")) {
            driver.navigate().back();
            delay(1000);
        }
    }

    @Override
    public void nextPage(WebDriver driver, Map<String, String> params) {
        LOGGER.info("准备访问下一页");

        //是否存在new-nextpage-only
        final String pageSource = driver.getPageSource();
        final Document document = Jsoup.parse(pageSource);
        Element controller = document.getElementById("page-controller");
        if (null == controller) {
            LOGGER.info("没有找到 page-controller");
            return;
        }

        final WebElement nextPage;
        final Elements elements = document.getElementsByClass("new-nextpage-only");
        if (null != elements && elements.size() > 0) {
            nextPage = driver.findElement(By.className("new-nextpage-only"));
        } else {
            nextPage = driver.findElement(By.className("new-nextpage"));
        }

        //页面定位到元素
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", nextPage);
        delay(1000);
        LOGGER.info("点击下一页");
        //下一页
        nextPage.click();
        delay(3000);

        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.presenceOfElementLocated(By.id("results")));
        LOGGER.info("下一页 结果数量={}", driver.findElements(By.id("results")).size());
    }

    @Override
    public void randomVisit(WebDriver driver, Map<String, String> params) {
        LOGGER.info("准备随机浏览");
        //获取所有搜索结果
//        final List<WebElement> elements = driver.findElements(By.className("c-result-content"));
        final List<WebElement> elements = driver.findElements(By.className("c-result"));
        if (CollectionUtils.isEmpty(elements)) {
            LOGGER.info("没有可以浏览的结果");
            return;
        }


        WebElement webElement = null;
        for (int i = 0; i < 10; i++) {

            //随机选一个结果
            int index = RandomUtils.nextInt(0, elements.size());
            webElement = elements.get(index);

            //过滤网站
            String dataLog = webElement.getAttribute("data-log");
            String[] ignoreClicks = ConfigHelper.getProperty("ignoreClick").split("\\|");
            boolean b = Arrays.stream(ignoreClicks).noneMatch(dataLog::contains);
            if (b) {
                break;
            } else {
                webElement = null;
            }
        }
        if (null == webElement) {
            LOGGER.info("没有结果可浏览");
            return;
        }

        LOGGER.info("准备浏览 {}", webElement.getText());


        //页面定位到元素
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", webElement);
        delay(1000);

        //点击进去
        LOGGER.info("点击浏览");
        webElement.click();
        delay(3000);
        String title = driver.getTitle();
        LOGGER.info("成功浏览 {}", title);

    }

    @Override
    public boolean checkPage(GoniubChromeDriver driver) {
        return !driver.getTitle().contains("百度安全验证");
    }
}
