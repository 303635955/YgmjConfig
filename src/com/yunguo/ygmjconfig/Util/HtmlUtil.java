package com.yunguo.ygmjconfig.Util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class HtmlUtil {
	static public String GetHtml(String path) {
		String html = "";
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();
			byte[] data = readInputStream(inStream);// �õ�html�Ķ���������
			html = new String(data, "UTF-8");
		} catch (Exception e) {
			Log.i("error",e.toString());
		}
		return html;
	}

	static private byte[] readInputStream(InputStream inStream)
			throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}
	
	
	static public String PostStringToUrl(String urlStr, Map<String,String> textMap){
        String res = "";
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            StringBuffer strBuf = new StringBuffer();
            if(textMap != null){
                Iterator iter = textMap.entrySet().iterator();
                while(iter.hasNext()){
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append(inputName).append("=").append(URLEncoder.encode(inputValue, "UTF-8"));
                }
                String ss = strBuf.toString();
                out.write(ss.getBytes());
            }
            out.flush();
            out.close();
                        
            strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            String ress = strBuf.toString();
            res = URLDecoder.decode(ress, "UTF-8");
            reader.close();
            reader = null;
        } catch (MalformedURLException ex) {

        } catch (IOException ex) {
        }
        catch(Exception ex){
        	
        }
        finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }
	
	static public String PostStringToUrl(String urlStr, String paramStr){
		return PostStringToUrl(urlStr,paramStr,"param");
	}
	
	static public String PostStringToUrl(String urlStr, String paramStr,String key) {
		
		
		System.out.println(urlStr+"========url");
		String res = "";
		String params = key + "=" + paramStr;
		try {  
			System.out.println(params+"========params");
			byte[] data = params.getBytes();
			URLEncoder.encode(params,"GBK") ;
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", data.length + "");
			
			conn.setDoOutput(true);
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			out.write(data);
			out.flush();
			out.close();

			StringBuffer strBuf = new StringBuffer();
			strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			String ress = strBuf.toString();
			res = URLDecoder.decode(ress, "UTF-8");
			reader.close();
			reader = null;
		} catch (Exception ex) {

		}
		System.out.println(params+"========params");
		System.out.println(urlStr+"返回数据=========="+res);
		return res;
	}

}
