package com.eagle.easyshopping;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmob.BmobProFile;

public class DetailActivity extends Activity {
    String TAG = "DetailActivity";
    TextView goodname;
    TextView price;
    TextView description;
    ImageView picture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detail);
        Intent intent1 = getIntent();
        String sgoodname = intent1.getStringExtra("goodname");
        String sprice = intent1.getStringExtra("price");
        String spicture = intent1.getStringExtra("picture");
        String sdescription = intent1.getStringExtra("description");
        goodname=(TextView)findViewById(R.id.tvDgoodname);
        price=(TextView)findViewById(R.id.tvDprice);
        description=(TextView)findViewById(R.id.dtvdescription);
        picture=(ImageView)findViewById(R.id.divPicture);
        //加载图片
        loadShopPicImpl(picture, spicture);
        goodname.setText("名称："+sgoodname);
        description.setText("口碑："+sdescription);
        price.setText("单价：￥"+sprice+".0");
        findViewById(R.id.bbb).setOnClickListener(new SureClickListener());


    }


    //加载店家图片
    private void loadShopPicImpl(ImageView shop_pic, String pic_name) {
        Log.d(TAG, "in function loadShopPicImpl:loading");
        final ImageView view = shop_pic;
        Bitmap shop_bg = null;
        File file = new File(pic_name);
        if (file.exists()) {
            Log.i(TAG, "loading pic local?");
            shop_bg = BitmapFactory.decodeFile(pic_name);
            shop_pic.setImageBitmap(shop_bg);
        } else {
            BmobProFile pro_file = BmobProFile.getInstance(DetailActivity.this);
            pro_file.download(pic_name, new com.bmob.btp.callback.DownloadListener() {
                @Override
                public void onSuccess(String s) {
                    setImageBitmap(view, s);
                }

                @Override
                public void onProgress(String s, int i) {

                }

                @Override
                public void onError(int i, String s) {
                    Log.e(TAG, "loading pic wrong:" + s);
                }
            });
        }
    }

    private void setImageBitmap(ImageView view, String file_path) {
        Log.i(TAG, "loading success: " + file_path);
        File file = new File(file_path);
        Bitmap bit = null;
        if (view.isShown()) {
            view.setImageResource(0);
            bit = BitmapFactory.decodeFile(file_path);
            if (file.exists())
                Log.e(TAG, "file exists!");
            if (bit == null)
                Log.e(TAG, "WTF NullPointer?");
            else
                Log.i(TAG, "" + bit.getWidth());
            view.setImageBitmap(bit);
            view.invalidate();
            Log.i(TAG, "set to imageView");
        }
        //file.delete();
    }


    private class SureClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bbb) {
               DetailActivity.this.finish();
            }
        }
    }

    private class BackListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            /*if (v.getId() == R.id.dbtn1) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);

            }*/

        }
    }
}
