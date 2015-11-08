package com.cmu.controller;

import java.util.List;

public class ControllerHelper {
	public static String createSemicolonSeparatedStringFromArray(List<String> array){
		StringBuilder nameBuilder = new StringBuilder();

		for (String n : array) {
			nameBuilder.append(n.replace("\"", "\'")).append("||");
		}

		nameBuilder.deleteCharAt(nameBuilder.length() - 1);
		nameBuilder.deleteCharAt(nameBuilder.length() - 1);

		return nameBuilder.toString();
	}
}
