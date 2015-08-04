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
 * ��Ŀ����Activity�����е�Fragment��Ƕ�������
 */
public class MainActivity extends Activity implements OnClickListener {

	/**
	 * ��ҳ��Fragment
	 */
	private HomeFragment homeFragment;

	/**
	 * ������Fragment
	 */
	private SearchFragment searchFragment;

	/**
	 * �������ģ���½����Fragment
	 */
	private LogFragment logFragment;
	/**
	 * �������ģ������棩��Fragment
	 */
	private UserFragment userFragment;

	/**
	 * ��ҳ���沼��
	 */
	private View homeLayout;

	/**
	 * �������沼��
	 */
	private View searchLayout;

	/**
	 * �������ĵ�½���沼��
	 */
	private View logLayout;

	/**
	 * ��Tab��������ʾ��ҳ����Ŀؼ�
	 */
	private TextView homeText;

	/**
	 * ��Tab��������ʾ��������Ŀؼ�
	 */
	private TextView searchText;

	private int currentTab;
	/**
	 * ��Tab��������ʾ��������(��½)����Ŀؼ�
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
	 * ���ڶ�Fragment���й���
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
		// ��ʼ������Ԫ��
		isFirstStart = true;
		SDKInitializer.initialize(getApplicationContext());
		Bmob.initialize(getApplicationContext(), APP_ID);
		// ��һ������ʱѡ�е�0��tab
		fragmentManager = getFragmentManager();
		initViews();
		setTabSelection(0);
	}

	@Override
	protected void onStart() {
		// TODO �Զ����ɵķ������
		super.onStart();
	
	}

	/**
	 * �������ȡ��ÿ����Ҫ�õ��Ŀؼ���ʵ���������������úñ�Ҫ�ĵ���¼���
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
			// ���������Ϣtabʱ��ѡ�е�1��tab
			setTabSelection(0);
			break;
		case R.id.search_layout:
			// ���������ϵ��tabʱ��ѡ�е�2��tab
			setTabSelection(1);
			break;
		case R.id.log_layout:
			// ������˶�̬tabʱ��ѡ�е�3��tab
			setTabSelection(2);
			break;

		default:
			break;
		}
	}

	/**
	 * ���ݴ����index����������ѡ�е�tabҳ��
	 * 
	 * @param index
	 *            ÿ��tabҳ��Ӧ���±ꡣ0��ʾ��ҳ��1��ʾ������2��ʾ�������ģ���½���棩��
	 */

	public void setTabSelection(int index) {
		// ÿ��ѡ��֮ǰ��������ϴε�ѡ��״̬
		clearSelection();
		// ����һ��Fragment����
		fragmentManager.popBackStack();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		// �����ص����е�Fragment���Է�ֹ�ж��Fragment��ʾ�ڽ����ϵ����
		hideFragments(transaction);
		switch (index) {
		case 0:
			currentTab = 0;
			// �������hometabʱ���ı�ؼ���ͼƬ��������ɫ
			home.setImageResource(R.drawable.homeselected);
			homeText.setTextColor(Color.parseColor("#4ec1e8"));
			if (homeFragment == null) {
				// ���HomeFragmentΪ�գ��򴴽�һ�������ӵ�������
				homeFragment = new HomeFragment();
				transaction.replace(R.id.content, homeFragment);
			} else {
				// ���HomeFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(homeFragment);
			}
			break;

		case 1:
			currentTab = 1;
			// �������searchtabʱ���ı�ؼ���ͼƬ��������ɫ
			search.setImageResource(R.drawable.searchselected);
			searchText.setTextColor(Color.parseColor("#4ec1e8"));
			if (searchFragment == null) {
				// ���SearchFragmentΪ�գ��򴴽�һ�������ӵ�������
				searchFragment = new SearchFragment();
				transaction.add(R.id.content, searchFragment);
			} else {
				// ���SearchFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(searchFragment);
			}
			break;
		case 2:
			currentTab = 2;
			// �������logtabʱ���ı�ؼ���ͼƬ��������ɫ
			user.setImageResource(R.drawable.userselected);
			logText.setTextColor(Color.parseColor("#4ec1e8"));
			if (userFragment == null) {
				if (logFragment == null) {
					// ���LogFragmentΪ�գ��򴴽�һ�������ӵ�������
					logFragment = new LogFragment();
					transaction.add(R.id.content, logFragment);
				} else {
					// ���NewsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
					transaction.show(logFragment);
				}
			} else {
				transaction.show(userFragment);
			}
			break;
		case 3:
			currentTab = 3;
			// ����תusertabʱ���ı�ؼ���ͼƬ��������ɫ
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
			// ������˶�̬tabʱ���ı�ؼ���ͼƬ��������ɫ
			user.setImageResource(R.drawable.userselected);
			logText.setTextColor(Color.parseColor("#4ec1e8"));
			if (logFragment == null) {

				// ���LogFragmentΪ�գ��򴴽�һ�������ӵ�������
				logFragment = new LogFragment();

				transaction.add(R.id.content, logFragment);
			} else {
				// ���NewsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(logFragment);
			}
			break;

		}
		transaction.commit();
	}

	/**
	 * ��������е�ѡ��״̬��
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
	 * �����е�Fragment����Ϊ����״̬��
	 * 
	 * @param transaction
	 *            ���ڶ�Fragmentִ�в���������
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
	protected void onResume() { // TODO �Զ����ɵķ������ super.onResume();
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

				Toast.makeText(this, "�ٰ�һ���˳�����~", Toast.LENGTH_SHORT).show();
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