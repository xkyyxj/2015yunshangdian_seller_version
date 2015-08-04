package com.eagle.easyshopping;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 *Showing information of an order's goods.
 * Created by Albert on 2015/7/16.
 */
public class OrderItemGoodsInfoAdapter extends BaseAdapter {

    private Ord order = null;

    private ArrayList<String> goods_name;
    private ArrayList<String> goods_number,goods_price;

    private LayoutInflater inflater = null;

    public OrderItemGoodsInfoAdapter(Context _context,Ord _order)
    {
        order = _order;
        goods_number = order.getNums();
        goods_name = order.getGoodsname();
        goods_price = order.getPrice();
        inflater = LayoutInflater.from(_context);
    }

    @Override
    public int getCount() {
        return goods_name == null?0:goods_name.size();
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
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.goods_list_item_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder)convertView.getTag();
        holder.nametext.setText(goods_name.get(position));
        if(goods_number != null)
        	holder.numbertext.setText("" + goods_number.get(position));
        holder.pricetext.setText("" + goods_price.get(position));
        return convertView;
    }

    public class ViewHolder {
        public final TextView nametext;
        public final TextView numbertext;
        public final TextView pricetext;
        public final View root;

        public ViewHolder(View root) {
            nametext = (TextView) root.findViewById(R.id.name_text);
            numbertext = (TextView) root.findViewById(R.id.number_text);
            pricetext = (TextView) root.findViewById(R.id.price_text);
            this.root = root;
        }
    }
}
