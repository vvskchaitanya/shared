package com.vvsk.xml2json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Xml2Json {

	public static void main(String args[]) throws IOException {
		String xmlFilePath = "";
		String xml = readFile(xmlFilePath);
		Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
		JSONObject json = getJSONObject(doc.getElementsByTag("SSRF").get(0));
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(json));

	}

	public static JSONObject getJSONObject(Element node) {
		JSONObject jsonObject = new JSONObject();
		node.attributes().forEach(attribute -> {
			if (!attribute.getKey().contains("xml")) {
				jsonObject.put("@" + attribute.getKey(), attribute.getValue());
			}
		});
		node.childNodes().forEach(childNode -> {
			if (childNode.nodeName().equals("#text") && node.childNodeSize() == 1) {
				jsonObject.put("#text", node.text());
			}
			if (!jsonObject.containsKey(childNode.nodeName()) && !childNode.nodeName().equals("#text")) {
				List<Element> elements = node.getElementsByTag(childNode.nodeName());
				if (elements.size() == 1) {
					jsonObject.put(childNode.nodeName(), getJSONObject(elements.get(0)));
				} else {
					JSONArray jsonArray = new JSONArray();
					for (Element element : elements) {
						jsonArray.add(getJSONObject(element));
					}
					jsonObject.put(childNode.nodeName(), jsonArray);
				}
			}
		});
		return jsonObject;
	}

	public static String readFile(String fileName) throws IOException {
		String text = "";
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		for (String line : lines)
			text += line;
		return text;
	}

}
