package com.eagle.easyshopping;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.datatype.BmobGeoPoint;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.DownloadListener;


public class HomeAdapter extends BaseAdapter {

	private List<Shop> list = null;
	DecimalFormat df=new DecimalFormat("#0.00");
	private Context con;
	private LayoutInflater inflater = null;
	private BmobGeoPoint usergeo=null;

	public HomeAdapter(Context _context, List<Shop> fromBmob,BmobGeoPoint cgeo) {
		con = _context;
		list = fromBmob;
		usergeo=cgeo;
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
			convertView = inflater.inflate(R.layout.homeshop_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		Log.e("RUN:","" + position);
		list.get(position).getShopname();
		holder.shopname.setText(list.get(position).getShopname());
		holder.time.setText(list.get(position).getStarttime() + "-"
				+ list.get(position).getEndtime());
		holder.distdist.setText(""+ df.format(list.get(position).getSlonla().distanceInKilometersTo(usergeo))+"km");
		holder.priceforsent.setText(""
				+ list.get(position).getSendprice());
		loadShopPic(holder.store_pc, list.get(position).getSpicture(), con);
		if (list.get(position).getOperatesta() != null) {
			if (list.get(position).getOperatesta()) {
				holder.state.setText(convertView.getResources().getString(R.string.state));
			} else
				holder.state.setText(convertView.getResources().getString(R.string.state2));
		}

		return convertView;
	}

	public class ViewHolder {
		public final TextView shopname;
		public final TextView dist;
		public final TextView distdist;
		public final TextView opentime;
		public final TextView time;
		public final TextView forsent;
		public final TextView priceforsent;
		public final TextView state;

		public final ImageView store_pc;
		public final View root;

		public ViewHolder(View root) {
			shopname = (TextView) root.findViewById(R.id.storename);
			dist = (TextView) root.findViewById(R.id.dist);
			distdist = (TextView) root.findViewById(R.id.distdist);
			opentime = (TextView) root.findViewById(R.id.opentime);
			state = (TextView) root.findViewById(R.id.state);

			time = (TextView) root.findViewById(R.id.time);
			forsent = (TextView) root.findViewById(R.id.forsent);
			priceforsent = (TextView) root.findViewById(R.id.priceforsent);
			store_pc = (ImageView) root.findViewById(R.id.store_pc);
			this.root = root;
		}
	}


	private void loadShopPic(ImageView shop_pic, String pic_name,
			Context context) {

		final ImageView view = shop_pic;
		Bitmap shop_bg = null;
		File file = null;
		if(pic_name != null)
			file = new File(pic_name);
		if (file != null && file.exists()) {
			shop_bg = BitmapFactory.decodeFile(pic_name);
			shop_pic.setImageBitmap(shop_bg);
		} else {
			if(pic_name != null)
			{
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
		
	}

}
