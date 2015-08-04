package com.eagle.easyshopping;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.BmobProFile;

public class AccountActivity extends Activity {

	String TAG = "AccountActivity";
	ListView mlvFood;
	FoodAdapter mAdapter;
	List<FoodBean> nmlvFoods;
	List<FoodBean> put;
	int[] resid;
	String[] names;
	String[] prices;
	HashMap<Integer, String> hashMap;
	TextView mtextView;
	TextView mtextViewb;
	Double count = 0.0;
	TextView actvshopname;
	String cshopname;
	String cusername;
	Double qsend;
	Double psend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bmob.initialize(this, "474311be9f312229e370c28ebcac1d75");

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_account);
		findViewById(R.id.bBack).setOnClickListener(new BackListener());
		findViewById(R.id.bImput).setOnClickListener(new UpListener());

		initData();
		initView();

		actvshopname = (TextView) findViewById(R.id.actvshopname);
		actvshopname.setText(cshopname);

	}

	private void initView() {
		mlvFood = (ListView) findViewById(R.id.lvCFoods);
		mAdapter = new FoodAdapter();
		mlvFood.setAdapter(mAdapter);

	}

	private void initData() {
		String[] names = getResources().getStringArray(R.array.foods);
		String[] singlep = getResources().getStringArray(R.array.singlep);
		int[] ppp = new int[singlep.length];
		for (int i = 0; i < singlep.length; i++) {
			ppp[i] = Integer.parseInt(singlep[i]);
		}

		Intent intent1 = getIntent();
		nmlvFoods = new LinkedList<FoodBean>();
		put = new LinkedList<FoodBean>();

		cshopname = intent1.getStringExtra("cshopname");
		cusername = intent1.getStringExtra("cusername");
		qsend = intent1.getDoubleExtra("qsend", 10.0);
		psend = intent1.getDoubleExtra("psend", 0.0);

		// mlvFoods = (List<FoodBean>)intent1.getSerializableExtra("mlvFoods");

		mtextView = (TextView) findViewById(R.id.tvAcount);
		mtextViewb = (TextView) findViewById(R.id.tvAcount2);

		String ssize = intent1.getStringExtra("size");
		int size = Integer.parseInt(ssize);
		Log.v(TAG, "size" + size);

		for (int i = 0; i < size; i++) {
			FoodBean bean = (FoodBean) intent1.getSerializableExtra("foodbean"
					+ i);
			int price = Integer.parseInt(bean.getPrice());
			count = count + price * bean.getNum();
			put.add(bean);
			if (bean.getNum() > 0) {
				nmlvFoods.add(bean);

			}
		}

		if (count < 0) {
			count = 0.0;
		}

		double lastCount = qsend - count;
		mtextView.setText("总计：" + count + "元");
		if (lastCount > 0) {
			mtextViewb.setText("还差" + lastCount + "元起送");
		} else {
			mtextViewb.setText("还差" + 0.0 + "元起送");
		}

	}

	class FoodAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return nmlvFoods.size();
		}

		@Override
		public FoodBean getItem(int position) {
			return nmlvFoods.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View layout = View.inflate(AccountActivity.this,
					R.layout.item_food2, null);

			ImageView ivThumb = (ImageView) layout.findViewById(R.id.ivThumb);
			TextView tvName = (TextView) layout.findViewById(R.id.tvName);
			TextView tvPrice = (TextView) layout.findViewById(R.id.tvPrice);
			// MyEditText mEditText = (MyEditText)
			// layout.findViewById(R.id.meter);
			TextView ct = (TextView) layout.findViewById(R.id.tvCount);

			FoodBean bean = nmlvFoods.get(position);

			// 加载图片
			loadShopPicImpl(ivThumb, bean.getPid());

			// ivThumb.setImageResource(bean.getResid());
			tvName.setText("名称：" + bean.getName());
			tvPrice.setText("价格：￥" + bean.getPrice() + ".0");
			ct.setText("个数：" + bean.getNum());
			// mEditText.setNum(bean.getNum());
			return layout;
		}

		// 加载店家图片
		private void loadShopPicImpl(ImageView shop_pic, String pic_name) {
			Log.d(TAG, "in function loadShopPicImpl:loading");
			final ImageView view = shop_pic;
			Bitmap shop_bg = null;
			File file = new File(pic_name);
			if (file.exists()) {
				Log.i(TAG, "loading pic local?");
				shop_bg = BitmapFactory.decodeFile(pic_name);
				shop_pic.setImageBitmap(shop_bg);
			} else {
				BmobProFile pro_file = BmobProFile
						.getInstance(AccountActivity.this);
				pro_file.download(pic_name,
						new com.bmob.btp.callback.DownloadListener() {
							@Override
							public void onSuccess(String s) {
								setImageBitmap(view, s);
							}

							@Override
							public void onProgress(String s, int i) {

							}

							@Override
							public void onError(int i, String s) {
								Log.e(TAG, "loading pic wrong:" + s);
							}
						});
			}
		}

		private void setImageBitmap(ImageView view, String file_path) {
			Log.i(TAG, "loading success: " + file_path);
			File file = new File(file_path);
			Bitmap bit = null;
			if (view.isShown()) {
				view.setImageResource(0);
				bit = BitmapFactory.decodeFile(file_path);
				if (file.exists())
					Log.e(TAG, "file exists!");
				if (bit == null)
					Log.e(TAG, "WTF NullPointer?");
				else
					Log.i(TAG, "" + bit.getWidth());
				view.setImageBitmap(bit);
				view.invalidate();
				Log.i(TAG, "set to imageView");
			}
			// file.delete();
		}

	}

	class BackListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.bBack) {
				/*
				 * Intent intent = new Intent(AccountActivity.this,
				 * MainActivity.class); startActivity(intent);
				 */
				AccountActivity.this.finish();

			}

		}

	}

	class UpListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (count < qsend) {

				Toast.makeText(AccountActivity.this, "下单失败，本店" + qsend + "元起送",
						Toast.LENGTH_SHORT).show();
			}
			if (v.getId() == R.id.bImput && count > qsend) {
				for (int i = 0; i < nmlvFoods.size(); i++) {
					CartG cartG = new CartG();
					cartG.setUsername(cusername);
					cartG.setShopname(cshopname);
					cartG.setGoodsname(nmlvFoods.get(i).getName());
					// Toast.makeText(AccountActivity.this,
					// nmlvFoods.get(i).getName(), Toast.LENGTH_SHORT).show();
					cartG.setNum(nmlvFoods.get(i).getNum());
					// Toast.makeText(AccountActivity.this,
					// nmlvFoods.get(i).getNum()+"", Toast.LENGTH_SHORT).show();
					cartG.save(AccountActivity.this, new SaveListener() {
						@Override
						public void onSuccess() {

							Toast.makeText(AccountActivity.this, "下单成功",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onFailure(int code, String msg) {

							Toast.makeText(AccountActivity.this,
									"下单失败，请重试" + msg, Toast.LENGTH_SHORT)
									.show();
						}
					});
				}

				Intent intent666 = new Intent(AccountActivity.this,
						OrderActivity.class);
				intent666.putExtra("cshopname", cshopname);
				intent666.putExtra("cusername", cusername);
				intent666.putExtra("psend", psend);
				intent666.putExtra("size", "" + put.size());
				intent666.putExtra("totalprice", count);
				for (int i = 0; i < put.size(); i++) {
					intent666.putExtra("foodbean" + i, put.get(i));
					// Toast.makeText(MainActivity.this,"被短按"+mlvFoods.get(i).getNum(),
					// Toast.LENGTH_SHORT).show();
				}
				startActivity(intent666);

			}

		}

	}

}
