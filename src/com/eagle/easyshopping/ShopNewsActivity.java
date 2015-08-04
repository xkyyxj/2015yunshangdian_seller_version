package com.eagle.easyshopping;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/*
*
* */

public class ShopNewsActivity extends Activity {

    private ImageButton return_button;
    private TextView title = null;
    private ListView listView;

    private MyListAdapter adapter = null;
    private MyButtonListener li = null;

    private void assignViews() {
        return_button = (ImageButton) findViewById(R.id.return_button);
        title = (TextView) findViewById(R.id.activity_name);
        title.setText(R.string.customer_shop_news_title);
        listView = (ListView) findViewById(R.id.list_view);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_shop_news);
        assignViews();
        loadingInfo();
    }

    private void loadingInfo()
    {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        BmobQuery<ShopNews> query = new BmobQuery<>();
        query.findObjects(this, new FindListener<ShopNews>() {
            @Override
            public void onSuccess(List<ShopNews> list) {
                dialog.dismiss();
                toast(R.string.activity_shop_news_loading_success, null);
            }

            @Override
            public void onError(int i, String s) {
                dialog.dismiss();
                toast(R.string.activity_shop_news_loading_fail, null);
            }
        });
    }

    private void initView(List<ShopNews> _list)
    {
        li = new MyButtonListener();
        return_button.setOnClickListener(li);
        adapter = new MyListAdapter(this,_list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

    private void toast(int id,String text)
    {
        String content = getResources().getString(id);
        if(text != null)
            content += text;
        Toast.makeText(this,content,Toast.LENGTH_LONG).show();
    }

    class MyListAdapter extends BaseAdapter
    {
        private List<ShopNews> list = null;
        private LayoutInflater inflater = null;

        public MyListAdapter(Context _context,List<ShopNews> _list)
        {
            list = _list;
            inflater = LayoutInflater.from(_context);
        }

        @Override
        public int getCount() {
            return list == null?0:list.size();
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
                convertView = inflater.inflate(R.layout.shop_news_list_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            else
                holder = (ViewHolder)convertView.getTag();
            holder.newscontent.setText(list.get(position).getShop().getShopname());
            holder.newscontent.setText(list.get(position).getText());
            return convertView;
        }

        public class ViewHolder {
            public final TextView newsshopname;
            public final TextView newscontent;
            public final View root;

            public ViewHolder(View root) {
                newsshopname = (TextView) root.findViewById(R.id.news_shop_name);
                newscontent = (TextView) root.findViewById(R.id.news_content);
                this.root = root;
            }
        }
    }

    class MyButtonListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            finish();
        }
    }
}
