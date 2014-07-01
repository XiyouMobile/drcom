package com.mc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.Proxy.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.htmlparser.tags.Html;

/**
 * 
 * @author Administrator
 * @description �ǵ��޸Ĵ��룬���ͷ�������Ӧ��ʱ����Ҫ fall back ����
 */
public class HttpUtil {
	// ����URL
	// public static final String BASE_URL="http://10.0.2.2:8080/ShopServer/";
	// public static final String BASE_URL = "http://192.168.137.1:8080/TuoC/";
	public static final String BASE_URL = "http://124.89.91.246/SelfServiceLogin.jsp";
	public static final String LOGIN_URL = "http://124.89.91.246/SelfServiceTemp.jsp";
	public static final String CHANGE_PW_URL = "http://124.89.91.246/SelfServiceChangePW"; // �޸�����
	public static final String MAIN_URL = "http://124.89.91.246/SelfServiceMain.jsp?displaymode=";// �ɷѼ�¼
	// public static String SERVER_ADDRESS="192.168.1.103";
	/*
	 * public static String SERVER_ADDRESS="192.168.11.1"; public static int
	 * SERVER_PORT = 8080;
	 */
	private static String sessionid;
	public static String CONNECT_EXCEPTION = "�����쳣��";

	/**
	 * ���Ո��/�޸��ܴa
	 * 
	 * @param url
	 * @param params
	 * @param cookie
	 * @return
	 */
	public static String http(String url, Map<String, String> params,
			String cookie) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		URL u = null;
		HttpURLConnection con = null;
		// �����������
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (Entry<String, String> e : params.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue());
				sb.append("&");
			}
			// sb.substring(0, sb.length() - 1);
		}
		System.out.println("send_url:" + url);
		System.out.println("send_data:"
				+ sb.toString().substring(0, sb.length() - 1));
		// ���Է�������
		try {

			/*Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(
					"localhost", 8888));*/
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			// con.setRequestProperty("Content-Type",
			// "text/html;charset=ISO-8859-1");
			// con.setConnectTimeout(6000);// ����ӳ�6000����
			con.setRequestProperty("Cookie", cookie);
			con.setInstanceFollowRedirects(false);
			/*
			 * if (new Integer(con.getResponseCode()).toString().equals("302"))
			 * {//��ȡ״̬�� String newurl = con.getHeaderField( "location" );
			 * con.disconnect(); String s = HttpUtil.gethttp(newurl,cookie);
			 * System.out.println("�ض���"+newurl + "\n "+s); }else{
			 */
			/*
			 * String cookieval = con .getHeaderField("set-cookie");
			 * System.out.println(cookie+" "+cookieval);
			 */
			// con.setRequestProperty("Content-Length", "0");
			// ��ȡURLConnection�����Ӧ�������
			out = new PrintWriter(con.getOutputStream());
			// �����������
			out.print(sb.toString().substring(0, sb.length() - 1).toString());
			// flush������Ļ���
			out.flush();
			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			if (new Integer(con.getResponseCode()).toString().equals("302")) {// ��ȡ״̬��
				String newurl = con.getHeaderField("location");
				con.disconnect();
				result = HttpUtil.gethttp(newurl, cookie, "NEWS");// �����ض����ҳ��
				System.out.println("�ض���" + newurl + "\n " + result);
			}
			// }
		} catch (Exception e) {
			System.out.println("���� POST ��������쳣��" + e);
			e.printStackTrace();
		}
		// ʹ��finally�����ر��������������
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String gethttp(String url, String cookie, String displaymode) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		URL u = null;
		HttpURLConnection con = null;
		System.out.println("send_url:" + url);
		// ���Է�������
		try {
			/*Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(
					"localhost", 8888));*/
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			// con.setRequestMethod("GET");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "text/html;charset=GB2312");
			// con.setConnectTimeout(6000);// ����ӳ�6000����
			con.setRequestProperty("Cookie", cookie);
			// if (url.equals(NEWS_URL)) {//����ǵ�¼ҳ��

			// }

			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}

			String s = HtmlUtil.getHtmlTittle(result);// ��ȡ��������
			System.out.println("��ӡ:" + s);

			if (s.equals("��¼ҳ��")) {// �ص���ҳ ��¼ʧ��
				return "��֤����";
			} else {
				if (s.equals("selfserviceerror")) {// ������� �����˺Ŵ��� ����Ǹ�������Ļ� ����
					String str = HtmlUtil.getHtmlBody(result).split("\\,")[0];// ��ȡ
																				// ����ԭ��
					return str;
				} else if (s.equals("�Է�����ҳ")) {// ��¼�ɹ�
					// ���ﷵ�� �Է���ҳ��� ������Ϣ
					// HtmlUtil.parseHtmlTR(result);
					return HtmlUtil.parseHtmlTR(result, displaymode);
				}
			}

		} catch (Exception e) {
			System.out.println("���� get ��������쳣��" + e);
			e.printStackTrace();
		}
		// ʹ��finally�����ر��������������
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * �޸��ܴa
	 */
	
}
