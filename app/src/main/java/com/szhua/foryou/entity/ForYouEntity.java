package com.szhua.foryou.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Create by szhua ;
  */
public class ForYouEntity extends BmobObject implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;//名字
	private BmobFile file;//文件
	private String contentString; //内容 ；
    private String date ;
	private  long time ;
	public ForYouEntity(){
	}
	public ForYouEntity(String name, BmobFile file, String contentString,String date,long time){
		this.name =name;
		this.file = file;
		this.contentString =contentString ;
		this.date =date ;
		this.time =time ;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BmobFile getFile() {
		return file;
	}

	public void setFile(BmobFile file) {
		this.file = file;
	}

	public String getContentString() {
		return contentString;
	}

	public void setContentString(String contentString) {
		this.contentString = contentString;
	}

	@Override
	public String toString() {
		return "ForYou{" +
				"name='" + name + '\'' +
				", file=" + file +
				", contentString='" + contentString + '\'' +
				'}';
	}
}
