package com.ltm.web.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

@Service
public class SearchResultApi {

public JSONArray songResults(String songInfo){
		
		//인증키
		String apiKey = "e63d38bc9de5863a4bbdfa74a087ea38";
		
		String encodeSongInfo = "";
		
		try {
			encodeSongInfo = URLEncoder.encode(songInfo,"UTF-8");
			//url 객체 생성
			URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=track.search&track=" 
				+ encodeSongInfo + "&api_key=" + apiKey + "&format=json&limit=20");
			
			//요청하고자 하는 url과 통신하기 위한 connection 객체 생성
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			//통신을 위한 메소드 SET
			conn.setRequestMethod("GET");
			
			//통신을 위한 Content-type SET
			conn.setRequestProperty("Content-type", "application/json");
			
			//통신코드 응답 확인 - 200 뜨면 성공한거임
			System.out.println("Response code: " + conn.getResponseCode());
			
			//전달받은 데이터를 BufferedReader 객체로 저장
			BufferedReader rd;
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			
			//저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장
			StringBuilder sb = new StringBuilder();
			String result;
			while((result = rd.readLine()) != null) {
				sb.append(result);
			}
			
			//객체 해제
			rd.close();
			conn.disconnect();
			
			
			//문자열 형태의 JSON을 파싱하기 위한 JSONParser 객체 생성.
			JSONParser parser = new JSONParser();
			//문자열을 JSON 형태로 JSONObject 객체에 저장
			JSONObject obj = (JSONObject) parser.parse(sb.toString());
			//필요한 리스트 데이터 부분만 가져와 JSONArray로 저장
			JSONObject results = (JSONObject) obj.get("results");
			JSONObject trackMatches = (JSONObject) results.get("trackmatches");
			
			JSONArray track = (JSONArray) trackMatches.get("track");
			return track;
//			String songTitle = "";
//			String singer = "";
//			
//			for (int i = 0; i < 5; i++) {
//				JSONObject trackName = (JSONObject) track.get(i);
//				songTitle += trackName.get("name");
//				
//				JSONObject trackArtist = (JSONObject) track.get(i);
//				singer += trackArtist.get("artist");	
//			
//				System.out.println("제목: " + songTitle + "\n" +
//								   "가수: " + singer);	
//			}		
			
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}		
		return null;
	}
}
