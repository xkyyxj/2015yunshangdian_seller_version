package com.eagle.easyshopping;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Albert on 2015/7/17.
 */
public class OrderInfoFragment extends Fragment {

    private int frag_type = -1;

    private LayoutInflater inflater = null;

    private View view = null;

    private ListView list = null;

    private OrderInfoListAdapter adapter = null;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_info_layout,null);
        list = (ListView)view.findViewById(R.id.order_info);
        return view;
    }

    public void notifyDataChange(List<Ord> _order_list,Map<String, Shop> _shop_map)
    {
        if(adapter == null)
        {
            adapter = new OrderInfoListAdapter(getActivity(),frag_type,_order_list,_shop_map);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else
        {
            adapter.setAdapterContent(_order_list,_shop_map);
            adapter.notifyDataSetChanged();
        }
        Log.e(CustomerOrderActivity.MY_TAG,"running on fragment");
    }

    public void setFragmentType(int _frag_type)
    {
        frag_type = _frag_type;
    }
}
