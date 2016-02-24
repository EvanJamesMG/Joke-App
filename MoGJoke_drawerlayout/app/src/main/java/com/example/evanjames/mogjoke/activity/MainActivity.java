package com.example.evanjames.mogjoke.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.evanjames.mogjoke.R;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private static SimpleAdapter contentadapter;
    private static ArrayList<HashMap<String, String>> contentlistItems;
    private static int currentpage=1;//当前的页数
    private static ListView contentlistView;
    private static ArrayList<String> joketitle;
    private static ArrayList<String> jokecontent;
    private static ArrayList<String> joketime;
    private static Fragment[] fragments;
    private int Fragmentlabel;
    private ImageButton Exit_img;
    private TextView Exit_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setfragment();
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        Exit_img = (ImageButton) findViewById(R.id.Exit_img);
        Exit_txt = (TextView) findViewById(R.id.Exit_text);


        //设置退出键的弹出对话框
        Exit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());  //先得到构造器
                builder.setTitle("提示"); //设置标题
                builder.setMessage("是否确认退出?"); //设置内容
                builder.setIcon(R.drawable.laugh);//设置图标，图片id即可
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //关闭dialog
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //参数都设置完成了，创建并显示出来
                builder.create().show();

            }
        });
        //设置退出键的弹出对话框
        Exit_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());  //先得到构造器
                builder.setTitle("提示"); //设置标题
                builder.setMessage("是否确认退出?"); //设置内容
                builder.setIcon(R.drawable.laugh);//设置图标，图片id即可
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //关闭dialog
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //参数都设置完成了，创建并显示出来
                builder.create().show();
            }
        });


        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        // mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);


        toolbar.setTitle("MoG笑话");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();


        /**
         * 左侧抽出菜单中的listview
         */
        //生成动态数组，并且转载数据
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

        initalmap(mylist);


        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new SimpleAdapter(this, //没什么解释
                mylist,//数据来源
                R.layout.my_listitem,//ListItem的XML实现
                //动态数组与ListItem对应的子项
                new String[]{"ItemImage", "ItemText"},
                //ListItem的XML文件里面的两个TextView ID
                new int[]{R.id.ItemImg, R.id.ItemTitle}));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }


     /**初始化fragment*/
     public void setfragment() {
             fragments=new Fragment[4];
             fragments[0]=getSupportFragmentManager().findFragmentById(R.id.fragmentone);
             fragments[1]=getSupportFragmentManager().findFragmentById(R.id.fragmenttwo);
             fragments[2]=getSupportFragmentManager().findFragmentById(R.id.fragmentthree);
             fragments[3]=getSupportFragmentManager().findFragmentById(R.id.fragmentfour);

         Fragmentlabel =1;//判断是哪一个fragment的标记
         SharedPreferences.Editor editor =getSharedPreferences("FragmentLabel", Context.MODE_PRIVATE).edit();
         editor.putInt("label",Fragmentlabel);
         editor.apply();

         getSupportFragmentManager().beginTransaction().hide(fragments[1]).hide(fragments[2])
                 .hide(fragments[3]).show(fragments[0]).commit();
     }


    private void initalmap(ArrayList mylist) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("ItemImage", R.drawable.listbtn);//加入图片
        map.put("ItemText", "最新段子");
        mylist.add(map);
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("ItemImage", R.drawable.listbtn);//加入图片
        map2.put("ItemText", "文本笑话");
        mylist.add(map2);
        HashMap<String, Object> map3 = new HashMap<String, Object>();
        map3.put("ItemImage", R.drawable.listbtn);//加入图片
        map3.put("ItemText", "图片笑话");
        mylist.add(map3);
        HashMap<String, Object> map4 = new HashMap<String, Object>();
        map4.put("ItemImage", R.drawable.listbtn);//加入图片
        map4.put("ItemText", "美女图片");
        mylist.add(map4);
    }


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    private void selectItem(int position) {

        // update selected item and title, then close the drawer
        //mDrawerList.setItemChecked(position, true);
        switch (position){
            case 0:
                Fragmentlabel =1;//判断是哪一个fragment的标记
                SharedPreferences.Editor editor = getSharedPreferences("FragmentLabel", Context.MODE_PRIVATE).edit();
                editor.putInt("label",Fragmentlabel);
                editor.apply();
                getSupportFragmentManager().beginTransaction().hide(fragments[1]).hide(fragments[2]).hide(fragments[3])
                        .show(fragments[0]).commit();
                break;
            case 1:
                Fragmentlabel =2;//判断是哪一个fragment的标记
                SharedPreferences.Editor editor2 = getSharedPreferences("FragmentLabel", Context.MODE_PRIVATE).edit();
                editor2.putInt("label",Fragmentlabel);
                editor2.apply();
                getSupportFragmentManager().beginTransaction().hide(fragments[0]).hide(fragments[1]).hide(fragments[2]).hide(fragments[3])
                        .show(fragments[1]).commit();
                break;
            case 2:
                Fragmentlabel =3;//判断是哪一个fragment的标记
                SharedPreferences.Editor editor3 = getSharedPreferences("FragmentLabel", Context.MODE_PRIVATE).edit();
                editor3.putInt("label",Fragmentlabel);
                editor3.apply();
                getSupportFragmentManager().beginTransaction().hide(fragments[0]).hide(fragments[1]).hide(fragments[2]).hide(fragments[3])
                        .show(fragments[2]).commit();
                break;
            case 3:
                Fragmentlabel =4;//判断是哪一个fragment的标记
                SharedPreferences.Editor editor4 = getSharedPreferences("FragmentLabel", Context.MODE_PRIVATE).edit();
                editor4.putInt("label",Fragmentlabel);
                editor4.apply();
                getSupportFragmentManager().beginTransaction().hide(fragments[0]).hide(fragments[1]).hide(fragments[2]).hide(fragments[3])
                        .show(fragments[3]).commit();
                break;
            default:
                break;
        }

        //mDrawerLayout.closeDrawers();
    }

    /**
     *捕获Back按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出。
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认退出?"); //设置内容
        builder.setIcon(R.drawable.laugh);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }


}

