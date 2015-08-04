package com.eagle.easyshopping;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

public class FinishedOrderItemActivity extends Activity {

	public static final String TAG = "EasyShopping:";

	private Ord order = null;
	private Shop found_shop = null;
	private OrderItemGoodsInfoAdapter adapter = null;

	private LayoutInflater inflater = null;
	private Button comm_button = null;
	private RelativeLayout comm_content = null;
	private ImageView comm_state_view = null;
	private TextView comm_text = null;

	private TextView title = null;
	private ImageButton return_button = null;
	private TextView finishShopName;
	private ListView finishGoods;
	private TextView sendMoText;
	private TextView totalPriceText;
	private TextView otherInformation;
	private TextView finishOrderInfo;
	private FrameLayout commContent;

	private MyButtonListener li = null;

	private void assignViews() {
		title = (TextView) findViewById(R.id.activity_name);
		return_button = (ImageButton) findViewById(R.id.return_button);
		finishShopName = (TextView) findViewById(R.id.finish_shop_name);
		finishGoods = (ListView) findViewById(R.id.finish_goods);
		sendMoText = (TextView) findViewById(R.id.send_mo_text);
		totalPriceText = (TextView) findViewById(R.id.total_price_text);
		otherInformation = (TextView) findViewById(R.id.other_information);
		finishOrderInfo = (TextView) findViewById(R.id.finish_order_info);
		commContent = (FrameLayout) findViewById(R.id.comm_content);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_finished_order_item);
		inflater = getLayoutInflater();
		assignViews();
		loadOrder();
		// testing============================================================
		// order = Creating.creatingOrder(1,"��������");
		// found_shop = Creating.createShop("��������");
		// UploadObject.uploadOrd(this,order);
		// UploadObject.uploadShop(this,found_shop);
		// initViewInfo();
		// testing==============================================================
	}

	private void loadOrder() {
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
				query.findObjects(FinishedOrderItemActivity.this,
						new FindListener<Shop>() {
							@Override
							public void onSuccess(List<Shop> list) {
								progress.dismiss();
								found_shop = list.get(0);
								Log.i(TAG,
										"FinishedOrderItemActivity findShopNumber:"
												+ list.size() + "");
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
		// init Activity name
		title.setText(R.string.activity_customer_order_title);

		// init return button
		li = new MyButtonListener();
		return_button.setOnClickListener(li);
		int comm_state = 0;
		Integer integer = order.getComm_state();
		if (integer != null)
			comm_state = integer;
		Resources resources = getResources();
		String order_id_label = resources
				.getString(R.string.order_id_text_label);
		String receiver_name = resources
				.getString(R.string.receiver_customer_name_label);
		String customer_tele = resources
				.getString(R.string.customer_tele_label);
		String receiver_addr = resources
				.getString(R.string.receiver_addr_text_label);
		String pay_method = resources.getString(R.string.pay_method_label);
		String receive_time = resources.getString(R.string.order_time_label);
		String final_order_info = order_id_label + order.getObjectId() + "\n"
				+ receiver_name + order.getReceiver() + "\n" + customer_tele
				+ order.getUsertel() + "\n" + receiver_addr
				+ order.getUseradd() + "\n" + pay_method + order.getPayway()
				+ "\n" + receive_time + order.getOrdtime();
		switch (comm_state) {
		case 0:
			comm_content = (RelativeLayout) inflater.inflate(
					R.layout.finished_order_no_comm_info_layout, null);
			comm_button = (Button) comm_content
					.findViewById(R.id.comment_button);
			comm_button.setOnClickListener(li);
			commContent.addView(comm_content, 0);
			break;
		case 1:
			comm_content = (RelativeLayout) inflater.inflate(
					R.layout.finished_order_comm_info_layout, null);
			comm_state_view = (ImageView) comm_content
					.findViewById(R.id.finished_order_comm_state);
			comm_text = (TextView) comm_content
					.findViewById(R.id.finished_order_comm_content);
			// �������
			comm_state_view.setImageResource(R.drawable.good_comm);// �޸���Դ�ļ�
			comm_text.setText(order.getCom());
			commContent.addView(comm_content, 0);
			break;
		case 2:
			comm_content = (RelativeLayout) inflater.inflate(
					R.layout.finished_order_comm_info_layout, null);
			comm_state_view = (ImageView) comm_content
					.findViewById(R.id.finished_order_comm_state);
			comm_text = (TextView) comm_content
					.findViewById(R.id.finished_order_comm_content);
			// �������
			comm_state_view.setImageResource(R.drawable.bad_comm);// �޸���Դ�ļ�
			comm_text.setText(order.getCom());
			commContent.addView(comm_content, 0);
			break;
		}
		finishShopName.setText(order.getShopname());
		adapter = new OrderItemGoodsInfoAdapter(this, order);
		finishGoods.setAdapter(adapter);
		sendMoText.setText("" + found_shop.getDistributeprice());
		totalPriceText.setText("" + order.getTotalprice());
		otherInformation.setText(order.getCom());
		finishOrderInfo.setText(final_order_info);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	class MyButtonListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.return_button) {
				finish();
			} else {
				Intent intent = new Intent(FinishedOrderItemActivity.this,
						AddCommentActivity.class);
				intent.putExtra("ordObjectId", order.getObjectId());
				intent.putExtra("objectId", found_shop.getObjectId());
				startActivity(intent);
			}
		}
	}

}
