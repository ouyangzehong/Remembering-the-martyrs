package com.example.bottomtabbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.text.SimpleDateFormat;
import java.util.Date;

import model.Data;
import presenter.MyDatabase;

public class New_note extends AppCompatActivity {

    EditText ed_title;
    EditText ed_content;
    FloatingActionButton floatingActionButton;
    MyDatabase myDatabase;
    Data data;
    int ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        ed_title=findViewById(R.id.title);
        ed_content=findViewById(R.id.content);
        floatingActionButton=findViewById(R.id.finish);
        myDatabase=new MyDatabase(this);

        Intent intent=this.getIntent();
        //获取单击选项id
        ids=intent.getIntExtra("ids",0);
        if(ids!=0){
            data=myDatabase.getTiandCon(ids);
            ed_title.setText(data.getTitle());
            ed_content.setText(data.getContent());
        }
        //保存按钮响应事件
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSave();
            }
        });
    }
    //保存函数
    private void isSave(){
        //获取时间
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd   HH：mm");
        Date date=new Date(System.currentTimeMillis());
        String time =simpleDateFormat.format(date);
        String title=ed_title.getText().toString();
        String content=ed_content.getText().toString();
        //如果ids是传过来的值则修改数据库 如果是默认值0则进行插入操作
        if(ids!=0){
            data=new Data(title,ids,content,time);
            myDatabase.toUpdate(data);

            Intent intent=new Intent(New_note.this,MainActivityNote.class);
            startActivity(intent);
            New_note.this.finish();
        }
        else {
            data=new Data(title,content,time);
            myDatabase.toInsert(data);

            Intent intent=new Intent(New_note.this,MainActivityNote.class);
            startActivity(intent);
            New_note.this.finish();
        }
    }
    @Override
    public void onBackPressed() {     //重写返回建方法，如果是属于新建则插入数据表并返回主页面，如果是修改，修改表中数据并返回主页面
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd   HH:mm");
        Date date =  new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        String title = ed_title.getText().toString();
        String content = ed_content.getText().toString();
        if(ids!=0){
            data=new Data(title,ids, content, time);
            myDatabase.toUpdate(data);
            Intent intent=new Intent(New_note.this,MainActivityNote.class);
            startActivity(intent);
            New_note.this.finish();
        }
        //新建日记
        else{
            data=new Data(title,content,time);
            myDatabase.toInsert(data);
            Intent intent=new Intent(New_note.this,MainActivityNote.class);
            startActivity(intent);
            New_note.this.finish();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_lo,menu);
        return true;
    }
    //重写菜单按钮单击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_share :
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "标题："+ed_title.getText().toString()+"    " +
                                "内容："+ed_content.getText().toString());
                startActivity(intent);
                break;
            default:
                break;
        }
        return false;
    }

}