package net.twaiku;

import java.io.IOException;

import net.twaiku.cmu.CmuDictionary;
import net.twaiku.rhymer.Rhymer;

public class HaikuSyllableCoutingFromArray {

	public static boolean count(String[] words,Rhymer rhymer) throws IOException {

		//int[] arr = { 1, 1, 3, 3, 2, 1, 1, 1, 1, 3 };
		int i = 0;
		int syllableCount = 0;
		final int line1 = 5;
		final int line2 = 12;
		final int line3 = 17;
		int firstSyllableNum=0;
		int secondSyllableNum=0;
		int thirdSyllableNum=0;

		// Starting point
		for (i = 0; syllableCount <= 17; i++) {
			if (words.length>i) {
			//Use placeholder vars such that we only 
				firstSyllableNum = rhymer.getSyllables(words[i]);
				if (firstSyllableNum == 0) {
					System.out.println("String" + words[i] + "not found");
					return false;
				};
			}
			if (syllableCount == line1) {

				System.out.println("first 5 check");
				for (int j = i; syllableCount <= 17; j++) {
					if (words.length>j) {
						secondSyllableNum = rhymer.getSyllables(words[j]);
						if (secondSyllableNum == 0) {
							System.out.println("String" + words[j] + "not found");
							return false;
						};
					}
					if (syllableCount == line2) {

						System.out.println("second 5 check");
						for (int r = j; syllableCount <= 17; r++) {
							if (words.length>r) {
								thirdSyllableNum = rhymer.getSyllables(words[r]);
								if (thirdSyllableNum == 0) {
									System.out.println("String" + words[r] + "not found");
									return false;
								};
							}
							if (syllableCount == line3&&(words.length-1==r)) {
								System.out.println("We FUCKING DID IT");
								return true;
							} else if ((syllableCount + thirdSyllableNum) > line3) {
								System.out.println("failed second 5 check");
								return false;
							} else {
								syllableCount += thirdSyllableNum;
								System.out.println(syllableCount);
							}
						}

					} else if ((syllableCount + secondSyllableNum) > line2) {
						System.out.println("failed 7 check");
						return false;
					} else {
						syllableCount += secondSyllableNum;
						System.out.println(syllableCount);
					}

				}

			} else if ((syllableCount + firstSyllableNum) > line1) {
				System.out.println("failed line1 test");
				return false;
			} else {
				syllableCount += firstSyllableNum;
				System.out.println(syllableCount);

			}
		}
		return false;

	}
}