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

//�����õ�λ�����ѹ��ܣ���Ҫimport����
//���ʹ�õ���Χ�����ܣ���Ҫimport������
public class HomeFragment extends Fragment implements
		OnGetGeoCoderResultListener {

	public EditText city;
	public EditText keyad;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	public TextView mLocationResult;
	public Button toPutAddress;
	public GeoCoder mSearch = null; // ����ģ��
	public String tempcoor = "gcj02";
	public LocationMode tempMode = LocationMode.Hight_Accuracy;
	public String address;
	public String errorCode;
	public String index = "�����ֶ������ַ";
	public String city_s;
	public String keyad_s;
	public View contentView;
	public View classesView;
	public View orderView;
	public PopupWindow popupAddWindow;
	public PopupWindow popupOrderWindow;
	public PopupWindow popupClassesWindow;
	public ImageButton button;
	public CurrentAdd cadd;// ��ǰ��ַ�� �����ϴ���ǰ�����ַ����γ��
	public double longitude, latitude;// ��Ž����õ��ľ�γ��
	public static final int STATE = 0;
	// ViewPager���
	private ArrayList<View> dots;
	private ViewPager mViewPager;
	private ViewPagerAdapter adapter;
	private View view1, view2, view3;
	private int oldPosition = 0;// ��¼��һ�ε��λ��
	private int currentItem; // ��ǰҳ��
	private List<View> viewList = new ArrayList<View>();// ����Ҫ������ҳ����ӵ����list��
	private BmobGeoPoint usergeo;
	// ���൯�����
	private Button order, classes, distance, easytosent, popular, wellserve;
	private ImageButton all, sm, ls, md, ff;
	// listview
	public List<Shop> homeshop;
	public ListView listforhome;

	public View rootview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
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
		// ��ʼ����ҳviewPager
		// ��ʼ��listview
		// ��ʾ�ĵ�

		return rootview;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// listview ��ʼ��

		dots = new ArrayList<View>();
		dots.add(getActivity().findViewById(R.id.dot_1));
		dots.add(getActivity().findViewById(R.id.dot_2));
		dots.add(getActivity().findViewById(R.id.dot_3));
		// �õ�viewPager�Ĳ���
		LayoutInflater lf = LayoutInflater.from(getActivity());
		view1 = lf.inflate(R.layout.viewpager_item1, null);
		view2 = lf.inflate(R.layout.viewpager_item2, null);
		view3 = lf.inflate(R.layout.viewpager_item3, null);
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		// �ҵ���������Ǹ���ť
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
		Log.e("����", "21");
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
				// ע��ʧ�ܣ��û�����ռ��
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
					Toast.makeText(getActivity(), "list δ��ѯ��",
							Toast.LENGTH_SHORT).show();

				}
			}
		});

		// ��ʼ������ģ�飬ע���¼�����

		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		Log.e("����", "����ʵ���ɹ�");

		mLocationClient = new LocationClient(getActivity()
				.getApplicationContext()); // ����LocationClient��
		mLocationClient.registerLocationListener(myListener); // ע���������
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// ���ö�λģʽ
		option.setCoorType(tempcoor);// ���صĶ�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02
		int span = 2000;
		option.setScanSpan(span);// ���÷���λ����ļ��ʱ��Ϊ2000ms
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
				Log.e("����", "location=null");
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

	// չʾ�ֶ������ַ�����Լ�==
	public void showAddPopupWindow(View view) {
		// ���ð�ť�ĵ���¼�

		button = (ImageButton) contentView.findViewById(R.id.sureBtn);
		city = (EditText) contentView.findViewById(R.id.cityInput);
		keyad = (EditText) contentView.findViewById(R.id.keyadInput);

		popupAddWindow = new PopupWindow(contentView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupAddWindow.setTouchable(true);

		// ʹ��ۼ�
		popupAddWindow.setFocusable(true);

		/*
		 * // ����������������ʧ popupWindow.setOutsideTouchable(false);
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
							"���л���ϸ��ַ����Ϊ��", Toast.LENGTH_LONG).show();
				} else {
					city_s = city.getText().toString();
					keyad_s = keyad.getText().toString();
					Log.e("����", city_s + keyad_s);
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
		// ���ð�ť�ĵ���¼�
		/*
		 * private Button order,classes,distance,easytosent,popular,wellserve;
		 * private ImageButton all,sm,ls,md,ff;
		 */

		easytosent = (Button) orderView.findViewById(R.id.easytosent);
		// ���������ȡ�� �°�ťΪ�ۺ�����

		popular = (Button) orderView.findViewById(R.id.popular);
		wellserve = (Button) orderView.findViewById(R.id.wellserve);

		popupOrderWindow = new PopupWindow(orderView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupOrderWindow.setTouchable(true);

		// ʹ��ۼ�
		popupOrderWindow.setFocusable(true);

		/*
		 * // ����������������ʧ popupWindow.setOutsideTouchable(false);
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
						// ע��ʧ�ܣ��û�����ռ��
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
							Toast.makeText(getActivity(), "list δ��ѯ��",
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
						// ע��ʧ�ܣ��û�����ռ��
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
							Toast.makeText(getActivity(), "list δ��ѯ��",
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
						// ע��ʧ�ܣ��û�����ռ��
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
							Toast.makeText(getActivity(), "list δ��ѯ��",
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
		// ���ð�ť�ĵ���¼�
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

		// ʹ��ۼ�
		popupClassesWindow.setFocusable(true);

		/*
		 * // ����������������ʧ popupWindow.setOutsideTouchable(false);
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
						// ע��ʧ�ܣ��û�����ռ��
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
							Toast.makeText(getActivity(), "list δ��ѯ��",
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
				query.addWhereEqualTo("scat", "�����");
				Log.e("rg", "3");
				query.findObjects(getActivity(), new FindListener<Shop>() {

					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.e("rg", "4");
					}

					public void onSuccess(List<Shop> arg0) {
						// TODO Auto-generated method stub
						// ע��ʧ�ܣ��û�����ռ��
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
							Toast.makeText(getActivity(), "list δ��ѯ��",
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
				query.addWhereEqualTo("scat", "�������");
				Log.e("rg", "3");
				query.findObjects(getActivity(), new FindListener<Shop>() {

					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.e("rg", "4");
					}

					public void onSuccess(List<Shop> arg0) {
						// TODO Auto-generated method stub
						// ע��ʧ�ܣ��û�����ռ��
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
							Toast.makeText(getActivity(), "list δ��ѯ��",
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
				query.addWhereEqualTo("scat", "ҩƷ����");
				Log.e("rg", "3");
				query.findObjects(getActivity(), new FindListener<Shop>() {

					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.e("rg", "4");
					}

					public void onSuccess(List<Shop> arg0) {
						// TODO Auto-generated method stub
						// ע��ʧ�ܣ��û�����ռ��
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
							Toast.makeText(getActivity(), "list δ��ѯ��",
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
				query.addWhereEqualTo("scat", "�����ʳ");
				Log.e("rg", "3");
				query.findObjects(getActivity(), new FindListener<Shop>() {

					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.e("rg", "4");
					}

					public void onSuccess(List<Shop> arg0) {
						// TODO Auto-generated method stub
						// ע��ʧ�ܣ��û�����ռ��
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
							Toast.makeText(getActivity(), "list δ��ѯ��",
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
		// ע�⣺���ܵ���gameScore.setObjectId("")����
		cadd.setCrtAdd(add);
		cadd.setCrtGeoAdd(bgp);

		cadd.save(this.getActivity().getApplicationContext(),
				new SaveListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Log.i("upDateAdd",
								"������ݳɹ������ص�ַIDΪ��" + cadd.getObjectId()
										+ ",�����ڷ���˵Ĵ���ʱ��Ϊ��"
										+ cadd.getCreatedAt());
						Log.i("upDateAdd", "������ݳɹ������ص�ַ����Ϊ��" + cadd.getCrtAdd());
						Log.i("upDateAdd", "������ݳɹ�������longitudeΪ��"
								+ cadd.getCrtGeoAdd().getLongitude());
						Log.i("upDateAdd", "������ݳɹ�������latitudeΪ��"
								+ cadd.getCrtGeoAdd().getLatitude());
					}

					@Override
					public void onFailure(int code, String arg0) {
						// TODO Auto-generated method stub
						Log.e("upDateAdd", "�������ʧ��");
					}
				});

		return;
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "��Ǹ��δ���ҵ����", Toast.LENGTH_LONG)
					.show();
			return;
		}
		/* Log.e("����", "onGetGeoCode"); */
		latitude = result.getLocation().latitude;

		longitude = result.getLocation().longitude;
		usergeo = new BmobGeoPoint(longitude, latitude);

		String strInfo = String.format("γ�ȣ�%f ���ȣ�%f", latitude, longitude);

		Log.e("����", strInfo);

		upDateAdd(city_s + keyad_s, longitude, latitude);

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

		// TODO �Զ����ɵķ������
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "��Ǹ��δ���ҵ����", Toast.LENGTH_LONG)
					.show();
			return;
		}
	}

	private class ViewPagerAdapter extends PagerAdapter {

		public ViewPagerAdapter() {
			super();
			// TODO Auto-generated constructor stub
			// �õ�viewpager�л����������֣��������Ǽ��뵽viewList����,�ǵ�view�ô���Ͳ���ɣ��������׳��ֿ�ָ���쳣

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return viewList.size();
		}

		// �Ƿ���ͬһ��ͼƬ
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