package com.eagle.easyshopping;

import java.io.File;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;

import com.bmob.BmobProFile;

public class SearchFragment extends Fragment {
	String TAG = "SearchActivity";
	private LinearLayout l8;
	private LinearLayout l9;
	List<SearchBean> mlvSearchs;
	List<SearchBean> mlvSearchShops;
	SearchShopAdapter mssAdapter;
	SearchAdapter msAdapter;
	ListView mlvSearch;
	ListView mlvSearchShop;
	EditText etSearsh;
	List<ShopG> object1;
	List<Shop> object2;
	List<Shop> object3;
	List<User> object5;
String  username;
	BmobGeoPoint lonla;

	TabHost tabHost;
	private View rootview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootview = inflater.inflate(R.layout.activity_search, container, false);

		return rootview;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		MainActivity main=(MainActivity)getActivity();
		username=main.getUsername();
		tabHost = (TabHost) this.getActivity().findViewById(
				android.R.id.tabhost);

		tabHost.setup();
		View goodsearch = getActivity().getLayoutInflater().inflate(
				R.layout.goodsearchtab, null);

		View shopsearch = getActivity().getLayoutInflater().inflate(
				R.layout.shopsearchtab, null);

		TabHost.TabSpec goodspec = tabHost.newTabSpec("good")
				.setIndicator(goodsearch).setContent(R.id.goodsearch);

		TabHost.TabSpec shopspec = tabHost.newTabSpec("shop")
				.setIndicator(shopsearch).setContent(R.id.shopsearch);

		tabHost.addTab(goodspec);
		tabHost.addTab(shopspec);

		getActivity().findViewById(R.id.ibSearch).setOnClickListener(
				new SearchClickListener());
		getActivity().findViewById(R.id.bt1).setOnClickListener(
				new ButtonClickListener());
		getActivity().findViewById(R.id.bt2).setOnClickListener(
				new ButtonClickListener());
		getActivity().findViewById(R.id.bt3).setOnClickListener(
				new ButtonClickListener());
		getActivity().findViewById(R.id.bt4).setOnClickListener(
				new ButtonClickListener());

		l8 = (LinearLayout) getActivity().findViewById(R.id.lll8);
		l9 = (LinearLayout) getActivity().findViewById(R.id.lll9);

		etSearsh = (EditText) getActivity().findViewById(R.id.etSearch);

		initView();
		initData();
		// initView();

		setListener();

	}

	private void setListener() {
		setOnItemClickListener();

	}

	public void setOnItemClickListener() {
		mlvSearch.setFocusable(true);
		mlvSearch.setFocusableInTouchMode(true);

		mlvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(getActivity(),
						ShopActivity.class);
				intent.putExtra("username",username);
				intent.putExtra("shopname", mlvSearchs.get(position)
						.getShopName());
				startActivity(intent);
			}
		});

		mlvSearchShop.setFocusable(true);
		mlvSearchShop.setFocusableInTouchMode(true);

		mlvSearchShop
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						Intent intent = new Intent(getActivity(),
								ShopActivity.class);
						intent.putExtra("username", username);
						intent.putExtra("shopname", mlvSearchShops
								.get(position).getShopName());
						getActivity().startActivity(intent);
					}
				});

	}

	private void initView() {
		mlvSearch = (ListView) getActivity().findViewById(R.id.lvSearch);
		mlvSearchShop = (ListView) getActivity()
				.findViewById(R.id.lvSearchShop);
		/*
		 * msAdapter = new SearchAdapter(); mlvSearch.setAdapter(msAdapter);
		 */

	}

	private void initData() {
		object1 = new LinkedList<ShopG>();
		object2 = new LinkedList<Shop>();
		mlvSearchs = new LinkedList<SearchBean>();
		mlvSearchShops = new LinkedList<SearchBean>();

		BmobQuery<User> query = new BmobQuery<User>();
		// 查询playerName叫“比目”的数据
		query.addWhereContains("username", username);
		// 返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(50);
		// 执行查询方法
		query.findObjects(getActivity(), new FindListener<User>() {
			@Override
			public void onSuccess(List<User> object) {

				object5 = object;
				// TODO Auto-generated method stub
				// toast("查询成功：共"+object.size()+"条数据。");
				// Toast.makeText(getActivity(),
				// "查询成功：共"+object5.size()+"条数据。", Toast.LENGTH_SHORT).show();
				for (User gameScore : object) {
					// 获得playerName的信息
					// gameScore.getShopname();
					// 获得数据的objectId信息
					gameScore.getObjectId();
					// 获得createdAt数据创建时间（注意是：createdAt，不是createAt）
					gameScore.getCreatedAt();
				}

				for (int i = 0; i < object5.size(); i++) {
					if (object5.get(i).getLonla() != null) {
						lonla = object5.get(i).getLonla();
					} else {
						Log.e("rg", "lona==null");
					}
				}

			}

			@Override
			public void onError(int code, String msg) {
				Toast.makeText(getActivity(), "查询失败" + msg, Toast.LENGTH_SHORT)
						.show();
			}
		});

	}

	private void putData() {
		for (int i = 0; i < object1.size(); i++) {
			SearchBean bean = new SearchBean(object1.get(i).getGoodsname(),
					object1.get(i).getShopname());
			// Toast.makeText(getActivity(), object1.get(i).getShopname(),
			// Toast.LENGTH_SHORT).show();
			mlvSearchs.add(bean);
		}

		for (int i = 0; i < object2.size(); i++) {
			SearchBean bean = new SearchBean(object2.get(i).getShopname());
			// Toast.makeText(getActivity(), object1.get(i).getShopname(),
			// Toast.LENGTH_SHORT).show();
			mlvSearchShops.add(bean);
		}

	}

	class SearchClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			if (v.getId() == R.id.ibSearch
					&& !etSearsh.getText().toString().equals("")) {
				l8.setVisibility(View.INVISIBLE);
				l9.setVisibility(View.VISIBLE);
				String shopname = etSearsh.getText().toString();
				String goodname = etSearsh.getText().toString();

				BmobQuery<Shop> query = new BmobQuery<Shop>();
				// 查询playerName叫“比目”的数据
				query.addWhereContains("shopname", shopname);
				// 返回50条数据，如果不加上这条语句，默认返回10条数据
				query.setLimit(50);
				// 执行查询方法
				query.findObjects(getActivity(), new FindListener<Shop>() {
					@Override
					public void onSuccess(List<Shop> object) {

						object2 = object;
						// TODO Auto-generated method stub
						// toast("查询成功：共"+object.size()+"条数据。");
						// Toast.makeText(getActivity(),
						// "查询成功：共"+object2.size()+"条数据。",
						// Toast.LENGTH_SHORT).show();
						for (Shop gameScore : object) {
							// 获得playerName的信息
							gameScore.getShopname();
							// 获得数据的objectId信息
							gameScore.getObjectId();
							// 获得createdAt数据创建时间（注意是：createdAt，不是createAt）
							gameScore.getCreatedAt();
						}

						mlvSearchShops.clear();
						for (int i = 0; i < object2.size(); i++) {
							DecimalFormat format = new DecimalFormat("####.#");
							String temp = format.format(object2.get(i)
									.getSlonla().distanceInKilometersTo(lonla));
							SearchBean bean = new SearchBean(object2.get(i)
									.getShopname(), object2.get(i)
									.getTotalgood(), object2.get(i)
									.getSendprice(), object2.get(i)
									.getSpicture(), object2.get(i)
									.getOperatesta(), "" + temp);
							// Toast.makeText(getActivity(),
							// object1.get(i).getShopname(),
							// Toast.LENGTH_SHORT).show();
							// Toast.makeText(getActivity(),
							// "距离："+object2.get(i).getSlonla().distanceInKilometersTo(lonla),
							// Toast.LENGTH_SHORT).show();
							mlvSearchShops.add(bean);
						}

						mssAdapter = new SearchShopAdapter();
						mssAdapter.notifyDataSetChanged();
						mlvSearchShop.setAdapter(mssAdapter);

					}

					@Override
					public void onError(int code, String msg) {
						Toast.makeText(getActivity(), "查询失败" + msg,
								Toast.LENGTH_SHORT).show();
					}
				});

				BmobQuery<ShopG> query1 = new BmobQuery<ShopG>();
				// 查询playerName叫“比目”的数据
				query1.addWhereContains("goodsname", goodname);
				// 返回50条数据，如果不加上这条语句，默认返回10条数据
				query1.setLimit(50);
				// 执行查询方法
				query1.findObjects(getActivity(), new FindListener<ShopG>() {
					@Override
					public void onSuccess(List<ShopG> object) {

						object1 = object;
						// TODO Auto-generated method stub
						// toast("查询成功：共"+object.size()+"条数据。");
						// Toast.makeText(getActivity(),
						// "查询成功：共"+object1.size()+"条数据。",
						// Toast.LENGTH_SHORT).show();
						for (ShopG gameScore : object) {
							// 获得playerName的信息
							gameScore.getShopname();
							// 获得数据的objectId信息
							gameScore.getObjectId();
							// 获得createdAt数据创建时间（注意是：createdAt，不是createAt）
							gameScore.getCreatedAt();
						}

						/*
						 * mlvSearchs.clear(); for(int
						 * i=0;i<object1.size();i++){ SearchBean bean = new
						 * SearchBean(object1.get(i).getGoodsname(),object1
						 * .get(i).getShopname());
						 * //Toast.makeText(getActivity(),
						 * object1.get(i).getShopname(),
						 * Toast.LENGTH_SHORT).show(); mlvSearchs.add(bean); }
						 * 
						 * msAdapter = new SearchAdapter();
						 * msAdapter.notifyDataSetChanged();
						 * mlvSearch.setAdapter(msAdapter);
						 */

						mlvSearchs.clear();
						for (int i = 0; i < object1.size(); i++) {
							BmobQuery<Shop> query = new BmobQuery<Shop>();
							// 查询playerName叫“比目”的数据
							query.addWhereContains("shopname", object1.get(i)
									.getShopname());
							// 返回50条数据，如果不加上这条语句，默认返回10条数据
							query.setLimit(50);
							// 执行查询方法
							query.findObjects(getActivity(),
									new FindListener<Shop>() {
										@Override
										public void onSuccess(List<Shop> object) {

											object3 = object;
											// TODO Auto-generated
											// method stub
											// toast("查询成功：共"+object.size()+"条数据。");
											// Toast.makeText(getActivity(),
											// "查询成功：共"+object3.size()+"条数据。",
											// Toast.LENGTH_SHORT).show();
											for (Shop gameScore : object) {
												// 获得playerName的信息
												gameScore.getShopname();
												// 获得数据的objectId信息
												gameScore.getObjectId();
												// 获得createdAt数据创建时间（注意是：createdAt，不是createAt）
												gameScore.getCreatedAt();
											}

											// mlvSearchs.clear();
											for (int i = 0; i < object3.size(); i++) {
												DecimalFormat format = new DecimalFormat(
														"####.#");
												String temp = format
														.format(object3
																.get(i)
																.getSlonla()
																.distanceInKilometersTo(
																		lonla));
												SearchBean bean = new SearchBean(
														object1.get(i)
																.getGoodsname(),
														object1.get(i)
																.getShopname(),
														object3.get(i)
																.getTotalgood(),
														object3.get(i)
																.getSendprice(),
														object3.get(i)
																.getSpicture(),
														object3.get(i)
																.getOperatesta(),
														"" + temp);
												// Toast.makeText(getActivity(),
												// object1.get(i).getShopname(),
												// Toast.LENGTH_SHORT).show();
												mlvSearchs.add(bean);
											}

											msAdapter = new SearchAdapter();
											msAdapter.notifyDataSetChanged();
											mlvSearch.setAdapter(msAdapter);

										}

										@Override
										public void onError(int code, String msg) {
											Toast.makeText(getActivity(),
													"查询失败" + msg,
													Toast.LENGTH_SHORT).show();
										}
									});
						}

					}

					@Override
					public void onError(int code, String msg) {
						Toast.makeText(getActivity(), "查询失败" + msg,
								Toast.LENGTH_SHORT).show();
					}
				});

			}

		}

	}

	class ButtonClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			// if (v.getId() == R.id.bt1) {
			l8.setVisibility(View.INVISIBLE);
			l9.setVisibility(View.VISIBLE);
			String goodname = "";
			switch (v.getId()) {
			case R.id.bt1:
				goodname = "农夫山泉矿泉水";
				break;
			case R.id.bt2:
				goodname = "康师傅方便面";
				break;
			case R.id.bt3:
				goodname = "报纸";
				break;
			case R.id.bt4:
				goodname = "心相印纸巾";
				break;
			}

			BmobQuery<ShopG> query1 = new BmobQuery<ShopG>();
			// 查询playerName叫“比目”的数据
			query1.addWhereContains("goodsname", goodname);
			// 返回50条数据，如果不加上这条语句，默认返回10条数据
			query1.setLimit(50);
			// 执行查询方法
			query1.findObjects(getActivity(), new FindListener<ShopG>() {
				@Override
				public void onSuccess(List<ShopG> object) {

					object1 = object;
					// TODO Auto-generated method stub
					// toast("查询成功：共"+object.size()+"条数据。");
					// Toast.makeText(getActivity(),
					// "查询成功：共"+object1.size()+"条数据。",
					// Toast.LENGTH_SHORT).show();
					for (ShopG gameScore : object) {
						// 获得playerName的信息
						gameScore.getShopname();
						// 获得数据的objectId信息
						gameScore.getObjectId();
						// 获得createdAt数据创建时间（注意是：createdAt，不是createAt）
						gameScore.getCreatedAt();
					}

					mlvSearchs.clear();
					for (int i = 0; i < object1.size(); i++) {
						SearchBean bean = new SearchBean(object1.get(i)
								.getGoodsname(), object1.get(i).getShopname());
						// Toast.makeText(getActivity(),
						// object1.get(i).getShopname(),
						// Toast.LENGTH_SHORT).show();
						mlvSearchs.add(bean);
					}

					msAdapter = new SearchAdapter();
					msAdapter.notifyDataSetChanged();
					mlvSearch.setAdapter(msAdapter);

				}

				@Override
				public void onError(int code, String msg) {
					Toast.makeText(getActivity(), "查询失败" + msg,
							Toast.LENGTH_SHORT).show();
				}
			});

			/*
			 * mlvSearchs.clear(); //mlvSearchShops.clear(); putData();
			 * 
			 * msAdapter = new SearchAdapter(); mlvSearch.setAdapter(msAdapter);
			 */
			// }

		}

	}

	class SearchShopAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mlvSearchShops.size();
		}

		@Override
		public SearchBean getItem(int position) {
			return mlvSearchShops.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View layout = View.inflate(getActivity(), R.layout.item_searchshop,
					null);

			ImageView ivThumb = (ImageView) layout.findViewById(R.id.ivThumb);
			TextView tvGoodName = (TextView) layout
					.findViewById(R.id.tvGoodName);
			TextView stvgood = (TextView) layout.findViewById(R.id.stvgood);
			// TextView stvnum = (TextView)
			// layout.getActivity().findViewById(R.id.stvnum);
			TextView stvdistance = (TextView) layout
					.findViewById(R.id.stvdistance);
			TextView stvsendprice = (TextView) layout
					.findViewById(R.id.stvsendprice);
			TextView tvState = (TextView) layout.findViewById(R.id.tvState);

			SearchBean bean = mlvSearchShops.get(position);

			// Toast.makeText(getActivity(), bean.getShopName(),
			// Toast.LENGTH_SHORT).show();

			tvGoodName.setText(bean.getShopName());
			stvgood.setText("" + bean.getTotalGood());
			stvsendprice.setText("起送价：￥" + bean.getSendPrice());
			stvdistance.setText("距离：" + bean.getDistance() + "km");
			// Toast.makeText(getActivity(),
			// "距离："+bean.getDistance()+"km", Toast.LENGTH_SHORT).show();

			if (bean.getOperatesta() == Boolean.TRUE) {
				tvState.setText("正在营业");

			} else {
				tvState.setText("暂停营业");
			}

			// 加载图片
			loadShopPicImpl(ivThumb, bean.getSpicture());

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
				BmobProFile pro_file = BmobProFile.getInstance(getActivity());
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

	class SearchAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mlvSearchs.size();
		}

		@Override
		public SearchBean getItem(int position) {
			return mlvSearchs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View layout = View.inflate(getActivity(), R.layout.item_search,
					null);

			ImageView ivThumb = (ImageView) layout.findViewById(R.id.ivThumb);
			TextView tvGoodName = (TextView) layout
					.findViewById(R.id.tvGoodName2);
			TextView tvShopName = (TextView) layout
					.findViewById(R.id.tvShopName);
			TextView sgtvtotalgood = (TextView) layout
					.findViewById(R.id.sgtvtotalgood);
			TextView ssdistance = (TextView) layout
					.findViewById(R.id.ssdistance);
			TextView sgtvsendprice = (TextView) layout
					.findViewById(R.id.sgtvsendprice);

			TextView tvsState = (TextView) layout.findViewById(R.id.tvsState);

			SearchBean bean = mlvSearchs.get(position);

			// Toast.makeText(getActivity(), bean.getShopName(),
			// Toast.LENGTH_SHORT).show();

			tvGoodName.setText(bean.getGoodName());
			tvShopName.setText(bean.getShopName());
			sgtvtotalgood.setText(bean.getTotalGood());
			sgtvsendprice.setText("起送价：￥" + bean.getSendPrice());
			ssdistance.setText("距离：" + bean.getDistance() + "km");

			if (bean.getOperatesta() == Boolean.TRUE) {
				tvsState.setText("正在营业");

			} else {
				tvsState.setText("暂停营业");
			}

			// 加载图片
			loadShopPicImpl(ivThumb, bean.getSpicture());

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
				BmobProFile pro_file = BmobProFile.getInstance(getActivity());
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

}