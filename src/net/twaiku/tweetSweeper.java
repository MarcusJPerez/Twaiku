package net.twaiku;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider.Text;

import net.twaiku.cmu.CmuDictionary;
import net.twaiku.rhymer.Rhymer;
import net.twaiku.rhymer.RhymeResult;

public class tweetSweeper {

	private static final String[] tensNames = { "", " ten", " twenty", " thirty", " forty", " fifty", " sixty",
			" seventy", " eighty", " ninety" };

	private static final String[] numNames = { "", " one", " two", " three", " four", " five", " six", " seven",
			" eight", " nine", " ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen",
			" seventeen", " eighteen", " nineteen" };
	private static String[] badChars = { "@", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	private static String[] badWords = { "ass", "asses", "assface", "assfaces", "asshole", "assholes", "bastard", "bastards", "bitch",
			"bitches", "bitchy", "bullshit", "cocksucker", "cocksuckers", "cocksucking", "cunt", "cunts", "dickhead",
			"dickheads", "faggot", "faggots", "fuc", "fuck", "fucked", "fuckedup", "fucker", "fuckers", "fucking",
			"fuckoff", "fucks", "fuckup", "fuk", "fukker", "fukkers", "fuq", "goddamn", "goddamnit", "jackass",
			"jackasses", "motherfucker", "motherfuckers", "motherfucking", "nigger", "niggers", "pussy", "shit",
			"shithead", "shitheads", "shits", "shittier", "shittiest", "shitting", "shitty", "smartass", "smartasses",
			"tities", "tits", "titties", "wiseass", "wiseasses"};

	public static String[] sanitizeRawTweet(String rawTweetText) throws IOException {
		//In this method we split the raw twitter text into seperate strings for words based, remove punctuation,
		//look for some strings that CMU dictionary definitely won't have, and try to sanitize text for CMU dict
		//lookup as much as possible.
		//Return empty arraylist if contains haikuable words at some point. Otherwise, return sanitized text.
		ArrayList<String> list = new ArrayList<String>();
		
		String[] arry = rawTweetText.split(" ");
		
		if (rawTweetText.contains("http")) {
			return list.toArray(new String[list.size()]);
		}
		
		for (String word : rawTweetText.split(("[^a-zA-Z'’ʼ�@0-9]"))) {
			word = word.replaceAll("[’ʼ]", "'");
			if (!word.equals("")) {
				list.add(word);
			}
		}

		while ((list.get(0)).contains("@")) {

			list.remove(0);
		}
		//previous conditional checked for http when http check was first conditional in method 
		//while (list.get(list.size() - 1).contains("@") || list.get(list.size() - 1).contains("http")) {

		while (list.get(list.size() - 1).contains("@")) {
			list.remove(list.get(list.size() - 1));
		}

		//More redudancy that may be added back later if we end up removing first conditional
		//if (list.contains("@") || list.contains("http")) {
		for(String badWord:badChars) {
		if (list.contains(badWord)) {
			list.clear();
			return list.toArray(new String[list.size()]);
		}}
		/*
		for(String badWord:badWords) {
			for(int megadog = 0; megadog<list.size()-1; megadog++) {
			if (list.get(megadog).equalsIgnoreCase(badWord)) {
				list.clear();
				return list.toArray(new String[list.size()]);
		}}}*/
		
		
		/* Commented out since it cannot handle for numbers larger than ints
		 * (int k = 0; k < list.size(); k++) {
			if(isNumeric(list.get(k))) {
				if(Integer.parseInt(list.get(k)) < 1000) {
					list.set(k,convertLessThanOneThousand(Integer.parseInt(list.get(k))));
				}
				else if(Integer.parseInt(list.get(k)) >= 1000) {
					list.set(k,convert(Integer.parseInt(list.get(k))));
				}
			}
		} */
		
		
		return list.toArray(new String[list.size()]);
	}
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}

	public static int countNum(ArrayList<Integer> numList) {
		int totalCount = 0;

		for (int numCounter : numList) {
			totalCount = numCounter + totalCount;

		}
		return totalCount;

	}

	private static String convertLessThanOneThousand(int number) {
		String soFar;

		if (number % 100 < 20) {
			soFar = numNames[number % 100];
			number /= 100;
		} else {
			soFar = numNames[number % 10];
			number /= 10;

			soFar = tensNames[number % 10] + soFar;
			number /= 10;
		}
		if (number == 0)
			return soFar;
		return numNames[number] + " hundred" + soFar;
	}

	public static String convert(long number) {
		// 0 to 999 999 999 999
		if (number == 0) {
			return "zero";
		}

		String snumber = Long.toString(number);

		// pad with "0"
		String mask = "000000000000";
		DecimalFormat df = new DecimalFormat(mask);
		snumber = df.format(number);

		// XXXnnnnnnnnn
		int billions = Integer.parseInt(snumber.substring(0, 3));
		// nnnXXXnnnnnn
		int millions = Integer.parseInt(snumber.substring(3, 6));
		// nnnnnnXXXnnn
		int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
		// nnnnnnnnnXXX
		int thousands = Integer.parseInt(snumber.substring(9, 12));

		String tradBillions;
		switch (billions) {
		case 0:
			tradBillions = "";
			break;
		case 1:
			tradBillions = convertLessThanOneThousand(billions) + " billion ";
			break;
		default:
			tradBillions = convertLessThanOneThousand(billions) + " billion ";
		}
		String result = tradBillions;

		String tradMillions;
		switch (millions) {
		case 0:
			tradMillions = "";
			break;
		case 1:
			tradMillions = convertLessThanOneThousand(millions) + " million ";
			break;
		default:
			tradMillions = convertLessThanOneThousand(millions) + " million ";
		}
		result = result + tradMillions;

		String tradHundredThousands;
		switch (hundredThousands) {
		case 0:
			tradHundredThousands = "";
			break;
		case 1:
			tradHundredThousands = "one thousand ";
			break;
		default:
			tradHundredThousands = convertLessThanOneThousand(hundredThousands) + " thousand ";
		}
		result = result + tradHundredThousands;

		String tradThousand;
		tradThousand = convertLessThanOneThousand(thousands);
		result = result + tradThousand;

		// remove extra spaces!
		return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
	}
	

}
