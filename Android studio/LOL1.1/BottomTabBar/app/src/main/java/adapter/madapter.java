package adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.bottomtabbar.R;

import java.util.ArrayList;

import entity.MessageInfo;

public class madapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<MessageInfo> array;

    public madapter(LayoutInflater inf, ArrayList<MessageInfo> arry){
        this.inflater=inf;
        this.array=arry;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {  //代码块中包含了对listview的效率优化
        ViewHolder vh;
        if(convertView==null){
            vh=new ViewHolder();
            convertView=inflater.inflate(R.layout.message_list_item,null);//加载listview子项
            vh.iv=convertView.findViewById(R.id.imagePhoto);
            vh.tv1=(TextView) convertView.findViewById(R.id.imgrightname);
            vh.tv2=(TextView) convertView.findViewById(R.id.imgrighttime);
            vh.tv3=convertView.findViewById(R.id.message_content);
            vh.tv4=convertView.findViewById(R.id.school);
            vh.iv.setDrawingCacheEnabled(true);
            convertView.setTag(vh);
        }
        vh=(ViewHolder) convertView.getTag();
        //设置图片
        byte[] picturebyte = Base64.decode(array.get(position).photo, Base64.DEFAULT);//图片字符串转成字节
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap picturebmp = BitmapFactory.decodeByteArray(picturebyte, 0, picturebyte.length);
        vh.iv.setImageBitmap(picturebmp);

        vh.tv1.setText(array.get(position).name);//名字
        vh.tv2.setText(array.get(position).time);//时间
        vh.tv3.setText(array.get(position).content);//内容
        vh.tv4.setText("——"+array.get(position).university+" "+array.get(position).institute);
        return convertView;
    }
    class ViewHolder{ //内部类，对控件进行缓存
        ImageView iv;
        TextView tv1,tv2,tv3,tv4;
    }
}
