package com.zhuandian.qxe.MainFrame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuandian.qxe.R;

import java.util.ArrayList;

/**
 * @author  谢栋
 * 2016年8月14日21:11:16
 */
public class WKZXSFragment extends Fragment {

    private View parentView;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.calendar, container, false);
        ( (TextView)  getActivity().findViewById(R.id.navigation_text)).setText("失物招领");

        listView   = (ListView) parentView.findViewById(R.id.listView);
        initView();
        return parentView;
    }

    private void initView(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                getCalendarData());
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "Clicked item!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<String> getCalendarData(){
        ArrayList<String> calendarList = new ArrayList<String>();
        calendarList.add("1");
        calendarList.add("2");
        calendarList.add("3");
        calendarList.add("4");
        calendarList.add("5");
        calendarList.add("6");
        calendarList.add("8");
        calendarList.add("4");
        calendarList.add("5");
        calendarList.add("6");
        calendarList.add("8");

        return calendarList;
    }
}
