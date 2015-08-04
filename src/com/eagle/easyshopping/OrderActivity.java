package com.eagle.easyshopping;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class OrderActivity extends Activity {

    private RadioGroup payway;
    private RadioButton online;
    private RadioButton onhand;

    ListView lvOrderFoods;
    FoodAdapter mAdapter;
    List<FoodBean> nmlvOrderFoods;
    TextView mcount;
    TextView reciever;
    TextView tel;
    TextView add;
    String temp=null;
    ArrayList<String> goodsnames;
    ArrayList<String> price;
    ArrayList<String> nums;
    String cshopname;
    String cusername;
    List<Add> object2;
    Double totalprice;
    Double psend;
    Double lastcount;

    TextView otvshopname;
    TextView otvpsend;

    //int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order);

        Bmob.initialize(this, "474311be9f312229e370c28ebcac1d75");

        findViewById(R.id.ibet).setOnClickListener(new EditListener());
        findViewById(R.id.bImput).setOnClickListener(new UpListener());
        findViewById(R.id.ibBack).setOnClickListener(new BackListener());

        reciever = (TextView)findViewById(R.id.reciever);
        tel = (TextView)findViewById(R.id.tel);
        add = (TextView)findViewById(R.id.add);

        otvshopname = (TextView)findViewById(R.id.otvshopname);
        otvpsend = (TextView)findViewById(R.id.otvpsend);

        object2 = new LinkedList<Add>();

        Intent intent1 = getIntent();
        cshopname=intent1.getStringExtra("cshopname");
        cusername=intent1.getStringExtra("cusername");
        psend = intent1.getDoubleExtra("psend",0.0);

        otvshopname.setText(cshopname);
        otvpsend.setText("配送费：￥"+psend);

        BmobQuery<Add> query = new BmobQuery<Add>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("username", cusername);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(OrderActivity.this, new FindListener<Add>() {
            @Override
            public void onSuccess(List<Add> object) {

                object2=object;
                // TODO Auto-generated method stub
                //toast("查询成功：共"+object.size()+"条数据。");
                Toast.makeText(OrderActivity.this, "查询成功：共"+object2.size()+"条数据。", Toast.LENGTH_SHORT).show();
                for (Add gameScore : object) {
                    //获得playerName的信息
                    //gameScore.getShopname();
                    //获得数据的objectId信息
                    gameScore.getObjectId();
                    //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                    gameScore.getCreatedAt();
                }

                for(int i=0;i<object2.size();i++){
                    reciever.setText(object2.get(i).getReceiver());
                    tel.setText(object2.get(i).getUsertel());
                    add.setText(object2.get(i).getUseradd());
                }





            }
            @Override
            public void onError(int code, String msg) {
                Toast.makeText(OrderActivity.this,"查询失败" +msg, Toast.LENGTH_SHORT).show();
            }
        });



        payway=(RadioGroup) super.findViewById(R.id.payway);
        online=(RadioButton) super.findViewById(R.id.online);
        onhand=(RadioButton) super.findViewById(R.id.onhand);
        payway.setOnCheckedChangeListener(new OnCheckedChangeListenerImp());

        initData();
        initView();
    }

    class BackListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ibBack) {
                /*Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(intent);*/
                OrderActivity.this.finish();

            }

        }

    }


    private void initView() {
        lvOrderFoods = (ListView) findViewById(R.id.lvOrderFoods);
        mAdapter = new FoodAdapter();
        lvOrderFoods.setAdapter(mAdapter);

    }

    private void initData() {
        String[] names = getResources().getStringArray(R.array.foods);
        /*String[] singlep = getResources().getStringArray(R.array.singlep);
        int[] ppp = new int[singlep.length];
        for(int i=0;i<singlep.length;i++){
            ppp[i]=Integer.parseInt(singlep[i]);
        }*/


        mcount=(TextView)findViewById(R.id.tvAcount6);

        Intent intent8 = getIntent();
        nmlvOrderFoods=new LinkedList<FoodBean>();
        goodsnames= new ArrayList<String>();
        price= new ArrayList<String>();
        nums= new ArrayList<String>();


        //int singlep;

        String ssize = intent8.getStringExtra("size");
        int size = Integer.parseInt(ssize);
        totalprice = intent8.getDoubleExtra("totalprice",0.0);
        lastcount = totalprice+psend;
        mcount.setText("总计：￥"+lastcount);


        for (int i = 0; i < size; i++) {
            FoodBean bean = (FoodBean)intent8.getSerializableExtra("foodbean"+i);

            //singlep=Integer.parseInt(bean.getPrice());

            //count = count + singlep*bean.getNum();

            if(bean.getNum()>0){
                nmlvOrderFoods.add(bean);
                goodsnames.add(bean.getName());
                price.add(bean.getPrice());
                nums.add(""+bean.getNum());
            }
        }

        //mcount.setText("总计："+count+"元");

    }


    class EditListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ibet) {
                Intent intent = new Intent(OrderActivity.this, EditActivity.class);
                intent.putExtra("username",cusername);
                intent.putExtra("shopname",cshopname);
                intent.putExtra("size",""+nmlvOrderFoods.size());
                intent.putExtra("totalprice",totalprice);
                intent.putExtra("psend",psend);
                for(int i=0;i<nmlvOrderFoods.size();i++){
                    intent.putExtra("foodbean"+i,nmlvOrderFoods.get(i));
                    //Toast.makeText(MainActivity.this,"被短按"+mlvFoods.get(i).getNum(), Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);

            }

        }

    }

    class UpListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bImput) {
                //for(int i=0;i<nmlvOrderFoods.size();i++){
                    Ord ord = new Ord();
                    ord.setUsername(cusername);
                    ord.setShopname(cshopname);
                    ord.setReceiver(reciever.getText().toString());
                    //Toast.makeText(AccountActivity.this, nmlvFoods.get(i).getName(), Toast.LENGTH_SHORT).show();
                    ord.setUseradd(add.getText().toString());
                    ord.setPayway(temp);
                    ord.setGoodsname(goodsnames);
                    ord.setPrice(price);
                    ord.setNums(nums);
                    ord.setJystate("" + 0);
                    ord.setTotalprice("" + lastcount);
                    ord.setUsertel(tel.getText().toString());
                    //Toast.makeText(AccountActivity.this, nmlvFoods.get(i).getNum()+"", Toast.LENGTH_SHORT).show();
                    ord.save(OrderActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {

                            Toast.makeText(OrderActivity.this, "下单成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int code, String msg) {

                            Toast.makeText(OrderActivity.this,"下单失败，请重试" +msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                //}

            }

        }

    }


    private class OnCheckedChangeListenerImp implements RadioGroup.OnCheckedChangeListener {


        public void onCheckedChanged(RadioGroup group, int checkedId) {

            if(OrderActivity.this.online.getId()==checkedId){
                temp="在线支付";
            }
            else if(OrderActivity.this.onhand.getId()==checkedId){
                temp="货到付款";
            }
            //MainActivity.this.txt.setText("您的性别是"+temp);
        }
    }


    class FoodAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return nmlvOrderFoods.size();
        }

        @Override
        public FoodBean getItem(int position) {
            return nmlvOrderFoods.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View layout = View.inflate(OrderActivity.this, R.layout.item_orderfood, null);


            TextView tvGoodName = (TextView) layout.findViewById(R.id.otv1);
            TextView num2 = (TextView) layout.findViewById(R.id.otv2);
            TextView price = (TextView) layout.findViewById(R.id.otv3);


            FoodBean bean = nmlvOrderFoods.get(position);

            //Toast.makeText(SearchActivity.this, bean.getShopName(), Toast.LENGTH_SHORT).show();


            tvGoodName.setText(bean.getName());
            num2.setText(""+bean.getNum());
            price.setText(bean.getPrice());


            return layout;
        }
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
}
