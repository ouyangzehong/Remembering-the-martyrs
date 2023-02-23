package com.example.bottomtabbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import model.Data;
import presenter.MyAdapter;
import presenter.MyDatabase;

public class MainActivityNote extends AppCompatActivity {
    ListView listView;
    FloatingActionButton floatingActionButton;
    LayoutInflater layoutInflater;
    ArrayList<Data> arrayList;
    MyDatabase myDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_note);


        listView=findViewById(R.id.layout_listview);
        floatingActionButton=findViewById(R.id.add_note);
        layoutInflater=getLayoutInflater();

        //把对sqlite的操作封装成类进行操作
        myDatabase = new MyDatabase(this);
        //获取数据库的数据数组
        arrayList = myDatabase.getarray();
        //listview的自定义适配器
        MyAdapter adapter = new MyAdapter(layoutInflater,arrayList);
        listView.setAdapter(adapter);

        //listview的单击监听函数（查看修改） 获取点击的item的id并跳转到新建页面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {   //点击一下跳转到编辑页面（编辑页面与新建页面共用一个布局）
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),New_note.class);
                intent.putExtra("ids",arrayList.get(position).getIds());
                startActivity(intent);
                MainActivityNote.this.finish();
            }
        });
        //item的长按事件（删除）
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {   //长按删除
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivityNote.this)
                        //.setTitle("确定要删除此便签？")
                        .setMessage("确定要删除该笔记？")
                        .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除数据库这一项的数据 重新适配listview
                                myDatabase.toDelete(arrayList.get(position).getIds());
                                arrayList = myDatabase.getarray();
                                MyAdapter myAdapter = new MyAdapter(layoutInflater,arrayList);
                                listView.setAdapter(myAdapter);

                            }
                        })
                        .create()
                        .show();
                return true;
            }
        });

        //浮动按钮的监听函数
        floatingActionButton.setOnClickListener(new View.OnClickListener() {   //点击悬浮按钮时，跳转到新建页面
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),New_note.class);
                startActivity(intent);
                MainActivityNote.this.finish();
            }
        });
    }

    //重载菜单函数
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_lo,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //菜单项的单击监听函数
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){//主页面...的新建 退出
            case R.id.menu_newnote:
                Intent intent = new Intent(getApplicationContext(),New_note.class);
                startActivity(intent);
                MainActivityNote.this.finish();
                break;
            case R.id.menu_exit:
                MainActivityNote.this.finish();
                break;
            default:
                break;
        }
        return  true;

    }

}