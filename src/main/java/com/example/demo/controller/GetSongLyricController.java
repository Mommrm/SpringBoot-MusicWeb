package com.example.demo.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@CrossOrigin
@RestController
public class GetSongLyricController {


    @PostMapping("/getlyric")
    public String getLyric(@RequestParam("songId") String songId) throws IOException {
        String lyrics = "";
        String url = "http://music.163.com/api/song/media?id=" + songId;

        try{
            URL apiUrl = new URL(url);
            // 创建HttpURLConnection对象
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            // 设置请求方法
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取响应数据
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                // 解析JSON数据
                String json = response.toString();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(json);
                lyrics = jsonNode.get("lyric").asText();
                System.out.println(lyrics);
                return lyrics;
            } else {
                System.out.println("请求失败，错误代码：" + responseCode);
                return "无歌词，请欣赏!";
            }
        }catch (Exception e){
            System.out.println("捕获异常" + e);
            return null;
        }
    }
}
