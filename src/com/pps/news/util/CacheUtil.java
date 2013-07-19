package com.pps.news.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.os.Environment;

import com.pps.news.bean.Comment;
import com.pps.news.bean.Group;
import com.pps.news.bean.News;

/**
 * @file CacheUtil.java
 * @create 2013-6-7 上午10:54:34
 * @author lilong
 * @description TODO
 */
public class CacheUtil {
	
	private static final String CACHE_FILE_APPENDIX_NEWS_LIST = "_news_list.data";
	private static final String CACHE_FILE_APPENDIX_COMMENT = "_news_comment.data";
	/** 缓存新闻目录 */
	public static final String CACHE_DIRECTORY = "PPSNews/cache/data";
	
	
	public static void writeString(DataOutputStream out, String str)
			throws IOException {
		if (str != null) {
			out.writeInt(1);
			out.writeUTF(str);
		} else {
			out.writeInt(0);
		}
	}

	public static String readString(DataInputStream in) throws IOException {
		int flag = in.readInt();
		if (flag == 1) {
			return in.readUTF();
		} else {
			return null;
		}
	}

	public static void writeBoolean(DataOutputStream out, boolean value)
			throws IOException {
		out.writeBoolean(value);
	}

	public static boolean readBoolean(DataInputStream in) throws IOException {
		return in.readBoolean();
	}

	public static void writeInt(DataOutputStream out, int value)
			throws IOException {
		out.writeInt(value);
	}

	public static int readInt(DataInputStream in) throws IOException {
		return in.readInt();
	}

	public static void writeLong(DataOutputStream out, long value)
			throws IOException {
		out.writeLong(value);
	}

	public static long readLong(DataInputStream in) throws IOException {
		return in.readLong();
	}

	public static void writeFloat(DataOutputStream out, float value)
			throws IOException {
		out.writeFloat(value);
	}

	public static float readFloat(DataInputStream in) throws IOException {
		return in.readFloat();
	}

	public static void writeDouble(DataOutputStream out, double value)
			throws IOException {
		out.writeDouble(value);
	}

	public static Double readDouble(DataInputStream in) throws IOException {
		return in.readDouble();
	}
	
	public static void writeNews(DataOutputStream out, News news) throws IOException{
		news.writeToOutputStream(out);
	}
	
	public static News readNews(DataInputStream in) throws IOException {
		News news=new News();
		news.readFromInputStream(in);
		return news; 
	}
	
	public static void writeComment(DataOutputStream out, Comment comment) throws IOException {
		comment.writeToOutputStream(out);
	}
	
	public static Comment readComment(DataInputStream in) throws IOException {
		Comment comment = new Comment();
		comment.readToInputStream(in);
		return comment;
	}
	
	public static void saveNewsCache(List<News> data) {
		if (data == null) return;
		if (UIUtil.isSDCardAvailable()) {
			File baseDir = new File(Environment.getExternalStorageDirectory(),CACHE_DIRECTORY);
			if(!baseDir.exists()){
				baseDir.mkdirs();
			}
			File cacheFile = null;
			FileOutputStream fos=null;
			DataOutputStream out=null;
			try {
				cacheFile = new File(baseDir, CACHE_FILE_APPENDIX_NEWS_LIST);
				if(cacheFile.exists()){
					cacheFile.delete();
				}
				int size=data.size();
				fos=new FileOutputStream(cacheFile);
				out=new DataOutputStream(fos);
				out.writeInt(size);
				for(int i=0;i<size;i++){
					writeNews(out, data.get(i));
				}
				out.flush();
				fos.flush();
			} catch (Throwable e) {
				e.printStackTrace();
				if(cacheFile!=null){
					cacheFile.delete();
				}
			}finally{
				try {
					if(out!=null){
						out.close();
					}
					if(fos!=null){
						fos.close();
					}
				} catch (Exception e2) {
				}
			}
		}
	}

	public static List<News> getNewsCache(){
		List<News> news =new ArrayList<News>();
		if(UIUtil.isSDCardAvailable()){
			File baseDir = new File(Environment.getExternalStorageDirectory(),CACHE_DIRECTORY);
			File cacheFile=null;
			FileInputStream fis=null;
			DataInputStream in=null;
			try {
				cacheFile = new File(baseDir, CACHE_FILE_APPENDIX_NEWS_LIST);
				if(cacheFile.exists()){
					fis=new FileInputStream(cacheFile);
					in=new DataInputStream(fis);
					int size=readInt(in);
					for(int i=0;i<size;i++){
						news.add(readNews(in));
					}			
				}
			} catch (Throwable e) {
				e.printStackTrace();
				if(cacheFile!=null){
					cacheFile.delete();
				}
			}finally{
				try {
					if(in!=null){
						in.close();
					}
					if(fis!=null){
						fis.close();
					}
				} catch (Exception e2) {
				}
			}
		}
		return news;
	}
	
	public static void saveCommentCache(long newsId, Group<Comment> data) {
		if (data == null) return;
		if (UIUtil.isSDCardAvailable()) {
			File baseDir = new File(Environment.getExternalStorageDirectory(),CACHE_DIRECTORY);
			if(!baseDir.exists()){
				baseDir.mkdirs();
			}
			File cacheFile = null;
			FileOutputStream fos=null;
			DataOutputStream out=null;
			try {
				cacheFile = new File(baseDir, newsId + CACHE_FILE_APPENDIX_COMMENT);
				if(cacheFile.exists()){
					cacheFile.delete();
				}
				int size=data.size();
				fos=new FileOutputStream(cacheFile);
				out=new DataOutputStream(fos);
				out.writeInt(size); // 集合中对象数目
				out.writeInt(data.getTotal()); // 总数目
				out.writeInt(data.getTotal_page()); // 总页数
				for(int i=0;i<size;i++){
					writeComment(out, data.get(i));
				}
				out.flush();
				fos.flush();
			} catch (Throwable e) {
				e.printStackTrace();
				if(cacheFile!=null){
					cacheFile.delete();
				}
			}finally{
				try {
					if(out!=null){
						out.close();
					}
					if(fos!=null){
						fos.close();
					}
				} catch (Exception e2) {
				}
			}
		}
	}

	public static Group<Comment> getCommentCache(long newsId){
		Group<Comment> data =new Group<Comment>();
		if(UIUtil.isSDCardAvailable()){
			File baseDir = new File(Environment.getExternalStorageDirectory(),CACHE_DIRECTORY);
			File cacheFile=null;
			FileInputStream fis=null;
			DataInputStream in=null;
			try {
				cacheFile = new File(baseDir, newsId + CACHE_FILE_APPENDIX_COMMENT);
				if(cacheFile.exists()){
					fis=new FileInputStream(cacheFile);
					in=new DataInputStream(fis);
					int size=readInt(in); // 对象个数
					data.setTotal(in.readInt()); // 总数目
					data.setTotal_page(in.readInt()); // 总页数
					for(int i=0;i<size;i++){
						data.add(readComment(in));
					}			
				}
			} catch (Throwable e) {
				e.printStackTrace();
				if(cacheFile!=null){
					cacheFile.delete();
				}
			}finally{
				try {
					if(in!=null){
						in.close();
					}
					if(fis!=null){
						fis.close();
					}
				} catch (Exception e2) {
				}
			}
		}
		return data;
	}
}
