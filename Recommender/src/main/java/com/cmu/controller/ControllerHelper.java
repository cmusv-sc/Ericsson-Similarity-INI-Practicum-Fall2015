package com.cmu.controller;

import java.util.List;

public class ControllerHelper {
	
	//In order to have a better use of arrays in javascript, this function
	//receives an array and returns a String with the values from the array
	//divided by the characters "||"
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
