package com.vvsk.html2csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Test {
	public static void main(String args[]) throws Exception {

		// Scanner
		Scanner s = new Scanner(System.in);
		int t = s.nextInt();
		for (int j = 0; j < t; j++) {
			List<Long> costs = new ArrayList<Long>();
			List<List<Integer>> visits = new ArrayList<List<Integer>>();
			TreeMap<Long, Integer> profits = new TreeMap<Long, Integer>();
			int shops = s.nextInt();
			int people = s.nextInt();
			for (int i = 0; i < shops; i++) {
				costs.add(s.nextLong());
			}
			HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
			for (int i = 0; i < people; i++) {
				List<Integer> list = new ArrayList<Integer>();
				list.add(s.nextInt());
				list.add(s.nextInt());
				visits.add(list);
				profits.put(costs.subList(list.get(0) - 1, list.get(1)).stream().reduce(0L, Long::sum), i);
				for (int k = list.get(0); k <= list.get(1); k++) {
					if (hm.containsKey(k)) {
						hm.put(k, hm.get(k) + 1);
					} else {
						hm.put(k, 1);
					}
				}

			}
			int k = s.nextInt();
			maxProfit(k, profits, costs, hm);
		}

	}

	public static void maxProfit(int max, TreeMap<Long, Integer> source, List<Long> costs,
			HashMap<Integer, Integer> hm) {
		int count = 0;
		Long profit = 0L;
		for (Long entry : source.descendingKeySet()) {
			if (count >= max)
				break;
			else {
				count++;
				profit += entry;
			}
		}
		int maxT = 1;
		for (Map.Entry<Integer, Integer> entry : hm.entrySet()) {
			if (entry.getValue() > hm.get(maxT)) {
				maxT = entry.getKey();
			}

		}
		System.out.println(profit);
		System.out.println(maxT);
	}
}
