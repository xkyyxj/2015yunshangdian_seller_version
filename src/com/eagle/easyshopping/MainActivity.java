package com.eagle.easyshopping;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;

import com.baidu.mapapi.SDKInitializer;

/**
 * 项目的主Activity，所有的Fragment都嵌入在这里。
 */
public class MainActivity extends Activity implements OnClickListener {

	/**
	 * 主页的Fragment
	 */
	private HomeFragment homeFragment;

	/**
	 * 搜索的Fragment
	 */
	private SearchFragment searchFragment;

	/**
	 * 个人中心（登陆）的Fragment
	 */
	private LogFragment logFragment;
	/**
	 * 个人中心（主界面）的Fragment
	 */
	private UserFragment userFragment;

	/**
	 * 主页界面布局
	 */
	private View homeLayout;

	/**
	 * 搜索界面布局
	 */
	private View searchLayout;

	/**
	 * 个人中心登陆界面布局
	 */
	private View logLayout;

	/**
	 * 在Tab布局上显示主页标题的控件
	 */
	private TextView homeText;

	/**
	 * 在Tab布局上显示搜索标题的控件
	 */
	private TextView searchText;

	private int currentTab;
	/**
	 * 在Tab布局上显示个人中心(登陆)标题的控件
	 */
	private TextView logText;

	/*
	 * private TextView homeimage;
	 */private long mExitTime;
	private String APP_ID = "474311be9f312229e370c28ebcac1d75";
	private ImageView home, search, user;
	private String username = "wison";
	private boolean isFirstStart = false;
	/**
	 * 用于对Fragment进行管理
	 */
	FragmentManager fragmentManager;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 初始化布局元素
		isFirstStart = true;
		SDKInitializer.initialize(getApplicationContext());
		Bmob.initialize(getApplicationContext(), APP_ID);
		// 第一次启动时选中第0个tab
		fragmentManager = getFragmentManager();
		initViews();
		setTabSelection(0);
	}

	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
	
	}

	/**
	 * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
	 */
	private void initViews() {
		homeLayout = findViewById(R.id.home_layout);
		searchLayout = findViewById(R.id.search_layout);
		logLayout = findViewById(R.id.log_layout);
		homeText = (TextView) findViewById(R.id.hometext);
		searchText = (TextView) findViewById(R.id.searchtext);
		logText = (TextView) findViewById(R.id.usertext);
		home = (ImageView) findViewById(R.id.homeimage);
		if (home == null) {
			Log.e("rg", "home==null");
		}
		Log.e("1", "11");
		home = (ImageView) findViewById(R.id.homeimage);
		Log.e("1", "22");
		search = (ImageView) findViewById(R.id.searchimage);
		Log.e("1", "33");
		user = (ImageView) findViewById(R.id.userimage);
		Log.e("1", "44");

		homeLayout.setOnClickListener(this);
		searchLayout.setOnClickListener(this);
		logLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_layout:
			// 当点击了消息tab时，选中第1个tab
			setTabSelection(0);
			break;
		case R.id.search_layout:
			// 当点击了联系人tab时，选中第2个tab
			setTabSelection(1);
			break;
		case R.id.log_layout:
			// 当点击了动态tab时，选中第3个tab
			setTabSelection(2);
			break;

		default:
			break;
		}
	}

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * @param index
	 *            每个tab页对应的下标。0表示主页，1表示搜索，2表示个人中心（登陆界面）。
	 */

	public void setTabSelection(int index) {
		// 每次选中之前先清楚掉上次的选中状态
		clearSelection();
		// 开启一个Fragment事务
		fragmentManager.popBackStack();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:
			currentTab = 0;
			// 当点击了hometab时，改变控件的图片和文字颜色
			home.setImageResource(R.drawable.homeselected);
			homeText.setTextColor(Color.parseColor("#4ec1e8"));
			if (homeFragment == null) {
				// 如果HomeFragment为空，则创建一个并添加到界面上
				homeFragment = new HomeFragment();
				transaction.replace(R.id.content, homeFragment);
			} else {
				// 如果HomeFragment不为空，则直接将它显示出来
				transaction.show(homeFragment);
			}
			break;

		case 1:
			currentTab = 1;
			// 当点击了searchtab时，改变控件的图片和文字颜色
			search.setImageResource(R.drawable.searchselected);
			searchText.setTextColor(Color.parseColor("#4ec1e8"));
			if (searchFragment == null) {
				// 如果SearchFragment为空，则创建一个并添加到界面上
				searchFragment = new SearchFragment();
				transaction.add(R.id.content, searchFragment);
			} else {
				// 如果SearchFragment不为空，则直接将它显示出来
				transaction.show(searchFragment);
			}
			break;
		case 2:
			currentTab = 2;
			// 当点击了logtab时，改变控件的图片和文字颜色
			user.setImageResource(R.drawable.userselected);
			logText.setTextColor(Color.parseColor("#4ec1e8"));
			if (userFragment == null) {
				if (logFragment == null) {
					// 如果LogFragment为空，则创建一个并添加到界面上
					logFragment = new LogFragment();
					transaction.add(R.id.content, logFragment);
				} else {
					// 如果NewsFragment不为空，则直接将它显示出来
					transaction.show(logFragment);
				}
			} else {
				transaction.show(userFragment);
			}
			break;
		case 3:
			currentTab = 3;
			// 当跳转usertab时，改变控件的图片和文字颜色
			logText.setTextColor(Color.parseColor("#4ec1e8"));
			user.setImageResource(R.drawable.userselected);
			if (userFragment == null) {
			userFragment = new UserFragment();
			transaction.add(R.id.content, userFragment);

			}else{
				transaction.show(userFragment);
			}

			
			break;
		case 4:
			currentTab = 2;
			// 当点击了动态tab时，改变控件的图片和文字颜色
			user.setImageResource(R.drawable.userselected);
			logText.setTextColor(Color.parseColor("#4ec1e8"));
			if (logFragment == null) {

				// 如果LogFragment为空，则创建一个并添加到界面上
				logFragment = new LogFragment();

				transaction.add(R.id.content, logFragment);
			} else {
				// 如果NewsFragment不为空，则直接将它显示出来
				transaction.show(logFragment);
			}
			break;

		}
		transaction.commit();
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {
		homeText.setTextColor(Color.parseColor("#7a7a7a"));

		search.setImageResource(R.drawable.searchunselected);

		searchText.setTextColor(Color.parseColor("#7a7a7a"));

		home.setImageResource(R.drawable.homeunselected);

		logText.setTextColor(Color.parseColor("#7a7a7a"));

		user.setImageResource(R.drawable.userunselected);

	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (homeFragment != null) {
			transaction.hide(homeFragment);
		}
		if (searchFragment != null) {
			transaction.hide(searchFragment);
		}
		if (logFragment != null) {
			transaction.hide(logFragment);
		}
		if (userFragment != null) {
			transaction.hide(userFragment);
		}

	}

	@Override
	protected void onResume() { // TODO 自动生成的方法存根 super.onResume();
		super.onResume();
		fragmentManager.popBackStack();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		hideFragments(transaction);
			switch (currentTab) {
			case 0:
				home.setImageResource(R.drawable.homeselected);
				homeText.setTextColor(Color.parseColor("#4ec1e8"));
				transaction.show(homeFragment);
				break;
			case 1:
				search.setImageResource(R.drawable.searchselected);
				searchText.setTextColor(Color.parseColor("#4ec1e8"));
				transaction.show(searchFragment);
				break;
			case 3:
				user.setImageResource(R.drawable.userselected);
				logText.setTextColor(Color.parseColor("#4ec1e8"));
				transaction.show(userFragment);
			
				break;
			}
			transaction.commit();
	
	

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {

				Toast.makeText(this, "再按一次退出程序~", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				ExitApplication.getInstance().exit();
				// Intent i = new Intent(Intent.ACTION_MAIN);
				//
				// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//
				// i.addCategory(Intent.CATEGORY_HOME);
				//
				// startActivity(i);
				// ExitApplication.getInstance().exit();
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
