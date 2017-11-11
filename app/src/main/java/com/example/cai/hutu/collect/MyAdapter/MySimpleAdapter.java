package com.example.cai.hutu.collect.MyAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.cai.hutu.R;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/6/6.
 */

public class MySimpleAdapter extends SimpleAdapter {
    private LayoutInflater inflator;
    private List<? extends Map<String, ?>> reportData;
    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        inflator = LayoutInflater.from(context);
        this.reportData = data;
    }
    static class ViewHolder{
        public TextView Marker;
        public TextView Result;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup group){
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.item, null);
            holder.Marker = (TextView)convertView.findViewById(R.id.Marker);
            holder.Result = (TextView)convertView.findViewById(R.id.result);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.Marker.setText((String)reportData.get(position).get("Marker"));
        holder.Result.setText((String)reportData.get(position).get("Result"));
        String result = (String)reportData.get(position).get("Result");
      //  if(result.equals("++++" )|| result.equals("++") ||result.equals("+++") ||result.equals("+")){
        if(result.equals("-")||result.equals("0")){
            holder.Result.setBackgroundColor(Color.GREEN);
        }else{
            holder.Result.setBackgroundColor(Color.RED);
        }
        //super.convertView.setBackgroundColor(R.color.blue);
        return convertView;
    }
}
