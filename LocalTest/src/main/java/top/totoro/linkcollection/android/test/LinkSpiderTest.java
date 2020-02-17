package top.totoro.linkcollection.android.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import spider.LinkSpider;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.UrlUtils;

public class LinkSpiderTest implements PageProcessor {

    private Site site;
    private String title;

    public String getTitle() {
        return title;
    }

    private static final String UA = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31";

    public LinkSpiderTest(String link) {
        site = Site.me()
                .addStartUrl(link)
                .setDomain(UrlUtils.getDomain(link))
                .setUserAgent(UA);
        Spider.create(this).run();
    }

    public Selectable html;

    @Override
    public void process(Page page) {
        html = page.getHtml();
        title = html.xpath("//title").toString();
        System.out.println(title);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
//        Callable callable = new Callable() {
//            @Override
//            public Object call() throws Exception {
//                LinkSpiderTest spider = new LinkSpiderTest("http://totoro-dev.top");
//                System.out.println(spider.getTitle());
//                return spider.getTitle();
//            }
//        };
//        ScheduledFuture future = Executors.newScheduledThreadPool(1).schedule(callable,0, TimeUnit.MILLISECONDS);
//        try {
//            System.out.println(future.get());
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        LinkSpider spider = new LinkSpider("http://totoro-dev.top");
        System.out.println(spider.getTitle());
    }
}
