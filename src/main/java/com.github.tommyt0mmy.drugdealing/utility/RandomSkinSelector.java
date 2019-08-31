package com.github.tommyt0mmy.drugdealing.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomSkinSelector {
	private final static int SkinsCount = 7;

	private static List<String> SkinsList = new ArrayList<>(Arrays.asList(
			"a",
			"b",
			"c",
			"d",
			"e",
			"f",
			"g"
	));
	
	public static String getRandomSkinName() {
    	Random random = new Random();
    	int randomNumber = random.nextInt(SkinsCount);
    	
		return SkinsList.get(randomNumber);
	}

}
