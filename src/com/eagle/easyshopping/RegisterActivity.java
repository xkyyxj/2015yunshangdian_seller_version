package com.eagle.easyshopping;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends Activity {
	private String APP_ID = "474311be9f312229e370c28ebcac1d75";
	private EditText user_name, password, password_confirm;
	private ImageButton register_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		Bmob.initialize(getApplicationContext(), APP_ID);
		Log.e("rg", "1");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.toregist);
		Log.e("rg", "2");
		user_name = (EditText) findViewById(R.id.namesign);
		password = (EditText) findViewById(R.id.psdsign);
		password_confirm = (EditText) findViewById(R.id.reinput);
		register_button = (ImageButton) findViewById(R.id.signbtn);
		register_button.setOnClickListener(new ButtonListener());
		ExitApplication.getInstance().addActivity(this);

	}

	class ButtonListener implements OnClickListener {
		String name = null;
		String user_password = null;
		String user_password_confirm = null;

		public void onClick(View v) {
			// TODO Auto-generated method stub
			name = user_name.getText().toString();
			user_password = password.getText().toString();
			user_password_confirm = password_confirm.getText().toString();
			if (user_password == null || user_password.length() <= 0)
				Toast.makeText(RegisterActivity.this, "���벻��Ϊ�գ�",
						Toast.LENGTH_LONG).show();
			else if (user_password.equals(user_password_confirm)) {
				registe(name, user_password);

			} else {
				Toast.makeText(RegisterActivity.this, "���벻һ�£�����������",
						Toast.LENGTH_LONG).show();
				password.setText(null);
				password_confirm.setText(null);
			}

		}

	}

	private void registe(String user_name, String user_password) {
		final User user = new User();
		final Context context = this;
		user.setUsername(user_name);
		user.setUserpwd(user_password);
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("username", user_name);
		query.findObjects(context, new FindListener<User>() {
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(RegisterActivity.this, "Find_Error",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(List<User> arg0) {
				// TODO Auto-generated method stub
				// ע��ʧ�ܣ��û�����ռ��
			
				if (arg0.size() != 0) {
					Toast.makeText(RegisterActivity.this, "�û����Ѵ���",
							Toast.LENGTH_SHORT).show();
				} else {

					Toast.makeText(RegisterActivity.this, "δ��ѯ���������",
							Toast.LENGTH_SHORT).show();
					user.save(context, new SaveListener() {

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							// ע��ʧ��
							Toast.makeText(RegisterActivity.this, "ע��ʧ��",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							// ע��ɹ�
							Toast.makeText(RegisterActivity.this, "ע��ɹ�",
									Toast.LENGTH_SHORT).show();
							finish();
						}

					});
					

				}
			}
		});
	}
}