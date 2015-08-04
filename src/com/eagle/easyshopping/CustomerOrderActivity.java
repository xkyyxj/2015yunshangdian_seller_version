package com.eagle.easyshopping;

import android.animation.ValueAnimator;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class CustomerOrderActivity extends FragmentActivity {

    public static final String MY_TAG = "RUN:";


    public static String BMOB_API_KEY = "5eb7b9f34bd4f213f1b9da322b9e5f37";

    private int creating_adapter = -1;

    private int offset = 0,slide_bar_width = 0,previous_position = 0;
    private boolean click_to_select = false;

    private int current_index = 0;

    //index 0:waiting_order_list; index 1:finished_oder_list
    private List<OrderInfoFragment> fragment_list = null;
    private List<Ord> ord_content = null;
    private List<String> shop_obj_list = null;
    private Map<String,Shop> map = null;

    private TextView title = null;

    private ImageButton return_button = null;
    private Button waiting_button,finished_button;
    private ViewPager pager = null;
    private ImageView slide_bar = null;

    private MyButtonListener button_li = null;
    private MyTranslateAnimatorListener animation_listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.customer_order_layout);
        //Bmob.initialize(getApplicationContext(), BMOB_API_KEY);
        initFindArray();
        initView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(current_index == 0)
            initWaitingOrdAdapter();
        else
            initFinishedOrdAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initFindArray()
    {
        shop_obj_list = new ArrayList<String>();
        map = new HashMap<String,Shop>();
    }

    //��ݵ�ǰ�û�����ѯ������Ϣ
    //state��?��״̬
    private void loadOrdInfo(List<String> state){
        User user = ((MyApplication)getApplication()).getUser();
        BmobQuery<Ord> query1 = new BmobQuery<Ord>();
        //�������ˣ���Ӧ����objectId,Ӧ����user.name�ġ���ΪOrd����ֻ�洢��username�ֶΣ�����User�?
        query1.addWhereEqualTo("username", user.getUsername());
        query1.addWhereContainedIn("jystate",state);
        query1.findObjects(this, new FindListener<Ord>() {
            @Override
            public void onSuccess(List<Ord> list) {
                ord_content = list;
                Log.e(MY_TAG, "loading_ord_info_success");
                for (Ord ord : list) {
                    shop_obj_list.add(ord.getShopname());
                }
                loadShopInfo(shop_obj_list);
            }

            @Override
            public void onError(int i, String s) {
                Log.e(MY_TAG, "loading_order_info_wrong " + s);
            }
        });
    }

    private void setCurrentItem(int index)
    {
        current_index = index;
        pager.setCurrentItem(index);
    }

    //��ѯ�̵���Ϣ����ݶ�������Ʒ�����̼�
    private void loadShopInfo(List<String> shop_obj_list) {
        BmobQuery<Shop> query = new BmobQuery<Shop>();
        query.addWhereContainedIn("shopname", shop_obj_list);
        query.findObjects(this, new FindListener<Shop>() {
            @Override
            public void onSuccess(List<Shop> list) {
                for (Shop shop : list) {
                    map.put(shop.getShopname(), shop);
                }
                Log.e(MY_TAG, "loading_shop_info_success");
                if (creating_adapter == OrderInfoListAdapter.WAITING_ORDER) {
                    fragment_list.get(0).notifyDataChange(ord_content, map);
                } else {
                    fragment_list.get(1).notifyDataChange(ord_content, map);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.e(MY_TAG, "loading_shop_info_wrong " + s);
            }
        });
    }

    private void initView()
    {
        //title name
        title = (TextView)findViewById(R.id.activity_name);
        title.setText(R.string.activity_customer_order_title);

        //fragment init
        OrderInfoFragment fragment = null;
        fragment_list = new ArrayList<OrderInfoFragment>();
        fragment = new OrderInfoFragment();
        fragment.setFragmentType(OrderInfoListAdapter.WAITING_ORDER);
        fragment_list.add(fragment);
        fragment = new OrderInfoFragment();
        fragment.setFragmentType(OrderInfoListAdapter.FINISHED_ORDER);
        fragment_list.add(fragment);

        //init slide bar
        slide_bar = (ImageView)findViewById(R.id.slide_bar);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screen_width = metrics.widthPixels;
        slide_bar_width = BitmapFactory.decodeResource(getResources(),R.drawable.huadong).getWidth();
        offset = (screen_width / 2 - slide_bar_width) / 2;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset,0);
        slide_bar.setImageMatrix(matrix);

        //viewPager init
        pager = (ViewPager)findViewById(R.id.cus_order_pager);
        pager.setAdapter(new MyFragmentPagerAdatper(getSupportFragmentManager(), fragment_list));
        pager.setOnPageChangeListener(new MyOnPageChangeListener());

        //button init
        waiting_button = (Button)findViewById(R.id.waiting);
        finished_button = (Button)findViewById(R.id.finished);
        return_button = (ImageButton)findViewById(R.id.return_button);
        button_li = new MyButtonListener();
        waiting_button.setOnClickListener(button_li);
        finished_button.setOnClickListener(button_li);
        return_button.setOnClickListener(button_li);

        animation_listener = new MyTranslateAnimatorListener();
    }

    //��ʼ������ɶ���Adapter
    private void initFinishedOrdAdapter()
    {
        List<String> list = new ArrayList<String>();
        creating_adapter = OrderInfoListAdapter.FINISHED_ORDER;
        list.add("21");
        list.add("22");
        list.add("23");
        loadOrdInfo(list);
    }

    //��ʼ�����?��Adapter
    private void initWaitingOrdAdapter()
    {
        List<String> list = new ArrayList<String>();
        creating_adapter = OrderInfoListAdapter.WAITING_ORDER;
        list.add("0");
        list.add("11");
        list.add("12");
        loadOrdInfo(list);
    }

    class MyFragmentPagerAdatper extends FragmentPagerAdapter
    {
        List<OrderInfoFragment> list = null;
        public MyFragmentPagerAdatper(FragmentManager fm,List<OrderInfoFragment> _list) {
            super(fm);
            list = _list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list != null ? list.size() : 0;
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener
    {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Matrix matrix = new Matrix();
            Log.v(MY_TAG,"" + positionOffset + " " + positionOffsetPixels);
            if(positionOffset != 0 && !click_to_select) {
                matrix.postTranslate(offset + (offset * 2 + slide_bar_width) * positionOffset, 0);
                slide_bar.setImageMatrix(matrix);
            }
            if(previous_position != position)
            {
                click_to_select = false;
                previous_position = position;
            }
        }

        @Override
        public void onPageSelected(int position) {
            ValueAnimator animation = null;
            current_index = position;
            switch(position)
            {
                case 0:
                    Log.e(MY_TAG, "onPageSelected:" + click_to_select);
                    if(click_to_select) {
                        animation = ValueAnimator.ofInt(offset * 3 + slide_bar_width,offset);
                        animation.setDuration(300);
                        animation.addUpdateListener(animation_listener);
                        animation.start();
                    }
                    initWaitingOrdAdapter();
                    break;
                case 1:
                    Log.e(MY_TAG, "onPageSelected:" + click_to_select);
                    if(click_to_select) {
                        animation = ValueAnimator.ofInt(offset,offset * 3 + slide_bar_width);
                        animation.addUpdateListener(animation_listener);
                        animation.setDuration(300);
                        animation.start();
                    }
                    initFinishedOrdAdapter();
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyButtonListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.finished)
            {
                click_to_select = true;
                setCurrentItem(1);
            }
            else if(v.getId() == R.id.waiting)
            {
                click_to_select = true;
                setCurrentItem(0);
            }
            else if(v.getId() == R.id.return_button)
                finish();
        }
    }

    class MyTranslateAnimatorListener implements ValueAnimator.AnimatorUpdateListener
    {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            Matrix matrix = new Matrix();
            int value = (Integer)animation.getAnimatedValue();
            Log.e(MY_TAG,"in onAnimationUpdate function" + value);
            matrix.postTranslate(value,0);
            slide_bar.setImageMatrix(matrix);
        }
    }
}
