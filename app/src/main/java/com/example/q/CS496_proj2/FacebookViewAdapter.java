package com.example.q.CS496_proj2;

/**
 * Created by q on 2017-07-10.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



public class FacebookViewAdapter extends BaseAdapter {

    Bitmap bmImg;
    ImageView iconImageView;
    String listViewItem2;
    getFacebookImages task;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<String> listViewItemList = new ArrayList() ;
    private ArrayList<String> listViewItemList2 = new ArrayList() ;

    // ListViewAdapter의 생성자
    public FacebookViewAdapter(ArrayList list, ArrayList list2) {
        listViewItemList = list;
        listViewItemList2 = list2;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tab1_2_contacts_listview, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        iconImageView = (ImageView) convertView.findViewById(R.id.picture) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.name) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득

        String listViewItem = listViewItemList.get(position);
        listViewItem2 = listViewItemList2.get(position);

        // 아이템 내 각 위젯에 데이터 반영

        titleTextView.setText(listViewItem);

        task = new getFacebookImages();
        task.execute();

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public class getFacebookImages extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... none) {
            try {
                URL myFileUrl = new URL(listViewItem2);
                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();

                bmImg = BitmapFactory.decodeStream(is);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void none){

            iconImageView.setImageBitmap(bmImg);
        }
    }
}
