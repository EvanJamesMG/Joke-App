package com.example.evanjames.mogjoke.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.evanjames.mogjoke.R;
import com.example.evanjames.mogjoke.activity.AutoRefresh;
import com.example.evanjames.mogjoke.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by EvanJames on 2015/9/13.
 */
public class FragmentOne extends Fragment {
    private static ArrayList<HashMap<String, String>> contentlistItems;
    private static SimpleAdapter contentadapter;
    private static int currentpage=1;//当前的页数
    private static ListView contentlistView;
    private static ArrayList<String> joketitle;
    private static ArrayList<String> jokecontent;
    private static ArrayList<String> joketime;
    private static int pagenum;
    private static RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.layone, container, false);//关联布局文件


        contentlistView = (ListView) rootView.findViewById(R.id.list_view);
        //通过Volley获取响应的Json数据
        mQueue = Volley.newRequestQueue(getActivity());

        /**
         * 主内容中的listview
         */
        contentlistItems = new ArrayList<HashMap<String, String>>();
        initalmap_content();

        contentadapter = new SimpleAdapter(getActivity(),
                contentlistItems,
                R.layout.main_listitem,
                new String[]{"ItemTitle", "ItemText", "ItemTime"},
                new int[]{R.id.joke_title, R.id.joke_content, R.id.joke_time});
        contentlistView.setAdapter(contentadapter);

        // 获取RefreshLayout实例
        final AutoRefresh myRefreshListView = (AutoRefresh) rootView.findViewById(R.id.swipe_layout);

        contentlistView.setOnItemClickListener(new DrawerItemClickListener());


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

                //Toast.makeText(getActivity(), "refresh", Toast.LENGTH_SHORT).show();

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

        // 加载监听器
        myRefreshListView.setOnLoadListener(new AutoRefresh.OnLoadListener() {

            @Override
            public void onLoad() {
                //Toast.makeText(getActivity(), "load", Toast.LENGTH_SHORT).show();

                myRefreshListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        queryJson(++currentpage);
                        if (currentpage >=pagenum) {
                            Toast.makeText(getActivity(), "亲,没有更多内容了", Toast.LENGTH_SHORT).show();
                        }
                        // 加载完后调用该方法
                        myRefreshListView.setLoading(false);
                    }
                }, 1500);
            }
        });

        return rootView;
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
            case 1:
                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
            default:
                break;
        }

        //mDrawerLayout.closeDrawers();
    }

    /**
     * 启动程序时，初始化加载
     * 如果缓存中有数据，直接从缓存中加载，否则直接网络加载。
     * 这里的缓存只是原来退出时候的第一页的数据
     */
    private void initalmap_content() {
        SharedPreferences pref = getActivity().getSharedPreferences("LatestJoke",getActivity().MODE_PRIVATE);
        String response = pref.getString("pageone_JsonResponse","");
        pagenum =5;//这里赋予5，大于currentpage的1,避免第一次加载信息时提示没有更多信息
        if(response!=null&&response.length()!=0){
            //解析json数据并存储第一页的数据，以便于重新进入时加载数据
            joketitle = Utility.handleJokeResponse_gettitle_two(getActivity(), response, currentpage);
            jokecontent = Utility.handleJokeResponse_getcontent_two(getActivity(), response, currentpage);
            joketime  =Utility.handleJokeResponse_gettime_two(getActivity(), response, currentpage);
            if(jokecontent.size()!=0&&joketitle.size()!=0) {
                for (int i = 0; i < jokecontent.size(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("ItemTitle", joketitle.get(i));//题目
                    map.put("ItemText", Html.fromHtml(jokecontent.get(i)).toString());//笑话内容
                    map.put("ItemTime","发布时间:"+joketime.get(i));//笑话的时间
                    contentlistItems.add(map);
                }
            }

        }else {
            queryJson(1);
        }
    }

    /**
     * 得到几天前的时间
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 刷新时重新获取Json数据
     */
    public static void requeryJson(final Context context) {

        currentpage=1;
        mQueue.cancelAll(context);
        mQueue.stop();
        mQueue = Volley.newRequestQueue(context);

        String address;
        java.util.Calendar rightNow = java.util.Calendar.getInstance();
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        String timedate = sim.format(rightNow.getTime());

        int randomnum = 1+(int)(Math.random()*1);//生成一个1 - 5 的随机数

        Date date = new Date(); // 新建一个日期
        java.text.SimpleDateFormat sim2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String beforeDate = sim2.format(getDateBefore(date, randomnum));


        address="https://route.showapi.com/341-1?" +
                "maxResult=10&" +
                "page=" +currentpage+
                "&showapi_appid=" +"Your Id"+
                "&showapi_timestamp="+timedate +
                "&time=" +beforeDate+
                "&showapi_sign="+"Your sign";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //获取所有的笑话的页数
                        try {
                            pagenum = Utility.getJokepagenum_one(context, response.toString());
                            SharedPreferences.Editor editor = context.getSharedPreferences("LatestJoke",context.MODE_PRIVATE).edit();
                            editor.putInt("PageNum", pagenum);
                            //如果是第一页的Json,存储起来，便于以后使用
                            editor.putString("pageone_JsonResponse", response.toString());
                            editor.apply();

                            contentlistItems.clear();

                            //解析json数据并存储第一页的数据，以便于重新进入时加载数据
                            joketitle = Utility.handleJokeResponse_gettitle_two(context, response.toString(), currentpage);
                            jokecontent = Utility.handleJokeResponse_getcontent_two(context, response.toString(), currentpage);
                            joketime  =Utility.handleJokeResponse_gettime_two(context, response.toString(), currentpage);
                            if(jokecontent.size()!=0&&joketitle.size()!=0) {
                                for (int i = 0; i < jokecontent.size(); i++) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("ItemTitle", joketitle.get(i));//题目
                                    map.put("ItemText", Html.fromHtml(jokecontent.get(i)).toString());//笑话内容
                                    map.put("ItemTime","发布时间:"+joketime.get(i));//笑话的时间
                                    contentlistItems.add(map);
                                }
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

    /**
     * 根据传入的页数获取Json数据
     * @param temcurrentpage
     */
    private void queryJson(final int temcurrentpage){
        String address;
        java.util.Calendar rightNow = java.util.Calendar.getInstance();
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        String timedate = sim.format(rightNow.getTime());
        java.text.SimpleDateFormat sim2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String timedate2 = sim2.format(rightNow.getTime());
        address="https://route.showapi.com/341-1?" +
                "maxResult=10&" +
                "page=" +temcurrentpage+
                "&showapi_appid=" +"Your Id"+
                "&showapi_timestamp="+timedate +
                "&time=" +timedate2+
                "&showapi_sign="+"Your sign";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //获取所有的笑话的页数
                        try {
                            pagenum = Utility.getJokepagenum_one(getActivity(), response.toString());
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("LatestJoke",getActivity().MODE_PRIVATE).edit();
                            editor.putInt("PageNum", pagenum);

                            //如果是第一页的Json,存储起来，便于以后使用
                            if(temcurrentpage==1){
                                editor.putString("pageone_JsonResponse", response.toString());
                            }
                            editor.apply();

                            //解析json数据并存储第一页的数据，以便于重新进入时加载数据
                            joketitle = Utility.handleJokeResponse_gettitle_two(getActivity(), response.toString(), currentpage);
                            jokecontent = Utility.handleJokeResponse_getcontent_two(getActivity(), response.toString(), currentpage);
                            joketime  =Utility.handleJokeResponse_gettime_two(getActivity(), response.toString(), currentpage);
                            if(jokecontent.size()!=0&&joketitle.size()!=0) {
                                for (int i = 0; i < jokecontent.size(); i++) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("ItemTitle", joketitle.get(i));//题目
                                    map.put("ItemText", Html.fromHtml(jokecontent.get(i)).toString());//笑话内容
                                    map.put("ItemTime","发布时间:"+joketime.get(i));//笑话的时间
                                    contentlistItems.add(map);
                                }
                                if(jokecontent.size()<10){
                                    //Toast.makeText(getApplicationContext(),"亲，没有更多内容了",Toast.LENGTH_SHORT).show();
                                }
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
                Log.e("TAG", error.getMessage(), error);
                if(currentpage>=2){
                    currentpage--;
                }
                Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    /**
     * 回到顶部
     */
    public static void backToTop(){
       contentlistView.setSelectionAfterHeaderView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mQueue.cancelAll(this);
        mQueue.stop();
    }
}
