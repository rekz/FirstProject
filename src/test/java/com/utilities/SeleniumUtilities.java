package com.utilities;

import java.io.File;

public class SeleniumUtilities {
	public void clearScreenshotsDirectory() {
		File path = new File("./Screenshots/");
		File[] files = path.listFiles();
		for (File file : files) {
			// System.out.println("Deleted filename :"+ file.getName());
			file.delete();
		}
		System.out.println("Deleted screenshots folders");
	}
}
