package com.eagle.easyshopping;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;


public class WaitingOrderItemActivity extends Activity {

    private static final String TAG = "RUN:";

    private Ord order = null;
    private Shop found_shop = null;
    private OrderItemGoodsInfoAdapter adapter = null;
    private MyButtonListener li = null;

    private TextView title = null;
    private ImageButton return_button = null;
    private TextView waiting_state_text;
    private View tempLine1;
    private TextView waitShopName;
    private View tempLine2;
    private RelativeLayout operateOrder;
    private ImageButton cancelOrderButton;
    private ImageButton confirmGoodsButton;
    private TextView waitOrderInfo;
    private View tempLine5;
    private TextView waitOrderInfoLabel;
    private View tempLine4;
    private TextView totalPriceText;
    private TextView sendMoText;
    private ListView finishGoods;

    private void assignViews() {
        title = (TextView)findViewById(R.id.activity_name);
        title.setText(R.string.activity_customer_order_title);
        return_button.setOnClickListener(li);
        waiting_state_text = (TextView) findViewById(R.id.waiting_state_text);
        tempLine1 = findViewById(R.id.temp_line1);
        waitShopName = (TextView) findViewById(R.id.wait_shop_name);
        tempLine2 = findViewById(R.id.temp_line2);
        operateOrder = (RelativeLayout) findViewById(R.id.operate_order);
        cancelOrderButton = (ImageButton) findViewById(R.id.cancel_order_button);
        confirmGoodsButton = (ImageButton) findViewById(R.id.confirm_goods_button);
        waitOrderInfo = (TextView) findViewById(R.id.wait_order_info);
        tempLine5 = findViewById(R.id.temp_line5);
        waitOrderInfoLabel = (TextView) findViewById(R.id.wait_order_info_label);
        tempLine4 = findViewById(R.id.temp_line4);
        totalPriceText = (TextView) findViewById(R.id.total_price_text);
        sendMoText = (TextView) findViewById(R.id.send_mo_text);
        finishGoods = (ListView) findViewById(R.id.finish_goods);
    }

    private void loadOrder()
    {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.show();
        String order_id = getIntent().getStringExtra("objectId");
        BmobQuery<Ord> query = new BmobQuery<Ord>();
        query.getObject(this, order_id, new GetListener<Ord>() {

            @Override
            public void onFailure(int i, String s) {
                progress.dismiss();
            }

            @Override
            public void onSuccess(Ord ord) {
                order = ord;
                Shop shop = new Shop();
                BmobQuery<Shop> query = new BmobQuery<Shop>();
                query.addWhereEqualTo("shopname", order.getShopname());
                query.findObjects(WaitingOrderItemActivity.this, new FindListener<Shop>() {
                    @Override
                    public void onSuccess(List<Shop> list) {
                        progress.dismiss();
                        found_shop = list.get(0);
                        Log.i(TAG, "FinishedOrderItemActivity findShopNumber:" + list.size() + "");
                        initViewInfo();
                    }

                    @Override
                    public void onError(int i, String s) {
                        progress.dismiss();
                    }
                });
            }
        });
    }

    private void initViewInfo() {
        int comm_state = order.getComm_state();
        int[] time_length = null;
        Resources resources = getResources();
        String order_id_label = resources.getString(R.string.order_id_text_label);
        String receiver_name = resources.getString(R.string.receiver_customer_name_label);
        String customer_tele = resources.getString(R.string.customer_tele_label);
        String receiver_addr = resources.getString(R.string.receiver_addr_text_label);
        String pay_method = resources.getString(R.string.pay_method_label);
        String receive_time = resources.getString(R.string.order_time_label);
        String final_order_info = order_id_label + order.getObjectId() + "\n"
                + receiver_name + order.getReceiver() + "\n"
                + customer_tele + order.getUsertel() + "\n"
                + receiver_addr + order.getUseradd() + "\n"
                + pay_method + order.getPayway() + "\n"
                + receive_time + order.getOrdtime();
        String order_state = order.getJystate();
        switch(order_state)
        {
            case "0":
                waiting_state_text.setText(R.string.activity_waiting_order_item_new_order);
                break;
            case "11":
                waiting_state_text.setText(R.string.activity_waiting_order_item_no_accept);
                break;
            case "12":
                /*String part1 = resources.getString(R.string.activity_waiting_order_item_sending_part_one);
                String part2 = resources.getString(R.string.activity_waiting_order_item_sending_part_two);
                String part3 = resources.getString(R.string.activity_waiting_order_item_sending_part_three);
                String time = "";
                time_length = getTimeLength(order.getOrdtime());
                if(time_length[0] != 0 )
                    time += time_length[0] + part3;
                time += time_length[1];*/
                //String info = getResources().getString(R.string.activity_waiting_order_item_accepted);
                waiting_state_text.setText(R.string.activity_waiting_order_item_accepted);
                break;
        }
        waitShopName.setText(order.getShopname());
        adapter = new OrderItemGoodsInfoAdapter(this,order);
        finishGoods.setAdapter(adapter);
        sendMoText.setText("" + found_shop.getDistributeprice());
        totalPriceText.setText("" + order.getTotalprice());

        if(!order_state.equals("0"))
            cancelOrderButton.setImageResource(R.drawable.cancel_order_gray);
        li = new MyButtonListener();
        cancelOrderButton.setOnClickListener(li);
        confirmGoodsButton.setOnClickListener(li);
        return_button = (ImageButton)findViewById(R.id.return_button);
    }

    /*����һ��������Ԫ�ص�int����
    *index 0:hour
    *index 1:minute
    * */
    private int[] getTimeLength(BmobDate _order_date)
    {
        int result[] = new int[2];
        int seconds = 0,minute = 0,hour = 0;
        long now_mseconds = 0,order_mseconds = 0;
        long time_difference = 0;
        Date order_date = null;
        Date now_date = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            order_date = format.parse(_order_date.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        now_mseconds = now_date.getTime();
        if(order_date != null)
        {
            order_mseconds = order_date.getTime();
            time_difference = now_mseconds - order_mseconds;
            if(time_difference > 0)
            {
                seconds = (int)(time_difference / 1000);
                minute = seconds / 60;
                hour = minute / 60;
            }
        }
        result[0] = hour;
        result[1] = minute;
        return result;
    }

    //ȡ��
    private void cancelOrder()
    {
        String order_state = order.getJystate();
        if(order_state.equals("0")) {
            Ord new_order = new Ord();
            new_order.setJystate("23");
            new_order.update(this, order.getObjectId(), new UpdateListener() {
                @Override
                public void onSuccess() {
                    toast(R.string.activity_waiting_order_item_cancel_order_success);
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }
        else
        {
            String message = getResources().getString(R.string.activity_waiting_order_item_cannot_cancel);
            new AlertDialog.Builder(this).setTitle(R.string.activity_waiting_order_item_info).setMessage(message)
                    .setPositiveButton(R.string.activity_waiting_order_item_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }

    }

    //ȷ���ջ�
    private void confirmGoods()
    {
        Ord new_order = new Ord();
        new_order.setJystate("21");
        new_order.update(this, order.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_waiting_order_item);
        assignViews();
        loadOrder();
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

    private void toast(int id)
    {
        Toast.makeText(this,id,Toast.LENGTH_LONG);
    }

    class MyButtonListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch(id)
            {
                case R.id.cancel_order_button:
                    cancelOrder();
                    break;
                case R.id.confirm_goods_button:
                    confirmGoods();
                    break;
                case R.id.return_button:
                    finish();
                    break;
            }
        }
    }
}
