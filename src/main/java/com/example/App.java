package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Set;

public class App {
    public static void main(String[] args) throws InterruptedException {

        // открыть браузер в полном экране (аргумент kiosk если полноэкранный без рамки)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        WebDriver driver = new ChromeDriver(options);

        // открыть яндекс
        driver.get("https://market.yandex.ru/");

        // поиск "Сотовые телефоны"
        driver.findElement(By.xpath("//input[@placeholder='Искать товары']")).click();
        driver.findElement(By.xpath("//input[@placeholder='Искать товары']")).sendKeys("Сотовые телефоны");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        // расширенный поиск
        driver.findElement(By.xpath("//*[@data-auto='allFiltersButton']")).click();

        // до 20000 рублей и Диагональ экрана от 3 дюймов.
        driver.findElement(By.xpath("(//*[@data-auto='range-filter-input-max'])[1]")).sendKeys("20000");

        driver.findElement(By.xpath("//*[contains(text(),'Диагональ экрана (точно),')]")).click();
        driver.findElement(By.xpath("(//*[@data-auto='range-filter-input-min'])[2]")).sendKeys("3");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // 5 производителей
        String[] brand = { "Apple", "Samsung", "Nokia", "HUAWEI", "Xiaomi" };
        for (int counter = 0; counter < brand.length; counter++) {
            driver.findElement(By.xpath("(//*[@class='_34OG2'])[1]")).click();
            driver.findElement(By.xpath("(//*[@class='_34OG2'])[1]")).sendKeys(brand[counter]);
            ExpectedConditions.visibilityOfElementLocated(By.id("//*[@class='_1WMsA _1VtMI _176_6'])[1]"));
            driver.findElement(By.xpath("(//*[@class='_1WMsA _1VtMI _176_6'])[1]")).click();
            driver.findElement(By.xpath("//*[@aria-label='Сбросить']")).click();

        }
        // яндекс считает за бота, нужно время для решения каптчи
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.findElement(By.xpath("//*[@data-auto='result-filters-link']")).click();

        // долистать до 10 страницы
        for (;;) {
            WebElement page = driver.findElement(By.xpath("(//*[@class='Xe4rX _18sEx _3-NJO'])[1]"));
            String extractedPage = page.getText();
            if (!extractedPage.equals("10")) {
                driver.findElement(By.xpath("//*[@data-auto='pagination-next']")).click();
                new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                        ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Показать ещё')]")));
            } else
                break;
        }

        // Запомнить название
        WebElement name = driver.findElement(By.xpath("(//*[@class='egKyN _2Fl2z']/child::*)[2]"));
        String extractedName = name.getText();
        driver.findElement(By.xpath("//*[contains(text(), '" + extractedName + "')]")).click();

        // Смена вкладок
        Set<String> OldWindow = driver.getWindowHandles();
        Set<String> currentWindows = driver.getWindowHandles();
        String NewWindow = null;
        for (String window : currentWindows) {
            if (!window.equals(OldWindow)) {
                NewWindow = window;
            }
        }
        driver.switchTo().window(NewWindow);

        // Оценка
        WebElement grade = driver.findElement(By.xpath("//*[@class='_2ri2x D7c4V _3aPfw _25vcL']"));
        String extractedGrade = grade.getText();
        System.out.println(extractedGrade);

        // 13. Закрыть браузер
        driver.quit();
    }

}
