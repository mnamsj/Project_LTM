package com.ltm.web.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

@Service
public class SongImageApi {
	
	public List<String> getImage(String songTitle, String singer){

	// 인증키
	String apiKey = "e63d38bc9de5863a4bbdfa74a087ea38";

	String encodeSinger = "";
	String encodeSongTitle = "";

	try
	{
		encodeSinger = URLEncoder.encode(singer, "UTF-8");
		encodeSongTitle = URLEncoder.encode(songTitle, "UTF-8");
		// url 객체 생성
		URL url = new URL("https://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=" + apiKey + "&artist="
				+ encodeSinger + "&track=" + encodeSongTitle + "&format=json");

		// 요청하고자 하는 url과 통신하기 위한 connection 객체 생성
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// 통신을 위한 메소드 SET
		conn.setRequestMethod("GET");

		// 통신을 위한 Content-type SET
		conn.setRequestProperty("Content-type", "application/json");

		// 통신코드 응답 확인 - 200 뜨면 성공한거임
		System.out.println("Response code: " + conn.getResponseCode());

		// 전달받은 데이터를 BufferedReader 객체로 저장
		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}

		// 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장
		StringBuilder sb = new StringBuilder();
		String result;
		while ((result = rd.readLine()) != null) {
			sb.append(result);
		}

		// 객체 해제
		rd.close();
		conn.disconnect();

		// 문자열 형태의 JSON을 파싱하기 위한 JSONParser 객체 생성.
		JSONParser parser = new JSONParser();
		// 문자열을 JSON 형태로 JSONObject 객체에 저장
		JSONObject obj = (JSONObject) parser.parse(sb.toString());
		// 필요한 리스트 데이터 부분만 가져와 JSONArray로 저장
		JSONObject track = (JSONObject) obj.get("track");
		JSONObject album = (JSONObject) track.get("album");

		String smallText = "";
		String largeText = "";
		
		if(album == null) {	//api에 앨범 태그가 없다면 		
			smallText = null;
			largeText = null;
			
		}else if(album != null) {
	
			JSONArray image = (JSONArray) album.get("image");

			JSONObject smallImage = (JSONObject) image.get(0);
			JSONObject largeImage = (JSONObject) image.get(3);
			smallText = (String) smallImage.get("#text");
			largeText = (String) largeImage.get("#text");
		}
				
		List<String> imageArray = new ArrayList<>();
		
		imageArray.add(smallText);
		imageArray.add(largeText);
		
		
		return imageArray; 
		
		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
	}
}
