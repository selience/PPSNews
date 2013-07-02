package com.pps.news.util;

import java.io.File;

public class FileUtils {

	/**
	 * 获取文件的后缀
	 * 
	 * @param file
	 *            文件File对象
	 * @return 文件扩展名
	 */
	public static String getFileExtension(File file) {
		if (file != null) {
			String fileName = file.getName();
			int i = fileName.lastIndexOf('.');
			if (i > 0 && i < fileName.length() - 1) {
				return fileName.substring(i + 1);
			}
		}
		return null;
	}

	/**
	 * 获取文件名称
	 * 
	 * @param file
	 *            文件File对象
	 * @return 文件名称，不包含扩展名
	 */
	public static String getFileWithoutExtension(File file) {
		if (file != null) {
			String fileName = file.getName();
			int i = fileName.lastIndexOf('.');
			if (i > 0 && i < fileName.length()) {
				return fileName.substring(0, i);
			}
		}
		return null;
	}

}
