package com.eagle.easyshopping;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class AddressActivity extends Activity {
	ListView listView;
	String username;
	TextView log;
	ImageButton addad;
	 PopupWindow popupAddWindow;
	 View contView;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.address_layout);
		LayoutInflater lf = LayoutInflater.from(this);
		 contView = lf.inflate(R.layout.addnewad_layout, null);

		 popupAddWindow = new PopupWindow(contView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		log = (TextView) this.findViewById(R.id.name);
		addad = (ImageButton) this.findViewById(R.id.addbtn);
		addad.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showAddPopupWindow(v);
			}
		});
			
		
		listView = (ListView) this.findViewById(R.id.address_list);

		Intent getNameIntent = getIntent();
		username = getNameIntent.getStringExtra("username");
		log.setText(username);
		BmobQuery<Add> query = new BmobQuery<Add>();
		// 返回50条数据，如果不加上这条语句，默认返回10条数据 query.setLimit(50);
		query.addWhereEqualTo("username", username);

		query.findObjects(this, new FindListener<Add>() {

			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.e("rg", "4");
			}

			public void onSuccess(List<Add> arg0) {
				// TODO Auto-generated method stub

				if (arg0.size() != 0) {

					Log.e("rg", "" + arg0.size());

					if (listView != null) {

						AddressAdapter all = new AddressAdapter(
								AddressActivity.this, arg0, username);

						listView.setAdapter(all);
						all.notifyDataSetChanged();

					}
				} else {
					Log.e("rg", "6");
					Toast.makeText(AddressActivity.this, "list 未查询到",
							Toast.LENGTH_SHORT).show();

				}
			}
		});

	}
	
	public void showAddPopupWindow(View view) {
		final EditText rec = (EditText) contView
				.findViewById(R.id.receiverInput);
		final EditText tel = (EditText) contView
				.findViewById(R.id.telInput);
		final EditText add = (EditText) contView
				.findViewById(R.id.addInput);
		ImageButton sure = (ImageButton) contView
				.findViewById(R.id.sureBtn);
		sure.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				popupAddWindow.dismiss();
				String name;
				String usertel;
				String useradd;
				name = rec.getText().toString();
				usertel = tel.getText().toString();
				useradd = add.getText().toString();
				if (name == null | usertel == null
						| useradd == null | name.equals("")
						| usertel.equals("") | useradd.equals("")) {
					Toast.makeText(AddressActivity.this, "城市或详细地址不能为空",
							Toast.LENGTH_LONG).show();
				} else {

					 Add nn=new Add(); 
					 nn.setUsername(username);
					 nn.setReceiver(name);
					  nn.setUsertel(usertel);
					 nn.setUseradd(useradd);
					 nn.save(AddressActivity.this, new SaveListener() {

					    @Override
					    public void onSuccess() {
					        // TODO Auto-generated method stub
					    	BmobQuery<Add> query = new BmobQuery<Add>();
							// 返回50条数据，如果不加上这条语句，默认返回10条数据 query.setLimit(50);
							query.addWhereEqualTo("username", username);

							query.findObjects(AddressActivity.this, new FindListener<Add>() {

								public void onError(int arg0, String arg1) {
									// TODO Auto-generated method stub
									Log.e("rg", "4");
								}

								public void onSuccess(List<Add> arg0) {
									// TODO Auto-generated method stub

									if (arg0.size() != 0) {

										Log.e("rg", "" + arg0.size());

										if (listView != null) {

											AddressAdapter all = new AddressAdapter(
													AddressActivity.this, arg0, username);
											

											listView.setAdapter(all);
											all.notifyDataSetChanged();

										}
									} else {
										Log.e("rg", "6");
										Toast.makeText(AddressActivity.this, "list 未查询到",
												Toast.LENGTH_SHORT).show();

									}
								}
							});
					    }

					    @Override
					    public void onFailure(int code, String arg0) {
					        // TODO Auto-generated method stub
					        // 添加失败
					    }
					});
					
					

			
		}}});
		popupAddWindow.setTouchable(true);

		// 使其聚集
		popupAddWindow.setFocusable(true);

		/*
		 * // 设置允许在外点击消失 popupWindow.setOutsideTouchable(false);
		 */
		popupAddWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("mengdd", "onTouch : ");

				return false;

			}
		});

		popupAddWindow.setBackgroundDrawable(AddressActivity.this.getResources()
				.getDrawable(R.drawable.addressbg));

		popupAddWindow.update();

		popupAddWindow.showAsDropDown(view);
	}
}