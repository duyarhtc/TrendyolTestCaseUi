package Trendyol;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.IOException;
import  java.util.Random;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AutomationTrendyolCase {

    public static Logger logger;

    public static void main(String[] args) throws InterruptedException{

        System.out.println("Lütfen kullanacağınız browser için sayı giriniz.1:Chrome,2:Firefox");
        Scanner scanner = new Scanner(System.in);
        String browsername=scanner.nextLine();
        WebDriver driver = createDriver(browsername);

        if (driver==null) {
            System.out.println("Lütfen browser seçimi için tanımlı sayılardan birini giriniz !");
        }
        else
        {
            //Not: Her case i ayrı ayrı deneyebilrisiniz.
            //case 2.1
            //filterProductSendBasket(driver);
            //case 2.2
            //favoriteProductAndSendBasket(driver);
            //case 3
            controlTabComponent(driver);
        }
    }

    public static WebDriver createDriver(String browsername){

        WebDriver driver;

       //chrome tarayıcı seçimi
       if(browsername.equals("1"))
       {
           System.setProperty("webdriver.chrome.driver", "src\\webDrivers\\chromedriver.exe");
           driver = new ChromeDriver();
           return driver;
       }
       //firefox tarayıcı seçimi
       else if(browsername.equals("2"))
       {
               System.setProperty("webdriver.gecko.driver","src\\webDrivers\\geckodriver.exe");
               driver=new FirefoxDriver();
               return driver;
       }

       else return null;
    }

    public static void login(WebDriver driver) throws InterruptedException{
        driver.get("https://www.trendyol.com/");
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        //kullanıcı giriş bilgileri
        String email="testtrendyol100@gmail.com";
        String password="testtrendyol100";
        driver.findElement(By.className("modal-close")).click();
        driver.findElement(By.className("link-text")).click();
        WebElement elementEmail = driver.findElement(By.id("login-email"));
        WebElement elementPassword=driver.findElement(By.id("login-password-input"));
        elementEmail.click();
        elementEmail.sendKeys(email);
        elementPassword.click();
        elementPassword.sendKeys(password);
        driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/div[1]/form/button/span")).click();
        Thread.sleep(5000);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        System.out.println("Giriş İşlemi başarılı bir şekilde yapılmıştır!");

    }

    public static void filterProductSendBasket(WebDriver driver)throws InterruptedException{
        login(driver);
        //Oyuncu bilgisayarı için arama işlemi
        WebElement elementSearchBox= driver.findElement(By.className("search-box"));
        elementSearchBox.click();
        elementSearchBox.sendKeys("Oyuncu Bilgsayarı");
        Thread.sleep(2000);
        elementSearchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        driver.navigate().refresh();//çıkan popo up ı kaldırmak için.
        driver.findElement(By.xpath("/html/body/div[1]/div[4]/div/div/div/div/div[1]/div/div/div[2]/input")).sendKeys("Monster");

        //markaya göre filtreleme değerinin seçilmesi
        WebElement  elementMark=driver.findElement(By.xpath("/html/body/div[1]/div[4]/div/div/div/div/div[1]/div/div/div[2]/div[2]/a/div[2]"));
        if(elementMark.getText().equals("Monster"))
        {
            Thread.sleep(2000);
            Actions act=new Actions(driver);
            act.moveToElement(elementMark).click().build().perform();
            Thread.sleep(2000);

        }
        //fiyat bandına göre filtreleme değerlerinin seçilmesi
        driver.findElement(By.xpath("/html/body/div[1]/div[4]/div/div/div/div/div[1]/div/div/div[3]/div[2]/div/input[1]")).sendKeys("3000");
        driver.findElement(By.xpath("/html/body/div[1]/div[4]/div/div/div/div/div[1]/div/div/div[3]/div[2]/div/input[2]")).sendKeys("10000");
        driver.findElement(By.className("fltr-srch-prc-rng-srch")).click();
        Thread.sleep(2000);

        //filtreleme sonrası seçilen herhangi bir ürünün sepete eklenmesi
        List<WebElement> elements= driver.findElements(By.className("add-to-basket-button"));
        Random random=new Random();
        int randomNumber=random.nextInt(elements.size());
        elements.get(randomNumber).click();
        System.out.println("Product added to basket");


    }



    public static void favoriteProductAndSendBasket(WebDriver driver)throws InterruptedException{

        login(driver);

        //gömlek ürünleri için arama işlemi
        WebElement elementSearchBox= driver.findElement(By.className("search-box"));
        elementSearchBox.click();
        elementSearchBox.sendKeys("Gömlek");
        elementSearchBox.sendKeys(Keys.ENTER);
        Thread.sleep(4000);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

        //listelenen gömleklerden herhangi birinin favori olarak eklenmesi
        List<WebElement> elements= driver.findElements(By.className("fvrt-btn"));
        Random random=new Random();
        int randomNumber=random.nextInt(elements.size()-20);
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(elements.get(randomNumber)));
        Actions act=new Actions(driver);
        act.moveToElement(elements.get(randomNumber));
        Thread.sleep(2000);
        elements.get(randomNumber).click();
        driver.navigate().refresh();
        Thread.sleep(2000);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

        //favori Listesinin açılması
        driver.findElement(By.xpath("/html/body/div[1]/div[1]/div/div[2]/div/div/div[3]/div/div/div/a/div/p")).click();
        Thread.sleep(5000);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        //favori ürünün sepete eklenmesi için sayfanın kaydırılması
        driver.manage().window().maximize();
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,250)");
        Thread.sleep(4000);
        WebElement elementBasketButton=driver.findElement(By.xpath("/html/body/div[1]/div[3]/div/div/div[3]/div/div/div[3]/div[3]"));
        //new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(elementBasketButton));
        Actions act2=new Actions(driver);
        act2.moveToElement(elementBasketButton);
        Thread.sleep(2000);
        elementBasketButton.click();

        //ürünün sepete eklenmeden önce zorunlu kılınan beden seçiminin yapılması
        WebElement elementDropdown=driver.findElement(By.xpath("/html/body/div[1]/div[3]/div/div/div[3]/div/div/div[3]/div[1]/div"));
        List<WebElement> options = elementDropdown.findElements(By.tagName("li"));
        for (WebElement option:options)
        {
            //tükenen bedenin sepete eklenemesinden dolayı kontrolünün yapılması
            if (!option.getText().contains("Tükendi"))
            {
                Actions act3=new Actions(driver);
                act3.moveToElement(option);
                Thread.sleep(2000);
                option.click();
                Thread.sleep(2000);
                System.out.println("Favori ürün beden seçimi yapılarak sepete gönderilmiştir.");
                break;
            }

        }

    }

    public static void controlTabComponent(WebDriver driver)throws  InterruptedException{
        login(driver);

        //Anasayfada tab listesinin oluşturulması
        WebElement tabUl= driver.findElement(By.className("main-nav"));
        List<WebElement> tabAllList=tabUl.findElements(By.className("category-header"));

        for( int i=1;i<=tabAllList.size();i++)
        {

            driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
            driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/div[1]/nav/ul/li["+i+"]/a")).click();
            Thread.sleep(5000);

            //tab alalnının ilk component/butiğinin açılması
            List<WebElement> allComponents=driver.findElements(By.className("component-item"));
            allComponents.get(0).click();
            driver.navigate().refresh();
            Thread.sleep(4000);

            //açılan butiğin ilk 4 fotrağının yüklenmes işleminin çek edilmesi ve durumun log doyasına kayıt edilmesi.
            List<WebElement> imageList=driver.findElements(By.className("p-card-img"));
            System.out.println("image count"+imageList.size());
            for(int k=0;k<4;k++){
                Boolean isImageLoaded = (Boolean) ((JavascriptExecutor)driver).executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", imageList.get(k));
                String record="image check "+imageList.get(k).getAttribute("src") +":"+isImageLoaded;
                //log doyasına kayıt işlemin yapılması.
                logFile(record);


            }

        }
        System.out.println("Tüm tabler için işlem başarı ile tammalanmıştır !");

    }

    public static void logFile(String record)throws  InterruptedException{
        logger=Logger.getLogger("LogFile");
        FileHandler fh;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try
        {
            fh = new FileHandler("LogFile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.info(dtf.format(now)+" "+record);

        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

