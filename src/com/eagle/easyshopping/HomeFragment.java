package com.eagle.easyshopping;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

//假如用到位置提醒功能，需要import该类
//如果使用地理围栏功能，需要import如下类
public class HomeFragment extends Fragment implements
		OnGetGeoCoderResultListener {

	public EditText city;
	public EditText keyad;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	public TextView mLocationResult;
	public Button toPutAddress;
	public GeoCoder mSearch = null; // 搜索模块
	public String tempcoor = "gcj02";
	public LocationMode tempMode = LocationMode.Hight_Accuracy;
	public String address;
	public String errorCode;
	public String index = "点我手动输入地址";
	public String city_s;
	public String keyad_s;
	public View contentView;
	public View classesView;
	public View orderView;
	public PopupWindow popupAddWindow;
	public PopupWindow popupOrderWindow;
	public PopupWindow popupClassesWindow;
	public ImageButton button;
	public CurrentAdd cadd;// 当前地址类 用于上传当前物理地址及经纬度
	public double longitude, latitude;// 存放解析得到的经纬度
	public static final int STATE = 0;
	// ViewPager相关
	private ArrayList<View> dots;
	private ViewPager mViewPager;
	private ViewPagerAdapter adapter;
	private View view1, view2, view3;
	private int oldPosition = 0;// 记录上一次点的位置
	private int currentItem; // 当前页面
	private List<View> viewList = new ArrayList<View>();// 把需要滑动的页卡添加到这个list中
	private BmobGeoPoint usergeo;
	// 分类弹窗相关
	private Button order, classes, distance, easytosent, popular, wellserve;
	private ImageButton all, sm, ls, md, ff;
	// listview
	public List<Shop> homeshop;
	public ListView listforhome;

	public View rootview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		rootview = inflater.inflate(R.layout.home_layout, container, false);

		contentView = inflater.from(getActivity()).inflate(
				R.layout.address_input, null);

		orderView = inflater.from(getActivity()).inflate(R.layout.order_layout,
				null);
		classesView = inflater.from(getActivity()).inflate(
				R.layout.classes_layout, null);
		// 初始化主页viewPager
		// 初始化listview
		// 显示的点

		return rootview;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// listview 初始化

		dots = new ArrayList<View>();
		dots.add(getActivity().findViewById(R.id.dot_1));
		dots.add(getActivity().findViewById(R.id.dot_2));
		dots.add(getActivity().findViewById(R.id.dot_3));
		// 得到viewPager的布局
		LayoutInflater lf = LayoutInflater.from(getActivity());
		view1 = lf.inflate(R.layout.viewpager_item1, null);
		view2 = lf.inflate(R.layout.viewpager_item2, null);
		view3 = lf.inflate(R.layout.viewpager_item3, null);
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		// 找到点击进入那个按钮
		mViewPager = (ViewPager) getActivity().findViewById(R.id.vp);
		adapter = new ViewPagerAdapter();
		mViewPager.setAdapter(adapter);
		dots.get(0).setBackgroundResource(R.drawable.dot_focused);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub

				dots.get(oldPosition).setBackgroundResource(
						R.drawable.dot_normal);
				dots.get(position)
						.setBackgroundResource(R.drawable.dot_focused);

				oldPosition = position;
				currentItem = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		Log.e("测试", "21");
		toPutAddress = (Button) getActivity().findViewById(R.id.address);
		toPutAddress.setText(index);
		order = (Button) getActivity().findViewById(R.id.order);
		classes = (Button) getActivity().findViewById(R.id.classes);
		listforhome = (ListView) getActivity().findViewById(R.id.list_view);
		if (listforhome != null) {
			Log.e("rg", "not null");
		}
		listforhome
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						Intent intent = new Intent(getActivity(),
								ShopActivity.class);
						intent.putExtra("username",
								((MainActivity) getActivity()).getUsername());
						intent.putExtra("shopname", homeshop.get(position)
								.getShopname());
						getActivity().startActivity(intent);
					}
				});
		BmobQuery<Shop> query = new BmobQuery<Shop>();
		query.order("susername");
		Log.e("rg", "3");
		query.findObjects(getActivity(), new FindListener<Shop>() {

			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.e("rg", "4");
			}

			public void onSuccess(List<Shop> arg0) {
				// TODO Auto-generated method stub
				// 注册失败，用户名被占用
				Log.e("rg", "44");
				if (arg0.size() != 0) {
					Log.e("rg", "5");
					Log.e("rg", "" + arg0.size());

					if (listforhome != null) {
						if(usergeo==null){
							Log.e("rg","usergeo null");
						}
						int i;
						Double dis = 2.5;
						for (i = 0; i < arg0.size(); i++) {
							if (dis- arg0.get(i).getSlonla()
											.distanceInKilometersTo(usergeo) < 0) {
								arg0.remove(i);
							}
						}if (arg0==null){
							Log.e("rg","usergeo null");
						}
						homeshop = arg0;
						if (getActivity()==null){
							Log.e("rg","a null");
						}
						HomeAdapter ha = new HomeAdapter(getActivity(), arg0,
								usergeo);
						listforhome.setAdapter(ha);
					}
				} else {
					Log.e("rg", "6");
					Toast.makeText(getActivity(), "list 未查询到",
							Toast.LENGTH_SHORT).show();

				}
			}
		});

		// 初始化搜索模块，注册事件监听

		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		Log.e("测试", "创建实例成功");

		mLocationClient = new LocationClient(getActivity()
				.getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 2000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为2000ms
		option.setIsNeedAddress(true);
		option.setOpenGps(true);

		mLocationClient.setLocOption(option);
		mLocationClient.start();
		mLocationClient.requestLocation();

		toPutAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showAddPopupWindow(v);
			}
		});
		order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showOrderPopupWindow(v);
			}
		});
		classes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showClassesPopupWindow(v);
			}
		});

		Log.e("ls", "3");

	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				Log.e("测试", "location=null");
				return;
			}
			if (location.getAddrStr() != null) {
				address = location.getAddrStr();
				double la = location.getLatitude();
				double lo = location.getLongitude();
				usergeo = new BmobGeoPoint(lo, la);
				
				toPutAddress.setText(address);

				/* errorCode.append(location.getLocType()); */
			}

		}
	}

	// 展示手动输入地址弹窗以及==
	public void showAddPopupWindow(View view) {
		// 设置按钮的点击事件

		button = (ImageButton) contentView.findViewById(R.id.sureBtn);
		city = (EditText) contentView.findViewById(R.id.cityInput);
		keyad = (EditText) contentView.findViewById(R.id.keyadInput);

		popupAddWindow = new PopupWindow(contentView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

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

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupAddWindow.dismiss();
				mLocationClient.stop();
				if (city.getText().toString() == null
						| keyad.getText().toString() == null
						| city.getText().toString() == null
						| city.getText().toString().equals("")
						| keyad.getText().toString().equals("")) {
					Toast.makeText(getActivity().getApplicationContext(),
							"城市或详细地址不能为空", Toast.LENGTH_LONG).show();
				} else {
					city_s = city.getText().toString();
					keyad_s = keyad.getText().toString();
					Log.e("测试", city_s + keyad_s);
					toPutAddress.setText(city_s + keyad_s);

					mSearch.geocode(new GeoCodeOption().city(city_s).address(
							keyad_s));

				}

			}
		});
		popupAddWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.addressbg));

		popupAddWindow.update();

		popupAddWindow.showAsDropDown(view);

	}

	public void showOrderPopupWindow(View view) {
		// 设置按钮的点击事件
		/*
		 * private Button order,classes,distance,easytosent,popular,wellserve;
		 * private ImageButton all,sm,ls,md,ff;
		 */

		easytosent = (Button) orderView.findViewById(R.id.easytosent);
		// 销量最高已取消 下按钮为综合排序

		popular = (Button) orderView.findViewById(R.id.popular);
		wellserve = (Button) orderView.findViewById(R.id.wellserve);

		popupOrderWindow = new PopupWindow(orderView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupOrderWindow.setTouchable(true);

		// 使其聚集
		popupOrderWindow.setFocusable(true);

		/*
		 * // 设置允许在外点击消失 popupWindow.setOutsideTouchable(false);
		 */
		popupOrderWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("mengdd", "onTouch : ");

				return false;

			}
		});

		easytosent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupOrderWindow.dismiss();
				BmobQuery<Shop> query = new BmobQuery<Shop>();
				query.order("sendprice");
				Log.e("rg", "3");
				query.findObjects(getActivity(), new FindListener<Shop>() {

					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.e("rg", "4");
					}

					public void onSuccess(List<Shop> arg0) {
						// TODO Auto-generated method stub
						// 注册失败，用户名被占用
						Log.e("rg", "44");
						if (arg0.size() != 0) {
							Log.e("rg", "5");
							Log.e("rg", "" + arg0.size());

							if (listforhome != null) {
								int i;
								Double dis = 2.5;
								for (i = 0; i < arg0.size(); i++) {
									if (dis
											- arg0.get(i)
													.getSlonla()
													.distanceInKilometersTo(
															usergeo) < 0) {
										arg0.remove(i);
									}
								}
								homeshop = arg0;
								HomeAdapter ha = new HomeAdapter(getActivity(),
										arg0, usergeo);

								listforhome.setAdapter(ha);
								ha.notifyDataSetChanged();

							}
						} else {
							Log.e("rg", "6");
							Toast.makeText(getActivity(), "list 未查询到",
									Toast.LENGTH_SHORT).show();

						}
					}
				});

			}
		});
		popular.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupOrderWindow.dismiss();
				BmobQuery<Shop> query = new BmobQuery<Shop>();
				query.order("shopname");
				Log.e("rg", "3");
				query.findObjects(getActivity(), new FindListener<Shop>() {

					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.e("rg", "4");
					}

					public void onSuccess(List<Shop> arg0) {
						// TODO Auto-generated method stub
						// 注册失败，用户名被占用
						Log.e("rg", "44");
						if (arg0.size() != 0) {
							Log.e("rg", "5");
							Log.e("rg", "" + arg0.size());

							if (listforhome != null) {
								int i;
								Double dis = 2.5;
								for (i = 0; i < arg0.size(); i++) {
									if (dis
											- arg0.get(i)
													.getSlonla()
													.distanceInKilometersTo(
															usergeo) < 0) {
										arg0.remove(i);
									}
								}
								homeshop = arg0;
								HomeAdapter ha = new HomeAdapter(getActivity(),
										arg0, usergeo);

								listforhome.setAdapter(ha);
								ha.notifyDataSetChanged();

							}
						} else {
							Log.e("rg", "6");
							Toast.makeText(getActivity(), "list 未查询到",
									Toast.LENGTH_SHORT).show();

						}
					}
				});

			}
		});

		wellserve.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupOrderWindow.dismiss();
				BmobQuery<Shop> query = new BmobQuery<Shop>();
				query.order("-totalgood");
				Log.e("rg", "3");
				query.findObjects(getActivity(), new FindListener<Shop>() {

					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.e("rg", "4");
					}

					public void onSuccess(List<Shop> arg0) {
						// TODO Auto-generated method stub
						// 注册失败，用户名被占用
						Log.e("rg", "44");
						if (arg0.size() != 0) {
							Log.e("rg", "5");
							Log.e("rg", "" + arg0.size());

							if (listforhome != null) {
								int i;
								Double dis = 2.5;
								for (i = 0; i < arg0.size(); i++) {
									if (dis
											- arg0.get(i)
													.getSlonla()
													.distanceInKilometersTo(
															usergeo) < 0) {
										arg0.remove(i);
									}
								}
								homeshop = arg0;
								HomeAdapter ha = new HomeAdapter(getActivity(),
										arg0, usergeo);

								listforhome.setAdapter(ha);
								ha.notifyDataSetChanged();

							}
						} else {
							Log.e("rg", "6");
							Toast.makeText(getActivity(), "list 未查询到",
									Toast.LENGTH_SHORT).show();

						}
					}
				});
			}
		});

		popupOrderWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.addressbg));

		popupOrderWindow.update();

		popupOrderWindow.showAsDropDown(view);

	}

	public void showClassesPopupWindow(View view) {
		// 设置按钮的点击事件
		/*
		 * private Button order,classes,distance,easytosent,popular,wellserve;
		 * private ImageButton all,sm,ls,md,ff;
		 */
		all = (ImageButton) classesView.findViewById(R.id.all);
		sm = (ImageButton) classesView.findViewById(R.id.sm);
		ls = (ImageButton) classesView.findViewById(R.id.ls);
		md = (ImageButton) classesView.findViewById(R.id.md);
		ff = (ImageButton) classesView.findViewById(R.id.ff);

		popupClassesWindow = new PopupWindow(classesView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupClassesWindow.setTouchable(true);

		// 使其聚集
		popupClassesWindow.setFocusable(true);

		/*
		 * // 设置允许在外点击消失 popupWindow.setOutsideTouchable(false);
		 */
		popupClassesWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("mengdd", "onTouch : ");

				return false;

			}
		});

		all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupClassesWindow.dismiss();
				BmobQuery<Shop> query = new BmobQuery<Shop>();
				query.order("scat");
				Log.e("rg", "3");
				query.findObjects(getActivity(), new FindListener<Shop>() {

					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.e("rg", "4");
					}

					public void onSuccess(List<Shop> arg0) {
						// TODO Auto-generated method stub
						// 注册失败，用户名被占用
						Log.e("rg", "44");
						if (arg0.size() != 0) {
							Log.e("rg", "5");
							Log.e("rg", "" + arg0.size());

							if (listforhome != null) {
								int i;
								Double dis = 2.5;
								for (i = 0; i < arg0.size(); i++) {
									if (dis
											- arg0.get(i)
													.getSlonla()
													.distanceInKilometersTo(
															usergeo) < 0) {
										arg0.remove(i);
									}
								}
								homeshop = arg0;
								HomeAdapter ha = new HomeAdapter(getActivity(),
										arg0, usergeo);

								listforhome.setAdapter(ha);
								ha.notifyDataSetChanged();

							}
						} else {
							Log.e("rg", "6");
							Toast.makeText(getActivity(), "list 未查询到",
									Toast.LENGTH_SHORT).show();

						}
					}
				});
			}
		});
		sm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupClassesWindow.dismiss();
				BmobQuery<Shop> query = new BmobQuery<Shop>();
				query.addWhereEqualTo("scat", "生活超市");
				Log.e("rg", "3");
				query.findObjects(getActivity(), new FindListener<Shop>() {

					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.e("rg", "4");
					}

					public void onSuccess(List<Shop> arg0) {
						// TODO Auto-generated method stub
						// 注册失败，用户名被占用
						Log.e("rg", "44");
						if (arg0.size() != 0) {
							Log.e("rg", "5");
							Log.e("rg", "" + arg0.size());

							if (listforhome != null) {
								int i;
								Double dis = 2.5;
								for (i = 0; i < arg0.size(); i++) {
									if (dis
											- arg0.get(i)
													.getSlonla()
													.distanceInKilometersTo(
															usergeo) < 0) {
										arg0.remove(i);
									}
								}
								homeshop = arg0;
								HomeAdapter ha = new HomeAdapter(getActivity(),
										arg0, usergeo);

								listforhome.setAdapter(ha);
								ha.notifyDataSetChanged();

							}
						} else {
							Log.e("rg", "6");
							Toast.makeText(getActivity(), "list 未查询到",
									Toast.LENGTH_SHORT).show();

						}
					}
				});
			}
		});
		ls.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupClassesWindow.dismiss();
				BmobQuery<Shop> query = new BmobQuery<Shop>();
				query.addWhereEqualTo("scat", "生活服务");
				Log.e("rg", "3");
				query.findObjects(getActivity(), new FindListener<Shop>() {

					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.e("rg", "4");
					}

					public void onSuccess(List<Shop> arg0) {
						// TODO Auto-generated method stub
						// 注册失败，用户名被占用
						Log.e("rg", "44");
						if (arg0.size() != 0) {
							Log.e("rg", "5");
							Log.e("rg", "" + arg0.size());

							if (listforhome != null) {
								int i;
								Double dis = 2.5;
								for (i = 0; i < arg0.size(); i++) {
									if (dis
											- arg0.get(i)
													.getSlonla()
													.distanceInKilometersTo(
															usergeo) < 0) {
										arg0.remove(i);
									}
								}
								homeshop = arg0;
								HomeAdapter ha = new HomeAdapter(getActivity(),
										arg0, usergeo);

								listforhome.setAdapter(ha);
								ha.notifyDataSetChanged();

							}
						} else {
							Log.e("rg", "6");
							Toast.makeText(getActivity(), "list 未查询到",
									Toast.LENGTH_SHORT).show();

						}
					}
				});
			}
		});

		md.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupClassesWindow.dismiss();
				BmobQuery<Shop> query = new BmobQuery<Shop>();
				query.addWhereEqualTo("scat", "药品保健");
				Log.e("rg", "3");
				query.findObjects(getActivity(), new FindListener<Shop>() {

					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.e("rg", "4");
					}

					public void onSuccess(List<Shop> arg0) {
						// TODO Auto-generated method stub
						// 注册失败，用户名被占用
						Log.e("rg", "44");
						if (arg0.size() != 0) {
							Log.e("rg", "5");
							Log.e("rg", "" + arg0.size());

							if (listforhome != null) {
								int i;
								Double dis = 2.5;
								for (i = 0; i < arg0.size(); i++) {
									if (dis
											- arg0.get(i)
													.getSlonla()
													.distanceInKilometersTo(
															usergeo) < 0) {
										arg0.remove(i);
									}
								}
								homeshop = arg0;
								HomeAdapter ha = new HomeAdapter(getActivity(),
										arg0, usergeo);

								listforhome.setAdapter(ha);
								ha.notifyDataSetChanged();

							}
						} else {
							Log.e("rg", "6");
							Toast.makeText(getActivity(), "list 未查询到",
									Toast.LENGTH_SHORT).show();

						}
					}
				});
			}
		});
		ff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupClassesWindow.dismiss();
				BmobQuery<Shop> query = new BmobQuery<Shop>();
				query.addWhereEqualTo("scat", "快餐甜食");
				Log.e("rg", "3");
				query.findObjects(getActivity(), new FindListener<Shop>() {

					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.e("rg", "4");
					}

					public void onSuccess(List<Shop> arg0) {
						// TODO Auto-generated method stub
						// 注册失败，用户名被占用
						Log.e("rg", "44");
						if (arg0.size() != 0) {
							Log.e("rg", "5");
							Log.e("rg", "" + arg0.size());

							if (listforhome != null) {
								int i;
								Double dis = 2.5;
								for (i = 0; i < arg0.size(); i++) {
									if (dis
											- arg0.get(i)
													.getSlonla()
													.distanceInKilometersTo(
															usergeo) < 0) {
										arg0.remove(i);
									}
								}
								homeshop = arg0;
								HomeAdapter ha = new HomeAdapter(getActivity(),
										arg0, usergeo);

								listforhome.setAdapter(ha);
								ha.notifyDataSetChanged();

							}
						} else {
							Log.e("rg", "6");
							Toast.makeText(getActivity(), "list 未查询到",
									Toast.LENGTH_SHORT).show();

						}
					}
				});
			}
		});

		popupClassesWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.addressbg));

		popupClassesWindow.update();

		popupClassesWindow.showAsDropDown(view);

	}

	public void upDateAdd(String add, Double longitude, Double latitude) {
		BmobGeoPoint bgp = new BmobGeoPoint();
		bgp.setLatitude(latitude);
		bgp.setLongitude(longitude);
		cadd = new CurrentAdd();
		// 注意：不能调用gameScore.setObjectId("")方法
		cadd.setCrtAdd(add);
		cadd.setCrtGeoAdd(bgp);

		cadd.save(this.getActivity().getApplicationContext(),
				new SaveListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Log.i("upDateAdd",
								"添加数据成功，返回地址ID为：" + cadd.getObjectId()
										+ ",数据在服务端的创建时间为："
										+ cadd.getCreatedAt());
						Log.i("upDateAdd", "添加数据成功，返回地址具体为：" + cadd.getCrtAdd());
						Log.i("upDateAdd", "添加数据成功，返回longitude为："
								+ cadd.getCrtGeoAdd().getLongitude());
						Log.i("upDateAdd", "添加数据成功，返回latitude为："
								+ cadd.getCrtGeoAdd().getLatitude());
					}

					@Override
					public void onFailure(int code, String arg0) {
						// TODO Auto-generated method stub
						Log.e("upDateAdd", "添加数据失败");
					}
				});

		return;
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		/* Log.e("测试", "onGetGeoCode"); */
		latitude = result.getLocation().latitude;

		longitude = result.getLocation().longitude;
		usergeo = new BmobGeoPoint(longitude, latitude);

		String strInfo = String.format("纬度：%f 经度：%f", latitude, longitude);

		Log.e("测试", strInfo);

		upDateAdd(city_s + keyad_s, longitude, latitude);

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

		// TODO 自动生成的方法存根
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
	}

	private class ViewPagerAdapter extends PagerAdapter {

		public ViewPagerAdapter() {
			super();
			// TODO Auto-generated constructor stub
			// 得到viewpager切换的三个布局，并把它们加入到viewList里面,记得view用打气筒生成，否则容易出现空指针异常

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return viewList.size();
		}

		// 是否是同一张图片
		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			((ViewPager) view).removeView(viewList.get(position));

		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			((ViewPager) view).addView(viewList.get(position));
			return viewList.get(position);
		}
	}

}