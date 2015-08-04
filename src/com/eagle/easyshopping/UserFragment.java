package com.eagle.easyshopping;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class UserFragment extends Fragment implements OnClickListener {
	Button userName;
	Button myCtn;
	Button myOrder;
	Button myAddress;
	Button aboutUs;
	Button logOut;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View newsLayout = inflater.inflate(R.layout.user_layout, container,
				false);
	

		return newsLayout;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		userName = (Button) getActivity().findViewById(R.id.username);
		userName.setText(((MainActivity) getActivity()).getUsername());
		myCtn = (Button) getActivity().findViewById(R.id.myctnb);
		myOrder = (Button) getActivity().findViewById(R.id.myorder);
		myAddress = (Button) getActivity().findViewById(R.id.myaddress);
		aboutUs = (Button) getActivity().findViewById(R.id.aboutus);
		logOut = (Button) getActivity().findViewById(R.id.logout);
		userName.setOnClickListener(this);
		myCtn.setOnClickListener(this);
		myOrder.setOnClickListener(this);
		myAddress.setOnClickListener(this);
		aboutUs.setOnClickListener(this);
		logOut.setOnClickListener(this);
	}
public void changeUserName(String newName){
	MainActivity main=(MainActivity)getActivity();
	main.setUsername(newName);

	
	
	
}
	public void onClick(View v) {
		MainActivity main=(MainActivity)getActivity();

		Intent intent = null;
		switch (v.getId()) {

		case R.id.username:
		
			Toast.makeText(this.getActivity(), "username", Toast.LENGTH_SHORT)
					.show();
			
			
			break;
		case R.id.myctnb:
		
			Toast.makeText(this.getActivity(), "myctnb", Toast.LENGTH_SHORT)
					.show();
			intent = new Intent(getActivity(),ShopNewsActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.myorder:
			Toast.makeText(this.getActivity(), "myorder", Toast.LENGTH_SHORT)
					.show();
			intent = new Intent(getActivity(),CustomerOrderActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.myaddress:
			Toast.makeText(this.getActivity(), "myaddress", Toast.LENGTH_SHORT)
					.show();
			Intent intent4 = new Intent(getActivity(),
					AddressActivity.class);
			intent4.putExtra("username",main.getUsername());
			startActivity(intent4);

			break;
		case R.id.logout:
			Toast.makeText(this.getActivity(), "logout", Toast.LENGTH_SHORT)
					.show();
			((MainActivity) getActivity()).setUsername("");
			MainActivity main4 = (MainActivity) getActivity();
			main.setTabSelection(4);
			
			break;
		case R.id.aboutus:
			Toast.makeText(this.getActivity(), "aboutus:", Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			break;
		}
	}

}