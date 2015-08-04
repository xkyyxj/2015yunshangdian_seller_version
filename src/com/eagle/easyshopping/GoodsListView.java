
package com.eagle.easyshopping;
import android.app.Activity;  
import android.content.Context;  
import android.os.Bundle;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.view.Window;
import android.widget.BaseAdapter;  
import android.widget.ImageView;  
import android.widget.ListView;  
import android.widget.TextView;  
  
public class GoodsListView extends Activity {  
    ListView listView;  
    String[] gname={"范狗蛋1","范狗蛋2","范狗蛋3","范狗蛋4"};  
    String[] price={"5.0","6.0","7.0","8.0"};  
    String[] storename={"v587230","v587230","v587230","v587230", };
    int[] imageid={R.drawable.sausages,R.drawable.sausages,R.drawable.sausages,R.drawable.sausages};  
    
      
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.myctn_layout);  
        listView=(ListView)this.findViewById(R.id.ListViewOfCtn);  
        listView.setAdapter(new ListViewAdapter(gname,price,storename,imageid));  
    }  
  
    public class ListViewAdapter extends BaseAdapter {  
        View[] ctnView;  
  
        public ListViewAdapter(String[] goodsName, String[] gPrice,  String[] storename,
                int[] itemImageRes) {  
            ctnView = new View[goodsName.length];  
  
            for (int i = 0; i < ctnView.length; i++) {  
                ctnView[i] = makeItemView(goodsName[i], gPrice[i],  storename[i],
                        itemImageRes[i]);  
            }  
        }  
  
        public int getCount() {  
            return ctnView.length;  
        }  
  
        public View getItem(int position) {  
            return ctnView[position];  
        }  
  
        public long getItemId(int position) {  
            return position;  
        }  
          
        private View makeItemView(String name, String price,String sname, int resId) {  
            LayoutInflater inflater = (LayoutInflater) GoodsListView.this  
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
  
            // 使用View的对象itemView与R.layout.item关联  
            View itemView = inflater.inflate(R.layout.myctn_item, null);  
  
           // 通过findViewById()方法实例R.layout.item内各组件  
            TextView gname = (TextView) itemView.findViewById(R.id.gname);  
            gname.setText(name);  
            TextView gprice = (TextView) itemView.findViewById(R.id.price);  
            gprice.setText(price);  
            TextView storename = (TextView) itemView.findViewById(R.id.storename);  
            storename.setText(sname);  
            ImageView image = (ImageView) itemView.findViewById(R.id.itemImage);  
            image.setImageResource(resId);  
       
            return itemView;  
        }  
  
        public View getView(int position, View convertView, ViewGroup parent) {  
            if (convertView == null)  
                return ctnView[position];  
            return convertView;  
        }  
    }  
  
}  