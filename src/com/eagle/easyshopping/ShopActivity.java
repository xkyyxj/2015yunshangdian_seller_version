package com.eagle.easyshopping;

import java.io.File;
import java.util.ArrayList;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.bmob.BmobProFile;

public class ShopActivity extends Activity {
	String TAG = "ShopActivity";
	ListView mlvFood;
	ListView mlvComment;
	FoodAdapter mAdapter;
	CommentAdapter mcAdapter;
	List<FoodBean> mlvFoods;
	List<CommentBean> mlvComments;
	// List<MyEditText> MT;
	// MyEditText mEditText;
	MyButton btn1;
	String cshopname;
	String cusername;
	List<ShopG> object2;
	List<ShopG> object3;
	List<ShopG> object4;
	List<ShopG> object5;
	List<Shop> object6;
	List<UserComment> object7;
	TextView title;

	// private int num;
	int[] resid = { R.drawable.sausage, R.drawable.sausages,
			R.drawable.sausage1, R.drawable.sausage2, R.drawable.sausage3,
			R.drawable.sausage4, R.drawable.sausage5 };

	int[] gresid = { R.drawable.orangenice, R.drawable.orangenice,
			R.drawable.orangenice, R.drawable.blacklow };

	TabHost tabHost;

	ImageView head;
	TextView stvComment;
	TextView dshopname;
	TextView dshopstate;
	TextView dtgood;
	TextView dqsend;
	TextView dpsend;

	Double qsend;
	Double psend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bmob.initialize(this, "474311be9f312229e370c28ebcac1d75");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test);

		tabHost = (TabHost) this.findViewById(android.R.id.tabhost);

		tabHost.setup();
		View home = this.getLayoutInflater().inflate(R.layout.goodtab, null);

		View search = this.getLayoutInflater().inflate(R.layout.shoptab, null);

		TabHost.TabSpec goodspec = tabHost.newTabSpec("good")
				.setIndicator(home).setContent(R.id.good);

		TabHost.TabSpec shopspec = tabHost.newTabSpec("shop")
				.setIndicator(search).setContent(R.id.shop);
		tabHost.addTab(goodspec);
		tabHost.addTab(shopspec);

		initView();
		mlvComment = (ListView) findViewById(R.id.lvComments);
		initData();
		/*// mlvComment = (ListView)findViewById(R.id.lvComments);
		mcAdapter = new CommentAdapter();
		// mcAdapter.notifyDataSetChanged();
		mlvComment.setAdapter(mcAdapter);*/

		findViewById(R.id.bok).setOnClickListener(new AccountClickListener());
		//findViewById(R.id.ibbBack).setOnClickListener(new BackListener());
		findViewById(R.id.bhot).setOnClickListener(new HotListener());
		findViewById(R.id.bsanck).setOnClickListener(new SnackListener());
		findViewById(R.id.bdaily).setOnClickListener(new DailyListener());
		findViewById(R.id.bfruit).setOnClickListener(new FruitListener());

		head = (ImageView) findViewById(R.id.ivhead1);
		stvComment = (TextView) findViewById(R.id.stvComment);

		dshopname = (TextView) findViewById(R.id.store_name);
		dshopstate = (TextView) findViewById(R.id.status);
		dtgood = (TextView) findViewById(R.id.Num1);
		dqsend = (TextView) findViewById(R.id.Num3);
		dpsend = (TextView) findViewById(R.id.Num4);
		title = (TextView) findViewById(R.id.tvTitle);
		
		
		Intent intent66 = getIntent();
		cshopname = intent66.getStringExtra("shopname");
		cusername = intent66.getStringExtra("username");
		dshopname.setText(cshopname);
		
		title.setText(cshopname);

		BmobQuery<Shop> query = new BmobQuery<Shop>();
		// 查询playerName叫“比目”的数据
		query.addWhereEqualTo("shopname", cshopname);
		// 返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(50);
		// 执行查询方法
		query.findObjects(ShopActivity.this, new FindListener<Shop>() {
			@Override
			public void onSuccess(List<Shop> object) {

				object6 = object;

				// TODO Auto-generated method stub
				// toast("查询成功：共"+object.size()+"条数据。");
				Toast.makeText(ShopActivity.this,
						"查询成功：共" + object6.size() + "条数据。", Toast.LENGTH_SHORT)
						.show();
				for (Shop gameScore : object) {
					// 获得playerName的信息
					// gameScore.getShopname();
					// 获得数据的objectId信息
					gameScore.getObjectId();
					// 获得createdAt数据创建时间（注意是：createdAt，不是createAt）
					gameScore.getCreatedAt();
				}

				// ImageView head =(ImageView)findViewById(R.id.ivhead1);

				for (int i = 0; i < object6.size(); i++) {

					loadShopPicImpl(head, object6.get(i).getSpicture());

					Log.v(TAG, "picture" + object6.get(i).getSpicture());

					// Toast.makeText(ShopActivity.this,object6.get(i).getSpicture(),
					// Toast.LENGTH_SHORT).show();
					dtgood.setText(object6.get(i).getTotalgood());
					dqsend.setText("起送价：￥" + object6.get(i).getSendprice());
					dpsend.setText("配送价：￥"
							+ object6.get(i).getDistributeprice());
					qsend = object6.get(i).getSendprice();
					psend = object6.get(i).getDistributeprice();

					if (object6.get(i).getOperatesta() == Boolean.TRUE) {
						dshopstate.setText("正在营业");

					} else {
						dshopstate.setText("暂停营业");
					}

				}

			}

			@Override
			public void onError(int code, String msg) {
				Toast.makeText(ShopActivity.this, "查询失败" + msg,
						Toast.LENGTH_SHORT).show();
			}
		});

		setListener();

	}

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		
	}

	class BackListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.ibbBack) {
				/*
				 * Intent intent = new Intent(AccountActivity.this,
				 * ShopActivity.class); startActivity(intent);
				 */
				ShopActivity.this.finish();

			}

		}

	}

	// 加载店家图片
	private void loadShopPicImpl(ImageView shop_pic, String pic_name) {
		Log.d(TAG, "in function loadShopPicImpl:loading");
		final ImageView view = shop_pic;
		Bitmap shop_bg = null;
		File file = null;
		if(pic_name != null)
			file = new File(pic_name);
		if (file != null&&file.exists()) {
			Log.i(TAG, "loading pic local?");
			shop_bg = BitmapFactory.decodeFile(pic_name);
			shop_pic.setImageBitmap(shop_bg);
		} else {
			if(pic_name != null)
			{
			BmobProFile pro_file = BmobProFile.getInstance(ShopActivity.this);
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

	class HotListener implements View.OnClickListener {

		@Override
        public void onClick(View v) {
            if (v.getId() == R.id.bhot) {
                //cshopname="胡先生的店铺";

                BmobQuery<ShopG> query = new BmobQuery<ShopG>();
                //查询playerName叫“比目”的数据
                query.addWhereEqualTo("shopname", cshopname);
                //返回50条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(50);
                //执行查询方法
                query.findObjects(ShopActivity.this, new FindListener<ShopG>() {
                    @Override
                    public void onSuccess(List<ShopG> object) {

                        object2=object;
                        // TODO Auto-generated method stub
                        //toast("查询成功：共"+object.size()+"条数据。");
                        Toast.makeText(ShopActivity.this, "查询成功：共"+object2.size()+"条数据。", Toast.LENGTH_SHORT).show();
                        for (ShopG gameScore : object) {
                            //获得playerName的信息
                            gameScore.getShopname();
                            //获得数据的objectId信息
                            gameScore.getObjectId();
                            //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                            gameScore.getCreatedAt();
                        }
/*
                        for(int i=0;i<object2.size();i++){
                            Toast.makeText(ShopActivity.this, object2.get(i).getGoodsname(), Toast.LENGTH_SHORT).show();
                        }*/

                        mlvFoods.clear();

                        for(int i=0;i<object2.size();i++){
                            Log.v(TAG,"adapter"+object2.get(i).getPrice());
                            FoodBean bean = new FoodBean(object2.get(i).getPicture(), object2.get(i).getGoodsname(), object2.get(i).getPrice(), 0, object2.get(i).getDescription());
                            //Toast.makeText(ShopActivity.this, object2.get(i).getGoodsname(), Toast.LENGTH_SHORT).show();
                            mlvFoods.add(bean);
                            //Toast.makeText(ShopActivity.this, bean.getName(), Toast.LENGTH_SHORT).show();

                        }

                        mAdapter = new FoodAdapter();
                        mlvFood.setAdapter(mAdapter);

                    }
                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(ShopActivity.this,"查询失败" +msg, Toast.LENGTH_SHORT).show();
                    }
                });


            }

        }
	}

	class SnackListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.bsanck) {

				// cshopname="胡先生的店铺";

				object3 = new LinkedList<ShopG>();

				BmobQuery<ShopG> eq1 = new BmobQuery<ShopG>();
				eq1.addWhereEqualTo("shopname", cshopname);
				BmobQuery<ShopG> eq2 = new BmobQuery<ShopG>();
				eq2.addWhereEqualTo("gcat", "零食");
				List<BmobQuery<ShopG>> queries = new ArrayList<BmobQuery<ShopG>>();
				queries.add(eq1);
				queries.add(eq2);
				BmobQuery<ShopG> mainQuery = new BmobQuery<ShopG>();
				mainQuery.and(queries);
				mainQuery.findObjects(ShopActivity.this,
						new FindListener<ShopG>() {
							@Override
							public void onSuccess(List<ShopG> object) {
								// TODO Auto-generated method stub

								object3 = object;
								mlvFoods.clear();
								for (int i = 0; i < object3.size(); i++) {
									FoodBean bean = new FoodBean(object3.get(i)
											.getPicture(), object3.get(i)
											.getGoodsname(), object3.get(i)
											.getPrice(), 0, object3.get(i)
											.getDescription());
									// Toast.makeText(ShopActivity.this,
									// object2.get(i).getGoodsname(),
									// Toast.LENGTH_SHORT).show();
									mlvFoods.add(bean);
									// Toast.makeText(ShopActivity.this,
									// bean.getName(),
									// Toast.LENGTH_SHORT).show();

								}
								Log.v(TAG, "snack" + object3.size());

								mAdapter = new FoodAdapter();
								mlvFood.setAdapter(mAdapter);
							}

							@Override
							public void onError(int code, String msg) {
								// TODO Auto-generated method stub

							}
						});

			}

		}

	}

	class DailyListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.bdaily) {
				// cshopname="胡先生的店铺";

				object4 = new LinkedList<ShopG>();

				BmobQuery<ShopG> eq1 = new BmobQuery<ShopG>();
				eq1.addWhereEqualTo("shopname", cshopname);
				BmobQuery<ShopG> eq2 = new BmobQuery<ShopG>();
				eq2.addWhereEqualTo("gcat", "日用品");
				List<BmobQuery<ShopG>> queries = new ArrayList<BmobQuery<ShopG>>();
				queries.add(eq1);
				queries.add(eq2);
				BmobQuery<ShopG> mainQuery = new BmobQuery<ShopG>();
				mainQuery.and(queries);
				mainQuery.findObjects(ShopActivity.this,
						new FindListener<ShopG>() {
							@Override
							public void onSuccess(List<ShopG> object) {
								// TODO Auto-generated method stub

								mlvFoods.clear();
								for (int i = 0; i < object4.size(); i++) {
									FoodBean bean = new FoodBean(object4.get(i)
											.getPicture(), object4.get(i)
											.getGoodsname(), object4.get(i)
											.getPrice(), 0, object4.get(i)
											.getDescription());
									// Toast.makeText(ShopActivity.this,
									// object2.get(i).getGoodsname(),
									// Toast.LENGTH_SHORT).show();
									mlvFoods.add(bean);
									// Toast.makeText(ShopActivity.this,
									// bean.getName(),
									// Toast.LENGTH_SHORT).show();

								}

								mAdapter = new FoodAdapter();
								mlvFood.setAdapter(mAdapter);
							}

							@Override
							public void onError(int code, String msg) {
								// TODO Auto-generated method stub

							}
						});
			}

		}

	}

	class FruitListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.bfruit) {

				// cshopname="胡先生的店铺";

				object5 = new LinkedList<ShopG>();

				BmobQuery<ShopG> eq1 = new BmobQuery<ShopG>();
				eq1.addWhereEqualTo("shopname", cshopname);
				BmobQuery<ShopG> eq2 = new BmobQuery<ShopG>();
				eq2.addWhereEqualTo("gcat", "水果");
				List<BmobQuery<ShopG>> queries = new ArrayList<BmobQuery<ShopG>>();
				queries.add(eq1);
				queries.add(eq2);
				BmobQuery<ShopG> mainQuery = new BmobQuery<ShopG>();
				mainQuery.and(queries);
				mainQuery.findObjects(ShopActivity.this,
						new FindListener<ShopG>() {
							@Override
							public void onSuccess(List<ShopG> object) {
								// TODO Auto-generated method stub

								mlvFoods.clear();
								for (int i = 0; i < object5.size(); i++) {
									FoodBean bean = new FoodBean(object5.get(i)
											.getPicture(), object5.get(i)
											.getGoodsname(), object5.get(i)
											.getPrice(), 0, object5.get(i)
											.getDescription());
									// Toast.makeText(ShopActivity.this,
									// object2.get(i).getGoodsname(),
									// Toast.LENGTH_SHORT).show();
									mlvFoods.add(bean);
									// Toast.makeText(ShopActivity.this,
									// bean.getName(),
									// Toast.LENGTH_SHORT).show();

								}

								mAdapter = new FoodAdapter();
								mlvFood.setAdapter(mAdapter);
							}

							@Override
							public void onError(int code, String msg) {
								// TODO Auto-generated method stub

							}
						});

			}

		}

	}

	private void setListener() {
		setOnItemClickListener();

	}

	public void setOnItemClickListener() {

		mlvFood.setFocusable(true);
		mlvFood.setFocusableInTouchMode(true);

		mlvFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(ShopActivity.this,
						DetailActivity.class);
				intent.putExtra("picture", mlvFoods.get(position).getPid());
				intent.putExtra("goodname", mlvFoods.get(position).getName());
				intent.putExtra("price", mlvFoods.get(position).getPrice());
				intent.putExtra("description", mlvFoods.get(position)
						.getDescription());
				// intent.putExtra("goodname",mlvFoods.get(position).getName());
				ShopActivity.this.startActivity(intent);

			}
		});

	}

	class AccountClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.bok) {

				Intent intent = new Intent(ShopActivity.this,
						AccountActivity.class);
				intent.putExtra("cshopname", cshopname);
				intent.putExtra("cusername", cusername);
				intent.putExtra("qsend", qsend);
				intent.putExtra("psend", psend);
				// intent.putExtra("test",23.4);
				intent.putExtra("size", "" + mlvFoods.size());
				for (int i = 0; i < mlvFoods.size(); i++) {
					intent.putExtra("foodbean" + i, mlvFoods.get(i));
					Log.v(TAG, "num" + mlvFoods.get(i).getNum());
				}

				ShopActivity.this.startActivity(intent);

			}

		}

	}

	private void initView() {
		mlvFood = (ListView) findViewById(R.id.lvFoods);
		// mAdapter = new FoodAdapter();
		// mcAdapter.notify();
		// mlvFood.setAdapter(mAdapter);

		/*
		 * mlvComment = (ListView)findViewById(R.id.lvComments); mcAdapter = new
		 * CommentAdapter(); //mcAdapter.notifyDataSetChanged();
		 * mlvComment.setAdapter(mcAdapter);
		 */

	}

	private void initData() {
		mlvFoods = new LinkedList<FoodBean>();
		mlvComments = new LinkedList<CommentBean>();
		object2 = new LinkedList<ShopG>();
		object7 = new LinkedList<UserComment>();

		// cshopname="胡先生的店铺";
		
		BmobQuery<ShopG> query = new BmobQuery<ShopG>();
		// 查询playerName叫“比目”的数据
		query.addWhereEqualTo("shopname", cshopname);
		// 返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(50);
		// 执行查询方法
		query.findObjects(ShopActivity.this, new FindListener<ShopG>() {
			@Override
			public void onSuccess(List<ShopG> object) {

				object2 = object;
				// TODO Auto-generated method stub
				// toast("查询成功：共"+object.size()+"条数据。");
				Toast.makeText(ShopActivity.this,
						"查询成功：共" + object2.size() + "条数据。", Toast.LENGTH_SHORT)
						.show();
				for (ShopG gameScore : object) {
					// 获得playerName的信息
					gameScore.getShopname();
					// 获得数据的objectId信息
					gameScore.getObjectId();
					// 获得createdAt数据创建时间（注意是：createdAt，不是createAt）
					gameScore.getCreatedAt();
				}
				/*
				 * for(int i=0;i<object2.size();i++){
				 * Toast.makeText(ShopActivity.this,
				 * object2.get(i).getGoodsname(), Toast.LENGTH_SHORT).show(); }
				 */

				for (int i = 0; i < object2.size(); i++) {
					Log.v(TAG, "adapter" + object2.get(i).getPrice());
					FoodBean bean = new FoodBean(object2.get(i).getPicture(),
							object2.get(i).getGoodsname(), object2.get(i)
									.getPrice(), 0, object2.get(i)
									.getDescription());
					// Toast.makeText(ShopActivity.this,
					// object2.get(i).getGoodsname(),
					// Toast.LENGTH_SHORT).show();
					mlvFoods.add(bean);
					// Toast.makeText(ShopActivity.this, bean.getName(),
					// Toast.LENGTH_SHORT).show();

				}

				mAdapter = new FoodAdapter();
				mlvFood.setAdapter(mAdapter);

			}

			@Override
			public void onError(int code, String msg) {
				Toast.makeText(ShopActivity.this, "查询失败" + msg,
						Toast.LENGTH_SHORT).show();
			}
		});

		
		
		
		
		BmobQuery<UserComment> query888 = new BmobQuery<UserComment>();
		// 查询playerName叫“比目”的数据
		query888.addWhereEqualTo("shopname", cshopname);
		// 返回50条数据，如果不加上这条语句，默认返回10条数据
		query888.setLimit(50);
		// 执行查询方法
		query888.findObjects(ShopActivity.this, new FindListener<UserComment>() {
			@Override
			public void onSuccess(List<UserComment> object) {

				object7 = object;
				// TODO Auto-generated method stub
				// toast("查询成功：共"+object.size()+"条数据。");
				Toast.makeText(ShopActivity.this,
						"查询成功：共" + object7.size() + "条数据。", Toast.LENGTH_SHORT)
						.show();
				for (UserComment gameScore : object) {
					// 获得playerName的信息
					gameScore.getShopname();
					// 获得数据的objectId信息
					gameScore.getObjectId();
					// 获得createdAt数据创建时间（注意是：createdAt，不是createAt）
					gameScore.getCreatedAt();
				}
				/*
				 * for(int i=0;i<object2.size();i++){
				 * Toast.makeText(ShopActivity.this,
				 * object2.get(i).getGoodsname(), Toast.LENGTH_SHORT).show(); }
				 */

				stvComment.setText("查看评论（共"+object7.size()+"条）");
				for (int i = 0; i < object7.size(); i++) {
					CommentBean bean = new CommentBean(object7.get(i).getState(),object7.get(i).getUsername(),object7.get(i).getContent());
					mlvComments.add(bean);
				}

				// mlvComment = (ListView)findViewById(R.id.lvComments);
				mcAdapter = new CommentAdapter();
				// mcAdapter.notifyDataSetChanged();
				mlvComment.setAdapter(mcAdapter);

			}

			@Override
			public void onError(int code, String msg) {
				Toast.makeText(ShopActivity.this, "查询失败" + msg,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		
		/*
		 * String[] names = getResources().getStringArray(R.array.foods);
		 * String[] prices = getResources().getStringArray(R.array.prices);
		 */
		String[] usernames = getResources().getStringArray(R.array.usernames);
		String[] times = getResources().getStringArray(R.array.times);
		String[] comments = getResources().getStringArray(R.array.comments);

		// int[] num = {0,0,0,0,0,0,0};
		// mlvFoods = new LinkedList<FoodBean>();
		//mlvComments = new LinkedList<CommentBean>();

		/*for (int i = 0; i < usernames.length; i++) {
			CommentBean bean = new CommentBean(gresid[i], usernames[i],
					times[i], comments[i]);
			mlvComments.add(bean);
		}*/

	}

	class FoodAdapter extends BaseAdapter {

		public FoodAdapter() {
			Log.v(TAG, "hello");
		}

		@Override
		public int getCount() {
			Log.v(TAG, "getCount" + mlvFoods.size());
			return mlvFoods.size();
		}

		@Override
		public FoodBean getItem(int position) {
			return mlvFoods.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			View layout = View.inflate(ShopActivity.this, R.layout.item_food,
					null);

			ImageView ivThumb = (ImageView) layout.findViewById(R.id.ivThumb);
			TextView tvName = (TextView) layout.findViewById(R.id.tvName);
			TextView tvPrice = (TextView) layout.findViewById(R.id.tvPrice);
			MyEditText mEditText = (MyEditText) layout.findViewById(R.id.meter);

			mEditText.setFoodBean(mlvFoods.get(position));

			btn1 = (MyButton) layout.findViewById(R.id.bbdd);
			btn1.setIndex(position);
			btn1.setOnClickListener(new VbClickListener(btn1, mEditText));

			FoodBean bean = mlvFoods.get(position);

			Log.v(TAG, "mlvFoods" + bean.getName());

			// 加载图片
			loadShopPicImpl(ivThumb, bean.getPid());

			// ivThumb.setImageResource(bean.getResid());
			tvName.setText("名称：" + bean.getName());
			tvPrice.setText("价格：￥" + bean.getPrice() + ".0");

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
						.getInstance(ShopActivity.this);
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

		class VbClickListener implements View.OnClickListener {
			VbClickListener(Button button, LinearLayout editText) {
				this.button = button;
				this.editText = editText;

			}

			private Button button;
			private LinearLayout editText;

			@Override
			public void onClick(View v) {
				// int index = ((MyButton)v).getIndex();
				// Toast.makeText(getApplicationContext(),
				// "单击我了"+v.getTag(),Toast.LENGTH_SHORT).show();

				if (cusername == null) {
					Intent intent = new Intent(ShopActivity.this,
							AccountActivity.class);
					ShopActivity.this.startActivity(intent);
				} else {
					button.setVisibility(View.INVISIBLE);
					editText.setVisibility(View.VISIBLE);
					// btn1.setIndex(index);
				}

			}
		}

	}

	class CommentAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mlvComments.size();
		}

		@Override
		public CommentBean getItem(int position) {
			return mlvComments.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View layout = View.inflate(ShopActivity.this,
					R.layout.item_comment, null);

			ImageView ivGood = (ImageView) layout.findViewById(R.id.ivGood);
			TextView tvUserName = (TextView) layout
					.findViewById(R.id.tvUserName);
			TextView tvTime = (TextView) layout.findViewById(R.id.tvTime);
			TextView tvCom = (TextView) layout.findViewById(R.id.tvCom);

			CommentBean bean = mlvComments.get(position);

			if(bean.getGresid()==Boolean.TRUE){
				ivGood.setImageResource(R.drawable.orangenice);
			}else{
				ivGood.setImageResource(R.drawable.blacklow);
			}
			
			tvUserName.setText(bean.getUsername());
			//tvTime.setText(bean.getTime());
			tvCom.setText(bean.getComment());

			return layout;
		}

	}
}
