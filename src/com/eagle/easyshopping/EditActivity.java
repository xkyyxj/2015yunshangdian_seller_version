package com.eagle.easyshopping;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


public class EditActivity extends Activity {
    String TAG = "EditActivity";
    List<FoodBean> saves;
    Double stotal;
    int size;
    int totalprice;
    String eusername;
    String cshopname;
    TextView reciever;
    TextView tel;
    TextView adds;
    List<Add> object2;
    Double psend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit);

        saves=new LinkedList<FoodBean>();
        Intent intent1=getIntent();
        eusername=intent1.getStringExtra("username");
        cshopname=intent1.getStringExtra("shopname");
        psend =intent1.getDoubleExtra("psend",0.0);
        String ssize = intent1.getStringExtra("size");
        size = Integer.parseInt(ssize);
        stotal = intent1.getDoubleExtra("totalprice",0.0);
        //totalprice = Integer.parseInt(stotal);
        for (int i = 0; i < size; i++) {
            FoodBean bean = (FoodBean)intent1.getSerializableExtra("foodbean"+i);
            saves.add(bean);

        }

        findViewById(R.id.ebBack).setOnClickListener(new BackListener());
        findViewById(R.id.ebSave).setOnClickListener(new SaveListener());

        reciever=(TextView) findViewById(R.id.eetReciever);
        adds=(TextView) findViewById(R.id.eetAdd);
        tel=(TextView) findViewById(R.id.eetTel);

        object2 = new LinkedList<Add>();

        BmobQuery<Add> query = new BmobQuery<Add>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("username", eusername);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(EditActivity.this, new FindListener<Add>() {
            @Override
            public void onSuccess(List<Add> object) {

                object2 = object;
                // TODO Auto-generated method stub
                //toast("查询成功：共"+object.size()+"条数据。");
                Toast.makeText(EditActivity.this, "查询成功：共" + object2.size() + "条数据。", Toast.LENGTH_SHORT).show();
                for (Add gameScore : object) {
                    //获得playerName的信息
                    //gameScore.getShopname();
                    //获得数据的objectId信息
                    gameScore.getObjectId();
                    //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                    gameScore.getCreatedAt();
                }



            }

            @Override
            public void onError(int code, String msg) {
                Toast.makeText(EditActivity.this, "查询失败" + msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    class BackListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ebBack) {
                Intent intent = new Intent(EditActivity.this, OrderActivity.class);
                intent.putExtra("cusername",eusername);
                intent.putExtra("cshopname",cshopname);
                intent.putExtra("size",""+size);
                intent.putExtra("totalprice",stotal);
                intent.putExtra("psend",psend);
                for(int i=0;i<size;i++){
                    intent.putExtra("foodbean"+i,saves.get(i));
                    //Toast.makeText(MainActivity.this,"被短按"+mlvFoods.get(i).getNum(), Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);


            }

        }

    }



    class SaveListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ebSave) {
                for (int i=0;i<object2.size();i++) {
                    //Log.v(TAG,"Save");
                    Add add = new Add();
                    add.setReceiver(reciever.getText().toString());
                    add.setUsertel(tel.getText().toString());
                    add.setUseradd(adds.getText().toString());
                    add.update(EditActivity.this, object2.get(i).getObjectId(), new UpdateListener() {

                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub
                            //toast("更新成功：");
                            Toast.makeText(EditActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            // TODO Auto-generated method stub
                            //toast("更新失败："+msg);
                            Toast.makeText(EditActivity.this, "更新失败，请重试" + msg, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
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
