package com.eagle.easyshopping;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/*
* ������Activity��ʱ��Ҫ����һ��String�ַ���
* ���ַ�����һ���Ѵ��ڵ�Shop���ʵ����ObjectID
* ͨ��intent.putExtra(String str,Object value)����
* ͨ�����Ϸ�ʽ����comment������һ��Shopʵ�����С�
* */
public class AddCommentActivity extends Activity {

    //UserӦ����һ������BaseActivity����Application�Ķ���
    public static final String TAG = "RUN:";
    private User user = new User();

    private boolean choose_state = false;
    private boolean comm_state = false;
    private boolean had_comm = false;

    private String comm_content,shop_object_id,order_object_id;

    private TextView title = null;
    private ImageButton return_button = null;
    private RelativeLayout tempGoodComm;
    private TextView tempGoodCommLabel;
    private ImageButton goodCommButton;
    private RelativeLayout tempBadComm;
    private TextView tempBadCommLabel;
    private ImageButton badCommButton;
    private TextView tempCommContentLabel;
    private EditText commContent;
    private Button commit;

    private Shop shop = null;
    private Ord order = null;
    private MyCommStateButtonListener li = null;

    private void assignViews() {
        title = (TextView)findViewById(R.id.activity_name);
        return_button = (ImageButton)findViewById(R.id.return_button);
        tempGoodComm = (RelativeLayout) findViewById(R.id.temp_good_comm);
        tempGoodCommLabel = (TextView) findViewById(R.id.temp_good_comm_label);
        goodCommButton = (ImageButton) findViewById(R.id.good_comm_button);
        tempBadComm = (RelativeLayout) findViewById(R.id.temp_bad_comm);
        tempBadCommLabel = (TextView) findViewById(R.id.temp_bad_comm_label);
        badCommButton = (ImageButton) findViewById(R.id.bad_comm_button);
        tempCommContentLabel = (TextView) findViewById(R.id.temp_comm_content_label);
        commContent = (EditText) findViewById(R.id.comm_content);
        commit = (Button) findViewById(R.id.commit);
    }

    private void testFunction()
    {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("name","name");
        query.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                Log.v(TAG,"fingdingUser success!");
                if(list != null && list.size() > 0) {
                    user = list.get(0);
                    Log.v(TAG, "AddCommentActivity:" + user.getObjectId());
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.v(TAG,"Finding user failed");

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //testing code
        //testFunction();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_add_comment);
        assignViews();

        MyApplication app = (MyApplication)this.getApplication();
        user = app.getUser();

        shop = new Shop();
        shop_object_id = getIntent().getStringExtra("objectId");
        shop.setObjectId(shop_object_id);
        Log.e(TAG, "objectId is" + shop_object_id);

        order = new Ord();
        order_object_id = getIntent().getStringExtra("ordObjectId");
        order.setObjectId(order_object_id);

        title.setText(R.string.activity_add_comment_title);

        li = new MyCommStateButtonListener();
        goodCommButton.setOnClickListener(li);
        badCommButton.setOnClickListener(li);
        return_button.setOnClickListener(li);
        commit.setOnClickListener(li);
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

    private void uploadComment(String _comm_content)
    {
        Log.e(TAG,"running in uploadcomment Function!");
        final String comm_content =_comm_content;
        Comment comment = new Comment();
        comment.setContent(_comm_content);
        comment.setState(comm_state);
        comment.setUser(user);
        comment.setDesti(shop);
        comment.save(AddCommentActivity.this, new SaveListener() {

            @Override
            public void onSuccess() {
                had_comm = true;
                order.setCom(comm_content);
                if (comm_state)
                    order.setComm_state(1);
                else
                    order.setComm_state(2);
                Log.v(TAG, "uplaod_order_success!");
                updateOrder();
            }

            @Override
            public void onFailure(int i, String s) {
                toast(R.string.activity_add_comment_failed,null);
            }
        });
    }

    private void updateOrder()
    {
        order.setJystate("22");
        order.update(this, order_object_id, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG,"error1");
                toast(R.string.activity_add_comment_successful, null);
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(TAG,"error2" + s);
                toast(R.string.activity_add_comment_failed,null);
            }
        });
    }

    private void toast(int id,String _text)
    {
        String text = getResources().getString(id);
        if(_text != null)
            text += _text;
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }

    class MyCommStateButtonListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.good_comm_button)
            {
                choose_state = true;
                comm_state = true;
                ((ImageButton)v).setImageResource(R.drawable.orangenice);
            }
            else if(v.getId() == R.id.bad_comm_button)
            {
                choose_state = true;
                comm_state = false;
                ((ImageButton)v).setImageResource(R.drawable.blacklow);
            }
            else if(v.getId() == R.id.commit)
            {
                String _comm_content = commContent.getText().toString();
                if(choose_state && _comm_content != null && !had_comm)
                    uploadComment(_comm_content);
                else if(!choose_state)
                    toast(R.string.activity_add_comment_no_content_err,null);
                else if(_comm_content == null)
                    toast(R.string.activity_add_comment_no_comm_state_err,null);
                else if(had_comm)
                    toast(R.string.activity_add_comment_no_repeat,null);

            }
            else if(v.getId() == R.id.return_button)
                finish();
        }
    }
}
