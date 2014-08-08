package com.example.adapter;

import java.util.List;

import com.example.item.ItemPopular;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;

public class PopularListAdapter extends ArrayAdapter<ItemPopular> {

	public PopularListAdapter(Activity act, int resource,
			List<ItemPopular> arrayList) {
		super(act, resource, arrayList);
		// TODO Auto-generated constructor stub
	}

}
