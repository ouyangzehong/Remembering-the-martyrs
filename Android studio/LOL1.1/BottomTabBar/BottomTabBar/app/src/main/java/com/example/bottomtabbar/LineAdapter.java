package com.example.bottomtabbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


public class LineAdapter extends ArrayAdapter<Line> {
    private int resourceId;
    private String name;
    //重写构造函数，把id和数据传进来
    public LineAdapter(Context context,int textViewResourceId,List<Line>objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Line line = getItem(position);//获取当前项的实例
      /* View view = LayoutInflater.from
                (getContext()).inflate(resourceId, parent, false);
        */
        //对上一个语句进行优化
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);//false参数表示只让我们在父布局中声明的layout属性生效
        }else {
            view = convertView;
        }

        ImageView lineImage = view.findViewById(R.id.line_image);
        lineImage.setImageResource(line.getImageId());

        TextView lineName = view.findViewById(R.id.lineNeme);
        lineName.setText(line.getName());

        TextView lineEditName = view.findViewById(R.id.lineEditName);
        lineEditName.setText(line.getLineEditName());

        return view;

    }
}