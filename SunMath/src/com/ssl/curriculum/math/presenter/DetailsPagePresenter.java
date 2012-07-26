package com.ssl.curriculum.math.presenter;

import com.ssl.curriculum.math.listener.NavigationMenuItemSelectedListener;

public class DetailsPagePresenter implements NavigationMenuItemSelectedListener{

	@Override
	public void onItemSelected(int dbIndex) {
		System.out.println(dbIndex);
	}
}
