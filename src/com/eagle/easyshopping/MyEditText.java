package com.eagle.easyshopping;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/7/9 0009.
 */
public class MyEditText extends LinearLayout {

    public EditText getmEditText() {
        return mEditText;
    }

    public void setmEditText(EditText mEditText) {
        this.mEditText = mEditText;
    }

    private EditText mEditText;
    private Button bAdd;
    private Button bReduce;




    private int num=0;

    private FoodBean bean;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    private int index = -1;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    //这里的构造一定是两个参数。
    public MyEditText(final Context ctxt, AttributeSet attrs) {
        super(ctxt,attrs);
    }


    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.myedittext, this);
        init_widget();
        addListener();



    }

    public void setFoodBean(FoodBean bean){
        this.bean=bean;
    }

    public void init_widget(){


        mEditText = (EditText)findViewById(R.id.et01);
        mEditText.setFocusable(true);
        bAdd = (Button)findViewById(R.id.bt01);
        bReduce = (Button)findViewById(R.id.bt02);

        mEditText.setText(num+"");
    }

    public void addListener(){
        bAdd.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                num = Integer.valueOf(mEditText.getText().toString());
                num++;
                bean.setNum(num);

                mEditText.setText(Integer.toString(num));
            }
        });

        bReduce.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int num = Integer.valueOf(mEditText.getText().toString());
                num--;
                bean.setNum(num);
                if(num<0){num=0;}
                mEditText.setText(Integer.toString(num));
            }
        });
    }


}
