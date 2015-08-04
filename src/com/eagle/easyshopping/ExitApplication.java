package com.eagle.easyshopping;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
public class ExitApplication extends Application {

 private List<Activity> activityList=new LinkedList<Activity>();

private static ExitApplication instance;
private ExitApplication()
{
}
 //单例模式中获取唯�?��ExitApplication 实例
public static ExitApplication getInstance()
{
	if(null == instance)
 {
 instance = new ExitApplication();
 }
 return instance;

 }
 //添加Activity 到容器中
 public void addActivity(Activity activity)
 {
	 boolean flag=false;
	 for(Activity activity1:activityList)
	 {
		 if( activity1.getClass().equals(activity.getClass()))
				 {
			       flag=true;
			       activity1=activity;
				 }
	 }
	 if(!flag)
        activityList.add(activity);
 }
 //遍历�?��Activity 并finish
 public void exit()
 {
 for(Activity activity:activityList)
 {
 activity.finish();
 }
 System.exit(0);
 }
 }