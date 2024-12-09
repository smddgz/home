package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.entity.ArticleInfo;
import org.example.entity.CryptoInfo;
import org.example.mapper.CryptoMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class CryptoService extends ServiceImpl<CryptoMapper,CryptoInfo> {
    @Autowired
    CryptoMapper cryptoMapper;

    public void save()   {
        for (int j = 0; j < 5; j++) {
            Thread thread = new Thread(() -> {
                ArrayList<CryptoInfo> list = new ArrayList<>();
                for (int i = 0; i < 10000; i++) {
                    CryptoInfo cryptoInfo = new CryptoInfo();
                    cryptoInfo.setInstId("BTC-USDC");
                    cryptoInfo.setLast("70000");
                    cryptoInfo.setCreatedDate(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
                    list.add(cryptoInfo);
                }
                saveBatch(list);
            });
            thread.start();
        }
    }

    private static final String announcementUrl="https://www.binance.com/zh-CN/support/announcement/%E6%95%B0%E5%AD%97%E8%B4%A7%E5%B8%81%E5%8F%8A%E4%BA%A4%E6%98%93%E5%AF%B9%E4%B8%8A%E6%96%B0?c=48&navId=48";
//    private static final String announcementUrl="http://localhost:9090/msg";
    private static CloseableHttpClient client = HttpClients.createDefault();
    public List<ArticleInfo> getAnnouncementInfo(String body){
        ArrayList<ArticleInfo> list = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(body);
            Element content = doc.getElementById("__APP_DATA");
            if (content!=null){
                ObjectMapper om = new ObjectMapper();
                Node node = content.childNode(0);
                String s = node.toString();
                JsonNode jsonNode = om.reader().readTree(s);
                JsonNode appState = jsonNode.get("appState");
                JsonNode loader = appState.get("loader");
                JsonNode dataByRouteId = loader.get("dataByRouteId");
                JsonNode d9b2 = dataByRouteId.get("d34e");
                JsonNode catalogs = d9b2.get("catalogDetail");
                JsonNode articles = catalogs.get("articles");
                for (JsonNode article : articles) {
                    JsonNode info = om.reader().readTree(article.toString());
                    list.add(new ArticleInfo(info.get("title").toString(),info.get("releaseDate").asLong()));
                }
            }
//            log.info(LocalDateTime.now() +":"+list.size());
        } catch (IOException e) {
            log.info(LocalDateTime.now() +":"+e.getMessage());
        }
        return list;
    }

    public void test(){
        System.out.println(12111);
    }

    public static void main(String[] args) {
    }
    @Autowired
    MailService mailService;
    static AtomicLong latestTimeStamp=new AtomicLong(0);
    static long time=0;
    public void announcementJob(){
        log.info("job");
        String announceFromApi = getAnnounceFromApi();
        if(!StringUtils.hasText(announceFromApi)){
            return;
        }
        List<ArticleInfo> list = getAnnouncementInfo(announceFromApi);


        if (!list.isEmpty()) {
            ArticleInfo articleInfo = list.get(0);
            Long releaseDate = articleInfo.getReleaseDate();
            if (sendMailFlag(releaseDate)) {
                StringJoiner joiner = new StringJoiner("<br>");
                joiner.add(articleInfo.getTitle()+LocalDateTime.ofInstant(Instant.ofEpochMilli(releaseDate), ZoneId.systemDefault()));
                System.out.println(joiner);
                try {
                    mailService.sendMail(joiner.toString());
                } catch (MessagingException e) {
                    log.error("failed to send email:{}",e.getMessage());
                }
            }
        }
    }

    public String getAnnounceFromApi(){
        try {
            HttpGet httpGet = new HttpGet(announcementUrl);
            CloseableHttpResponse response = client.execute(httpGet);
            byte[] bytes = EntityUtils.toByteArray(response.getEntity());
            return new String(bytes);
        } catch (IOException e) {
            log.error("failed to fetch binance announcement content");
        }
        return "";
    }

    static final Object lock=new Object();

    public boolean sendMailFlag(long releaseTime){
        synchronized (lock) {
            if(time==0){
                time=releaseTime;
                return false;
            }

            if (releaseTime>time) {
                time=releaseTime;
                return true;
            }
            return false;
        }
    }
}
