package com.example.evanjames.mogjoke.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.evanjames.mogjoke.R;
import com.example.evanjames.mogjoke.activity.AutoRefresh;
import com.example.evanjames.mogjoke.activity.ListViewAdapter;
import com.example.evanjames.mogjoke.activity.ListViewItem;
import com.example.evanjames.mogjoke.util.Utility;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by EvanJames on 2015/9/13.
 */
public class FragmentThree extends Fragment {


    private static int currentpage=1;//当前的页数


    private static ArrayList<String> joke_img_title;
    private static ArrayList<String> joke_img_des;
    private static ArrayList<String> joke_img_url;
    private static ArrayList<String> joke_time;

    private static ListView contentlistView;
    private static ListViewAdapter contentadapter;

    private static ArrayList<ListViewItem> arrayList;
    private static ListViewItem listViewItem;
    private static ImageLoader imageLoader;
    private static int pagenum;
    private static RequestQueue mQueue;
    private static int Fragmentlabel;
    private static ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       final View rootView = inflater.inflate(R.layout.laythree, container, false);//关联布局文件


        //初始化
        initImageLoader(getActivity());
        imageLoader=ImageLoader.getInstance();

        //通过Volley获取响应的Json数据
        mQueue = Volley.newRequestQueue(getActivity());

        initalmap_content();


        contentlistView = (ListView) rootView.findViewById(R.id.list_view);
        arrayList = new ArrayList<ListViewItem>();
        contentadapter = new ListViewAdapter(arrayList,getActivity(),imageLoader);
        contentlistView.setAdapter(contentadapter);

        // 获取RefreshLayout实例
        final AutoRefresh myRefreshListView = (AutoRefresh) rootView.findViewById(R.id.swipe_layout);


        // 设置下拉刷新时的颜色值,颜色值需要定义在xml中
        myRefreshListView
                .setColorSchemeResources(android.R.color.holo_green_dark,
                        android.R.color.holo_red_dark,
                        android.R.color.holo_blue_dark,
                        android.R.color.holo_orange_dark);
        // 设置下拉刷新监听器
        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                myRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // 更新数据
                        requeryJson(getActivity());
                        // 更新完后调用该方法结束刷新
                        myRefreshListView.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        // 加载更多监听器
        myRefreshListView.setOnLoadListener(new AutoRefresh.OnLoadListener() {

            @Override
            public void onLoad() {

                myRefreshListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (currentpage >= pagenum) {
                            Toast.makeText(getActivity(), "亲,没有更多内容了", Toast.LENGTH_SHORT).show();
                            myRefreshListView.setLoading(false);
                        } else {
                            queryJson(++currentpage);
                            //由于Api调用频率的限制，这里进行秒的延时操作
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    myRefreshListView.setLoading(false);
                                }
                            }, 1000);
                        }// 加载完后调用该方法
                    }
                }, 1000);
            }
        });


        ButtonDemoFragment menu3 = new ButtonDemoFragment();
        getChildFragmentManager().beginTransaction()
                .add(R.id.container, menu3)
                .commit();

        return rootView;
    }


    public static class ButtonDemoFragment extends Fragment {

        public ButtonDemoFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_menu_with_custom_action_button, container, false);

            //创建圆形菜单
            ImageView icon = new ImageView(getActivity()); // Create an icon
            icon.setBackgroundResource(R.drawable.menubtn);
            //itemIcon.setImageDrawable( ... );
            FloatingActionButton actionButton = new FloatingActionButton.Builder(getActivity())
                    .setContentView(icon)
                    .build();

            SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
            // repeat many times:
            ImageView itemIcon_top = new ImageView(getActivity());
            ImageView itemIcon_clean = new ImageView(getActivity());
            ImageView itemIcon_refresh = new ImageView(getActivity());

            itemIcon_top.setBackgroundResource(R.drawable.top);
            itemIcon_clean.setBackgroundResource(R.drawable.clean);
            itemIcon_refresh.setBackgroundResource(R.drawable.refresh);
            //itemIcon.setImageDrawable();

            SubActionButton btn_top = itemBuilder.setContentView(itemIcon_top).build();
            final SubActionButton btn_clean = itemBuilder.setContentView(itemIcon_clean).build();
            SubActionButton btn_refresh = itemBuilder.setContentView(itemIcon_refresh).build();


            FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                    .addSubActionView(btn_top)
                    .addSubActionView(btn_clean)
                    .addSubActionView(btn_refresh)
                    .attachTo(actionButton)
                    .build();


            btn_clean.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences pref = getActivity().getSharedPreferences("FragmentLabel",Context.MODE_PRIVATE);
                    Fragmentlabel = pref.getInt("label",Fragmentlabel);
                    Toast.makeText(getActivity(),"清理缓存完毕",Toast.LENGTH_SHORT).show();
                    switch (Fragmentlabel){

                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            clean();
                            break;
                        case 4:
                            FragmentFour.clean();
                            break;
                        default:
                            break;
                    }

                }
            });

            btn_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences pref = getActivity().getSharedPreferences("FragmentLabel",Context.MODE_PRIVATE);
                    Fragmentlabel = pref.getInt("label",Fragmentlabel);
                    switch (Fragmentlabel){

                        case 1:
                            showProgressDialog_refresh(getActivity());
                            FragmentOne.requeryJson(getActivity());

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    closeProgressDialog_refresh();
                                    Toast.makeText(getActivity(), "已是最新", Toast.LENGTH_SHORT).show();
                                }
                            }, 2000);
                            break;
                        case 2:
                            showProgressDialog_refresh(getActivity());
                            FragmentTwo.requeryJson(getActivity());

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    closeProgressDialog_refresh();
                                    Toast.makeText(getActivity(), "已是最新", Toast.LENGTH_SHORT).show();
                                }
                            }, 2000);
                            break;
                        case 3:
                            showProgressDialog_refresh(getActivity());
                            requeryJson(getActivity());

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    closeProgressDialog_refresh();
                                    Toast.makeText(getActivity(),"已是最新",Toast.LENGTH_SHORT).show();
                                }
                            }, 2000);
                            break;
                        case 4:
                            showProgressDialog_refresh(getActivity());
                            FragmentFour.requeryJson(getActivity());

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    closeProgressDialog_refresh();
                                    Toast.makeText(getActivity(), "已是最新", Toast.LENGTH_SHORT).show();
                                }
                            }, 2000);
                            break;
                        default:
                            break;
                    }
                    // 更新数据
                }
            });

            btn_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences pref = getActivity().getSharedPreferences("FragmentLabel",Context.MODE_PRIVATE);
                    Fragmentlabel = pref.getInt("label",Fragmentlabel);
                    switch (Fragmentlabel){

                        case 1:
                            FragmentOne.backToTop();
                            break;
                        case 2:
                            FragmentTwo.backToTop();
                            break;
                        case 3:
                            backToTop();
                            break;
                        case 4:
                            FragmentFour.backToTop();
                            break;
                        default:
                            break;
                    }
                }
            });

            return rootView;
        }
    }


    /**
     *  Configuration of ImageLoader:
     *  This configuration tuning is custom.
     *  You can tune every option, you may tune some of them,
     *  or you can create default configuration by
     *  ImageLoaderConfiguration.createDefault(this) method.
     *
     *  Note:
     *  1 enableLogging() // Not necessary in common
     *  2 实际项目中可将其放到Application中
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 3)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }


    private void initalmap_content() {
        pagenum=2;//初始大小定为大于1的一个数，目的避免程序初始进入提示用户没有更多的内容
        queryJson(1);
    }


    /**
     * 得到几天前的时间
     */
    private static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 刷新时重新获取Json数据
     *
     */
    private static void requeryJson(final Context context) {

        currentpage = 1;
        mQueue.cancelAll(context);
        mQueue.stop();
        mQueue = Volley.newRequestQueue(context);

        String address;
        java.util.Calendar rightNow = java.util.Calendar.getInstance();
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        String timedate = sim.format(rightNow.getTime());

        int randomnum = 1+(int)(Math.random()*10);//生成一个1 - 3 的随机数

        Date date = new Date(); // 新建一个日期
        java.text.SimpleDateFormat sim2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String beforeDate = sim2.format(getDateBefore(date, randomnum));


        address = "https://route.showapi.com/341-2?maxResult=10" +
                "&page=1" +
                "&showapi_appid="+"Your Id" +
                "&showapi_timestamp=" +timedate+
                "&time=" +beforeDate+
                "&showapi_sign="+"Your sign";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //获取所有的笑话的页数
                        try {
                            pagenum = Utility.getJokepagenum_three(context, response.toString());
                            SharedPreferences.Editor editor = context.getSharedPreferences("ImgJoke",context.MODE_PRIVATE).edit();
                            editor.putInt("PageNum", pagenum);
                            editor.apply();

                            //刷新之后重新从第一页开始，并且把原来的列表清空
                            arrayList.clear();

                            //解析json数据并存储第一页的数据，以便于重新进入时加载数据
                            joke_img_title = Utility.handleJokeResponse_get_img_title_three(context, response.toString(), currentpage);
                            joke_time = Utility.handleJokeResponse_gettime_three(context, response.toString(), currentpage);
                            joke_img_url  =Utility.handleJokeResponse_get_img_three(context, response.toString(), currentpage);

                            for (int i = 0; i < joke_img_url.size(); i++) {
                                listViewItem = new ListViewItem("This is " + i, joke_img_title.get(i),joke_time.get(i),joke_img_url.get(i));
                                arrayList.add(listViewItem);
                            }
                            contentadapter.notifyDataSetChanged();
                            contentlistView.setSelectionAfterHeaderView();

                        } catch (JSONException e) {
                            Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(jsonObjectRequest);
    }


    private void queryJson(final int tcurrentpage){

        String address;
        java.util.Calendar rightNow = java.util.Calendar.getInstance();
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        String timedate = sim.format(rightNow.getTime());
        java.text.SimpleDateFormat sim2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String timedate2 = sim2.format(rightNow.getTime());

        Date date = new Date(); // 新建一个日期
        String beforeDate = sim2.format(getDateBefore(date, 10));//最新的图片笑话太少了，里往前推10天


        address = "https://route.showapi.com/341-2?maxResult=10" +
                "&page=" +tcurrentpage+
                "&showapi_appid="+"Your Id" +
                "&showapi_timestamp=" +timedate+
                "&time=" +beforeDate+
                "&showapi_sign="+"Your sign";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //获取所有的笑话的页数
                        try {
                            pagenum = Utility.getJokepagenum_three(getActivity(), response.toString());
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("ImgJoke", getActivity().MODE_PRIVATE).edit();
                            editor.putInt("PageNum", pagenum);

                            //如果是第一页的Json,存储起来，便于以后使用
                            if(tcurrentpage==1){
                                editor.putString("pagethree_JsonResponse", response.toString());
                            }
                            editor.apply();

                            //解析json数据并存储第一页的数据，以便于重新进入时加载数据
                            joke_img_title = Utility.handleJokeResponse_get_img_title_three(getActivity(), response.toString(), currentpage);
                            joke_time = Utility.handleJokeResponse_gettime_three(getActivity(), response.toString(), currentpage);
                            joke_img_url  =Utility.handleJokeResponse_get_img_three(getActivity(), response.toString(), currentpage);
                            for (int i = 0; i < joke_img_url.size(); i++) {
                                listViewItem = new ListViewItem("This is " + i, joke_img_title.get(i),joke_time.get(i),joke_img_url.get(i));
                                arrayList.add(listViewItem);
                            }
                            contentadapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            if(currentpage>=2){
                                currentpage--;
                            }
                            Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(currentpage>=2){
                        currentpage--;
                    }
                    Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
                    Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(jsonObjectRequest);
    }
    public static void backToTop(){
        contentlistView.setSelectionAfterHeaderView();
    }

    public static void clean(){
        imageLoader.clearMemoryCache();
        imageLoader.clearDiskCache();
    }

    /**
     * 显示进度对话框
     */
    private static void showProgressDialog_refresh(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("正在刷新...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        try {
            progressDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 关闭进度对话框
     */
    private static void closeProgressDialog_refresh() {
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imageLoader.clearMemoryCache();

        imageLoader.stop();
        mQueue.cancelAll(this);
        mQueue.stop();
    }
}
