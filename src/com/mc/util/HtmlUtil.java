package com.mc.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;

public class HtmlUtil {

	// ���ظñ�ǩ������
	public static String getHtmlTag(HttpURLConnection httpURLConnection,
			String s) {
		NodeList nodes;
		String result = "error";
		try {
			Parser parser = new Parser(httpURLConnection);
			NodeFilter filter = new TagNameFilter(s);
			nodes = parser.extractAllNodesThatMatch(filter);
			if (nodes != null) {
				for (int i = 0; i < nodes.size(); i++) {
					Node textnode = (Node) nodes.elementAt(i);
					result = textnode.toPlainTextString();
				}
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*System.out.println("==============fdf==========================");
		System.out.println(result);*/
		return result;
	}

	/**
	 * ��ȡtittle
	 */
	public static String getHtmlTittle(String html) {
		String tittle = "error";
		try {
			Parser parser = new Parser(html);
			/*
			 * Parser parser = new Parser( httpURLConnection);
			 */
			parser.setEncoding("utf8");
			HtmlPage htmlPage = new HtmlPage(parser);
			parser.visitAllNodesWith(htmlPage);
			tittle = htmlPage.getTitle();
//			System.out.println(tittle);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("==============fdf==========================");
		System.out.println(tittle);
		return tittle;
	}

	/**
	 * ��ȡbody
	 */
	public static String getHtmlBody(String html) {
		String body = "error";
		try {
			Parser parser = new Parser(html);
			/*
			 * Parser parser = new Parser( httpURLConnection);
			 */
			parser.setEncoding("utf8");
			HtmlPage htmlPage = new HtmlPage(parser);
			parser.visitAllNodesWith(htmlPage);
			body = htmlPage.getBody().asString();
			body = body.replaceAll("[��\\t\\n\\r\\f(&nbsp;|gt) ]+", " ");
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*System.out.println("==============fdf==========================");
		System.out.println(body);*/
		return body;
	}

	/**
	 * д�ļ�
	 */

	public static void writeHtml(String html) {
		String s = html;
		FileWriter fw = null;
		File f = new File(FilePathUtil.htmlPath);
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			fw = new FileWriter(f);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(s, 0, s.length() - 1);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end");
	}

	/**
	 * ����tr
	 */
	public static String parseHtmlTR(String html, String displaymode) {
		String body = "";
		try {
			Parser parser = new Parser(html);
			/*
			 * Parser parser = new Parser( httpURLConnection);
			 */
			parser.setEncoding("utf8");
			NodeList nodeList = parser
					.extractAllNodesThatMatch(new TagNameFilter("TR"));
			System.out.println("��ӡ������Ϣ");
			if (displaymode.equals("NEWS")) {// ��ҳ
				for (int i = 10; i < 18; i++) { // for (int i = 0; i <
												// nodeList.size(); i++) { ��ȡ��ҳ
												// ������Ϣ �ӵ�10 �� ��17��
					// ��ȡtr
					TableRow trt = (TableRow) nodeList.elementAt(i);
					// ��ȡtr�ӽڵ������
					// System.out.println(trt.toPlainTextString());
					
					body = body + trt.toPlainTextString();
					body = body.replaceAll("[��\\t\\r\\f(&nbsp;|gt) ]+", " ");
					body = body+"\n";
				}
			}
			if (displaymode.equals("PAID")) {// �ɷѼ�¼
				for (int i = 19; i < 24; i++) { // for (int i = 0; i <
												// nodeList.size(); i++) { ��ȡ��ҳ
												// ������Ϣ �ӵ�10 �� ��17��
					// ��ȡtr
					TableRow trt = (TableRow) nodeList.elementAt(i);
					// ��ȡtr�ӽڵ������
//					 System.out.println(trt.toPlainTextString());
					body = body + trt.toPlainTextString()+"\n";
					body = body.replaceAll("[��\\t\\r\\f(&nbsp;|gt) ]+", " ");
					body = body+"\n";
				}
			}
			if (displaymode.equals("LD")) {// ��¼��¼
				for (int i = 19; i < 24; i++) { // for (int i = 0; i <
												// nodeList.size(); i++) { ��ȡ��ҳ
												// ������Ϣ �ӵ�10 �� ��17��
					// ��ȡtr
					TableRow trt = (TableRow) nodeList.elementAt(i);
					// ��ȡtr�ӽڵ������
					body = body + trt.toPlainTextString()+"\n";
					body = body.replaceAll("[��\\t\\r\\f(&nbsp;|gt) ]+", " ");
					body = body+"\n";
				}
			}
			if (displaymode.equals("UD")) {// ��ϸ����
				for (int i = 21; i < nodeList.size()-1; i++) { // for (int i = 0; i <
												// nodeList.size(); i++) { ��ȡ��ҳ
												// ������Ϣ �ӵ�10 �� ��17��
					// ��ȡtr
					TableRow trt = (TableRow) nodeList.elementAt(i);
					// ��ȡtr�ӽڵ������
					body = body + trt.toPlainTextString()+"\n";
					body = body.replaceAll("[��\\t\\r\\f(&nbsp;|gt) ]+", " ");
					body = body+"\n";
				}
			}
			// System.out.println(body);
			System.out.println("===================================");
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * System.out.println("==============fdf==========================");
		 * System.out.println(body);
		 */
		return body;
	}
}
