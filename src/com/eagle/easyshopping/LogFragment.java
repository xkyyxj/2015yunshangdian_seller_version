package com.eagle.easyshopping;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class LogFragment extends Fragment {
	TextView logText;

	public EditText username;
	public EditText psw;
	ImageButton lgbtn;
	Button register;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View newsLayout = inflater.inflate(R.layout.log_layout, container,
				false);
		return newsLayout;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		lgbtn = (ImageButton) getActivity().findViewById(R.id.logbtn);
		register = (Button) getActivity().findViewById(R.id.sign);
		logText = (TextView) getActivity().findViewById(R.id.usertext);
		username = (EditText) getActivity().findViewById(R.id.nameInput);
		psw = (EditText) getActivity().findViewById(R.id.psdInput);
		register.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(getActivity(), RegisterActivity.class);
				startActivity(intent);

			}
		});
		lgbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				String user_name = username.getText().toString();
				String user_password = psw.getText().toString();
				login(user_name, user_password);

			}
		});
	}

	private void login(String user_name, String user_password) {
		Log.e("lg", user_name);
		Log.e("lg", user_password);
		BmobQuery<User> query1 = new BmobQuery<User>();
		query1.addWhereEqualTo("username", user_name);
		BmobQuery<User> query2 = new BmobQuery<User>();
		query2.addWhereEqualTo("userpwd", user_password);
		List<BmobQuery<User>> andQuerys = new ArrayList<BmobQuery<User>>();
		andQuerys.add(query1);
		andQuerys.add(query2);
		BmobQuery<User> query = new BmobQuery<User>();
		query.and(andQuerys);
		query.findObjects(getActivity(), new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity().getApplicationContext(), "登录失败",
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(List<User> arg0) {
				// TODO Auto-generated method stub
				// 登陆成功
				if (arg0.size() == 0) {
					Toast.makeText(getActivity().getApplicationContext(),
							"未查询到相关用户", Toast.LENGTH_LONG).show();
					username.setText(null);
					psw.setText(null);
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"登陆成功 正在跳转", Toast.LENGTH_SHORT).show();
					((MainActivity) getActivity()).setUsername(arg0.get(0)
							.getUsername());
					Log.e("name", ((MainActivity) getActivity()).getUsername());
					username.setText(null);
					User user = arg0.get(0);
					((MyApplication)getActivity().getApplication()).setUser(user);
					psw.setText(null);
					// 获取父activity实例
					MainActivity main = (MainActivity) getActivity();
					// 调用父类方法进行fragment的跳转
					main.setTabSelection(3);
					
				}
			}

		});

	}
}