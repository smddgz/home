package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    private static final String restartScript="/root/";
    private static final String filepath="/root/file/";
//    private static final String filepath="d:/test/";
    @ResponseBody
    @GetMapping("/restart")
    public String restart(){
        try {
            Runtime.getRuntime().exec(restartScript+"home.sh");
            return "success";
        } catch (IOException e) {
            return "error";
        }
    }

    @GetMapping
    public ModelAndView index(){
        File file = new File(filepath);
        if(!file.exists()){
            file.mkdirs();
        }
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("files",file.listFiles());
        return mv;
    }

    @PostMapping("/upload")
    public void upload(HttpServletResponse response,MultipartFile file) throws IOException {
        System.out.println(LocalDateTime.now());
        File path = new File(filepath);
        if (!path.exists()){
            path.mkdirs();
        }
        ModelAndView mv = new ModelAndView("index");
        String originalFilename = file.getOriginalFilename();
        try {
            file.transferTo(new File(filepath+originalFilename));
            mv.addObject("msg","success");
        } catch (IOException e) {
            mv.addObject("msg","error");
        }
        mv.addObject("files",path.listFiles());
        response.sendRedirect("/");
        System.out.println(LocalDateTime.now());
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response, String name){
//        response.addHeader(HttpHeaders.CONTENT_DISPOSITION,"inline");
        try {
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename="+ URLEncoder.encode(name, StandardCharsets.UTF_8));
            byte[] bytes = Files.readAllBytes(Paths.get(filepath, name));
            response.addHeader(HttpHeaders.CONTENT_LENGTH,Integer.toString(bytes.length));
            response.getOutputStream().write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
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



