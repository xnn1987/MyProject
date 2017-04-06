package com.cn.aixiyi.utils;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SDCardUtil {
   /**
    * 获得SDCard的根路径
    * */
	 public static String getSDCardPath(){
			String rootPath=Environment.getExternalStorageDirectory().getAbsolutePath();
			  if(rootPath!=null){
				  return rootPath;
			  }else{
			  return null;
			  } 
		  }
	  /**获取数据�?/
	 public static List<Map<String, Object>> getListData(String path) {
	 	List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	 	File file=new File(path);
	 	if(file.getParent()!=null&&!"/mnt".equals(file.getParent())){
	 		Map<String,Object> root=new HashMap<String, Object>();//map为listView的每�?��
	 		root.put("path", file.getParent());
	 		list.add(root);
	 	}
	 	File[] files=getFilesOrder(file);
	 	if(files!=null&&files.length>0){
	 		for(File f:files){
	 			Map<String,Object> item=new HashMap<String, Object>();//map是ListView的每�?��
	 			if(f.isDirectory()){
	 				item.put("path", f.getAbsolutePath());
	 				list.add(item);
	 			}else if(f.isFile()&&isPic(f.getPath())){
	 				item.put("path", f.getAbsolutePath());
	 				list.add(item);
	 				}
	 			}
	 		
	 	}
	 	return list;
	 }
	 /**获得某个路径下的�?��文件及文件夹，按�?��顺序返回*/
	 public static File[] getFilesOrder(File file) {
	 	File[] files=null;
	 	if(file.exists()){
	 		files=file.listFiles();
	 		File temp;
	 		if(files!=null&&files.length>0){
	 			for(int i=0;i<files.length;i++){
	 				for(int j=0;j<files.length-i-1;j++){
	 					if(files[j].getName().compareTo(files[j+1].getName())>0){
	 						temp=files[j];
	 						files[j]=files[j+1];
	 						files[j+1]=temp;
	 					}
	 				}
	 			}
	 		}
	 		
	 	}
	 	return files;
	 }
	public static boolean isPic(String path) {
		if(path!=null&&!"".equals(path)){
			String fileExt=path.substring(path.lastIndexOf(".")+1,path.length());
			if("jpg".equalsIgnoreCase(fileExt)
					||"png".equalsIgnoreCase(fileExt)
					||"gif".equalsIgnoreCase(fileExt)
					||"jpeg".equalsIgnoreCase(fileExt)
					){
				return true;
			}
		}
		return false;
	}


}




