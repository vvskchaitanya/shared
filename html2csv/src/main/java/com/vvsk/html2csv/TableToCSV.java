package com.vvsk.html2csv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TableToCSV {

	public static void main(String[] args) {

		Document doc;
		try {
			String htmlFile = "test/test.html";
			String html = readFile(htmlFile);
			doc = Jsoup.parse(html);
			System.out.println(doc.text());
			Elements tables = doc.select("table");
			tables.forEach(table -> {
				String text = "";
				Elements rows = table.select("tr");
				for (Element row : rows)
					text += writeLine(row.children());
				if (table.hasAttr("id")) {
					Path path = Paths.get("test/table" + table.attr("id") + ".csv");
					try {
						System.out.println("Text :" + text);
						Files.write(path, text.getBytes());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String writeLine(Elements elements) {
		String text = "";
		if (!elements.isEmpty()) {
			System.out.println(elements.text());
			text += "\n" + elements.get(0).text();
			for (int i = 1; i < elements.size(); i++) {
				text += "," + elements.get(i).text();
			}
		}
		return text;
	}

	public static String readFile(String fileName) throws IOException {
		String text = "";
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		for (String line : lines)
			text += line;
		return text;
	}
}