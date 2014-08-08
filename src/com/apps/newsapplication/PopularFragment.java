package com.apps.newsapplication;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.example.adapter.PopularListAdapter;
import com.example.item.ItemPopular;
import com.example.util.AlertDialogManager;
import com.example.util.Constant;
import com.example.util.JsonUtils;


public class PopularFragment extends SherlockFragment {
	ListView lsv_popular;
	List<ItemPopular> arrayOfPopular;
	PopularListAdapter objAdapter;
	AlertDialogManager alert = new AlertDialogManager();
	private ItemPopular objAllBean;
	ArrayList<String> allListPopid,allListPopName,allListPopImageUrl;
	String[] allArrayPopid,allArrayPopname,allArrayPopImageurl;
	 int textlength = 0;
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View rootView = inflater.inflate(R.layout.fragment_popular, container, false);
		lsv_popular = (ListView)rootView.findViewById(R.id.lsv_popular);
		arrayOfPopular = new ArrayList<ItemPopular>();
		Log.e("aaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaa");
		setHasOptionsMenu(true);
		
		
		allListPopid = new ArrayList<String>();
		allListPopImageUrl = new ArrayList<String>();
		allListPopName = new ArrayList<String>();
		
		allArrayPopid = new String[allListPopid.size()];
		allArrayPopname = new String[allListPopName.size()];
		allArrayPopImageurl = new String[allListPopImageUrl.size()];
		
		lsv_popular.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				objAllBean = arrayOfPopular.get(arg2);
				int Popid = objAllBean.getCategoryId();
				Constant.CATEGORY_IDD = objAllBean.getCategoryId();
				Constant.CATEGORY_TITLE = objAllBean.getCategoryName();
				
				Intent intpop = new Intent(getActivity(), NewsList_Fragment.class);
				startActivity(intpop);
			}
			
		});
		
		if (JsonUtils.isNetworkAvailable(getActivity())) {
			new MyTask().execute(Constant.CATEGORY_URL);
		} else {
			showToast("No Network Connection!");
			alert.showAlertDialog(getActivity(), "Internet Connection Error", "Please connect to working Internet Connection", false);
		}
		
		
		return rootView;
	}
	private class MyTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return JsonUtils.getJSONString(arg0[0]);
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			
			if (null == result || result.length() == 0) {
				showToast("No data found from web!!!");
			} else {
				try {
					JSONObject mainJson = new JSONObject(result);
					JSONArray jsonArray = mainJson.getJSONArray(Constant.CATEGORY_ARRAY_NAME);
					JSONObject objJSON = null;
					for (int i = 0; i < jsonArray.length(); i ++) {
						objJSON = jsonArray.getJSONObject(i);
						
						ItemPopular objItem = new ItemPopular();
						objItem.setCategoryName(objJSON.getString(Constant.CATEGORY_NAME));
						objItem.setCategoryId(objJSON.getInt(Constant.CATEGORY_CID));
						objItem.setCategoryImageurl(objJSON.getString(Constant.CATEGORY_IMAGE));
						arrayOfPopular.add(objItem);
					}
					
				} catch (JSONException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			for (int j = 0; j < arrayOfPopular.size(); j ++) {
				
				Log.e(String.valueOf(arrayOfPopular.size()), "How are you DongMiengIl!");
				objAllBean = arrayOfPopular.get(j);
				
				allListPopid.add(String.valueOf(objAllBean.getCategoryId()));
				allArrayPopid = allListPopid.toArray(allArrayPopid);
				
				allListPopName.add(objAllBean.getCategoryName());
				allArrayPopname = allListPopName.toArray(allArrayPopname);
				
				allListPopImageUrl.add(objAllBean.getCategoryImageurl());
				allArrayPopImageurl = allListPopImageUrl.toArray(allArrayPopImageurl);
			}
			
			setAdapterToListview();
		}
	}
	public void showToast(String msg) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}
	public void setAdapterToListview() {
		// TODO Auto-generated method stub
		//objAdapter = new PopularListAdapter(getActivity(), R.layout.popular_lsv_item, arrayOfPopular);
		//lsv_popular.setAdapter(objAdapter);
		
	}
	


	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_search, menu);
		
		final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		final MenuItem searchMenuItem = menu.findItem(R.id.search);
		
		searchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasfocus) {
				// TODO Auto-generated method stub
				if (!hasfocus) {
					searchMenuItem.collapseActionView();
					searchView.setQuery("", false);
				}
			}
		});
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			
	    	@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
	    		textlength=newText.length();
	    		arrayOfPopular.clear();
	    		
	    		for(int i=0;i<allArrayPopname.length;i++)
	    		{
	    			if(textlength <= allArrayPopname[i].length())
	        		{
	    				if(newText.toString().equalsIgnoreCase((String) allArrayPopname[i].subSequence(0, textlength)))
	        			{
	    					ItemPopular objItem = new ItemPopular();
	    					objItem.setCategoryId(Integer.parseInt(allArrayPopid[i]));
	    					objItem.setCategoryName(allArrayPopname[i]);
	    					objItem.setCategoryImageurl(allArrayPopImageurl[i]);
	    					
	    					arrayOfPopular.add(objItem);
	        			}
	        		}
	    		}
	    		
	    		setAdapterToListview();
	    	
	    		return false;
			}
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
}
