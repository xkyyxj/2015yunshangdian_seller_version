package com.eagle.easyshopping;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.DownloadListener;

/**
 * Showing information of an order's goods. Created by Albert on 2015/7/16.
 */
public class AddressAdapter extends BaseAdapter {

	private List<Add> list = null;
	private String backupname;
	private Context con;
	private LayoutInflater inflater = null;

	public AddressAdapter(Context _context, List<Add> fromBmob, String name) {
		con = _context;
		list = fromBmob;
		backupname = name;
		inflater = LayoutInflater.from(_context);
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.address_item, null);

			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			
			holder.telnum.setText(list.get(position).getUsertel());
			holder.add.setText(list.get(position).getUseradd());
			holder.receiver.setText(list.get(position).getReceiver());
			holder.delete.setTag(position);
			holder.delete.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					int p = (int) v.getTag();
					String id = list.get(p).getObjectId();
					delete(id);
					list.remove(p);
					refresh();
				}
			});
			holder.edit.setTag(position);
			holder.edit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					final int p = (int) v.getTag();
					final String id = list.get(p).getObjectId();

					View view = inflater
							.inflate(R.layout.addinput_layout, null);
					final PopupWindow popupAddWindow = new PopupWindow(view,
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT, true);

					final EditText rec = (EditText) view
							.findViewById(R.id.receiverInput);
					final EditText tel = (EditText) view
							.findViewById(R.id.telInput);
					final EditText add = (EditText) view
							.findViewById(R.id.addInput);
					ImageButton sure = (ImageButton) view
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
								Toast.makeText(con, "城市或详细地址不能为空",
										Toast.LENGTH_LONG).show();
							} else {

								editAdd(id, name, usertel, useradd);
								
								
								 Add nn=new Add(); nn.setReceiver(name);
								 nn.setUsertel(usertel);
								nn.setUseradd(useradd);
								 list.remove(p);
								list.add(nn);
								refresh();
								//这种方法 在编辑完本地list之后
								//该position位置的新Add对象的objectId为空 无法进行删除操作 要重新下载该表 
								BmobQuery<Add> query = new BmobQuery<Add>();
								// 返回50条数据，如果不加上这条语句，默认返回10条数据
								// query.setLimit(50);
								query.addWhereEqualTo("username", backupname);

								query.findObjects(con,
										new FindListener<Add>() {

											public void onError(int arg0,
													String arg1) {
												// TODO Auto-generated method
												// stub
												Log.e("rg", "4");
											}

											public void onSuccess(List<Add> arg0) {
												// TODO Auto-generated method
												list=arg0;
											}
										});
								

							}

						}
					});
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

					popupAddWindow.setBackgroundDrawable(con.getResources()
							.getDrawable(R.drawable.addressbg));

					popupAddWindow.update();

					popupAddWindow.showAsDropDown(v,20,15);;

				}

			});
		}
		return convertView;
	}

	public class ViewHolder {
		public final TextView receiver;
		public final TextView telnum;
		public final TextView add;
		public final ImageButton edit;
		public final ImageButton delete;
		public final View root;

		public ViewHolder(View root) {
			receiver = (TextView) root.findViewById(R.id.username);
			telnum = (TextView) root.findViewById(R.id.tel_num);
			add = (TextView) root.findViewById(R.id.add);
			edit = (ImageButton) root.findViewById(R.id.edit);
			delete = (ImageButton) root.findViewById(R.id.delete);
			this.root = root;
		}
	}

	private void loadShopPic(ImageView shop_pic, String pic_name,
			Context context) {

		final ImageView view = shop_pic;
		Bitmap shop_bg = null;
		File file = new File(pic_name);
		if (file.exists()) {
			shop_bg = BitmapFactory.decodeFile(pic_name);
			shop_pic.setImageBitmap(shop_bg);
		} else {
			BmobProFile pro_file = BmobProFile.getInstance(context);
			pro_file.download(pic_name, new DownloadListener() {
				@Override
				public void onSuccess(String s) {
					setImageBitmap(view, s);
				}

				@Override
				public void onProgress(String s, int i) {

				}

				@Override
				public void onError(int i, String s) {
				}
			});
		}
	}

	private void setImageBitmap(ImageView view, String file_path) {

		File file = new File(file_path);
		Bitmap bit = null;
		if (view.isShown()) {
			view.setImageResource(0);
			if (file.exists())
				bit = BitmapFactory.decodeFile(file_path);
			view.setImageBitmap(bit);
			view.invalidate();
		}
		// file.delete();
	}

	private void editAdd(String id, String receiver, String usertel,
			String useradd) {
		Add newAdd = new Add();
		newAdd.setReceiver(receiver);
		newAdd.setUsertel(usertel);
		newAdd.setUseradd(useradd);
		newAdd.update(con, id, new UpdateListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(con, "编辑成功", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(con, "编辑失败", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void delete(String id) {
		Add add = new Add();
		add.setObjectId(id);
		add.delete(con, new DeleteListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(con, "删除成功", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(con, "删除失败", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void refresh() {
		this.notifyDataSetChanged();
	}
}
