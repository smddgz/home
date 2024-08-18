package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;

@Controller
public class IndexController {
    @ResponseBody
    @GetMapping("/time")
    public LocalDateTime time(){
        return LocalDateTime.now();
    }

    @GetMapping
    public String index(){
        return "index";
    }

    /**
     * {
     *   "v": "2",
     *   "ps": "23.145.56.47:21480",
     *   "add": "23.145.56.47",
     *   "port": 21480,
     *   "aid": 0,
     *   "type": "utp",
     *   "net": "kcp",
     *   "path": "",
     *   "host": "",
     *   "id": "e2fa7441-2c05-4431-969b-736d475adf76",
     *   "tls": "none"
     * }
     * @return string
     */
    @ResponseBody
    @GetMapping("/vmess")
    public String v2ray(){
//        String path= "config.json";
        String path="/etc/v2ray/config.json";
        ObjectMapper om = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = om.reader().readTree(new FileInputStream(path));
        } catch (IOException e) {
            return "io error";
        }
        String hostAddress;
        try {
            InetAddress localHost = Inet4Address.getLocalHost();
            hostAddress = localHost.getHostAddress();
        } catch (UnknownHostException e) {
            return "UnKnow host error";
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("v","2");
        map.put("ps","smddgz");
        map.put("add",hostAddress);
        map.put("port",jsonNode.get("inbounds").get(0).get("port"));
        map.put("aid",jsonNode.get("inbounds").get(0).get("settings").get("clients").get(0).get("alterId"));
        map.put("type",jsonNode.get("inbounds").get(0).get("streamSettings").get("kcpSettings").get("header").get("type"));
        map.put("net",jsonNode.get("inbounds").get(0).get("streamSettings").get("network"));
        map.put("path","");
        map.put("host","");
        map.put("id",jsonNode.get("inbounds").get(0).get("settings").get("clients").get(0).get("id"));
        map.put("tls",jsonNode.get("inbounds").get(0).get("streamSettings").get("security"));
        Base64.Encoder encoder = Base64.getEncoder();
        String link;
        try {
            link = "vmess://"+new String(encoder.encode(om.writeValueAsBytes(map)));
        } catch (JsonProcessingException e) {
            return "base64 error";
        }
        return new String(encoder.encode(link.getBytes()));
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    public static void main(String[] args) throws IOException {
        String s = new IndexController().v2ray();
        System.out.println(s);
    }
}



