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
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.evanjames.mogjoke.R;
import com.example.evanjames.mogjoke.activity.AutoRefresh;
import com.example.evanjames.mogjoke.activity.ListViewAdapter_girl;
import com.example.evanjames.mogjoke.activity.ListViewItem_girl;
import com.example.evanjames.mogjoke.util.Utility;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.evanjames.mogjoke.fragment.FragmentThree.initImageLoader;

/**
 * Created by EvanJames on 2015/9/13.
 */
public class FragmentFour extends Fragment {


    private static int currentpage=1;//当前的页数

    private static ArrayList<ListViewItem_girl> arrayList;
    private static ImageLoader imageLoader;
    private static ListViewAdapter_girl contentadapter;
    private static GridView contentlistView;
    private static ListViewItem_girl listViewItem_girl;

    private static ArrayList<String> joke_img_title;
    private static ArrayList<String> joke_img_url;

    private static ProgressDialog progressDialog;
    private static int pagenum;
    private static RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.layfour, container, false);//关联布局文件

        initImageLoader(getActivity());
        imageLoader= ImageLoader.getInstance();

        //通过Volley获取响应的Json数据
        mQueue = Volley.newRequestQueue(getActivity());

        initalmap_content();


        contentlistView =(GridView) rootView.findViewById(R.id.photo_wall);

        arrayList = new ArrayList<ListViewItem_girl>();

        contentadapter = new ListViewAdapter_girl(arrayList,getActivity(),imageLoader,contentlistView);

        contentlistView.setAdapter(contentadapter);

        contentlistView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(contentlistView.getLastVisiblePosition() == (contentlistView.getAdapter().getCount() - 1)) {
                    //showProgressDialog();
                    if(pagenum==-1) {
                        Toast.makeText(getActivity(),"亲，没有更多内容喽",Toast.LENGTH_SHORT).show();
                    }else {
                        showProgressDialog(getActivity());
                        //由于Api调用频率的限制，这里进行秒的延时操作
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                queryJson(++currentpage);
                                closeProgressDialog();
                            }
                        }, 2000);
                    }
                }

            }
        });


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

                //Toast.makeText(getActivity(), "refresh", Toast.LENGTH_SHORT).show();

                myRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // 更新数据
                        requeryJson(getActivity());

                        contentadapter.notifyDataSetChanged();
                        // 更新完后调用该方法结束刷新
                        myRefreshListView.setRefreshing(false);
                    }
                }, 1000);
            }
        });


        return rootView;
    }


    private void initalmap_content() {
        queryJson(1);
    }


    /**
     * 刷新时重新获取Json数据
     *
     */
    public static void requeryJson(final Context context) {

        mQueue.cancelAll(context);
        mQueue.stop();
        mQueue = Volley.newRequestQueue(context);

        String address;
        java.util.Calendar rightNow = java.util.Calendar.getInstance();
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        String timedate = sim.format(rightNow.getTime());
        java.text.SimpleDateFormat sim2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String timedate2 = sim2.format(rightNow.getTime());
        int randomnum = 1+(int)(Math.random()*6);//生成一个1 - 6 的随机数
        currentpage = randomnum;



        address ="https://route.showapi.com/197-1?num=10"+
                "&page=" +currentpage+
                "&showapi_appid=Your Id" +
                "&showapi_timestamp=" +timedate+
                "&showapi_sign=Your sign";

        showProgressDialog(context);
        final int finalCurrentpage = currentpage;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //获取所有的笑话的页数
                        try {
                            pagenum = Utility.getJokepagenum_four(context, response.toString());
                            SharedPreferences.Editor editor = context.getSharedPreferences("ImgJoke", context.MODE_PRIVATE).edit();
                            editor.putInt("PageNum", pagenum);
                            editor.apply();

                            //刷新时，将原链表清空，重新进行加载
                            arrayList.clear();

                            //解析json数据并存储第一页的数据，以便于重新进入时加载数据
                            joke_img_title = Utility.handleJokeResponse_get_img_title_four(context, response.toString(), finalCurrentpage);
                            joke_img_url  =Utility.handleJokeResponse_get_img_four(context, response.toString(), finalCurrentpage);
                            for (int i = 0; i < joke_img_url.size(); i++) {
                                listViewItem_girl = new ListViewItem_girl("This is " + i, joke_img_title.get(i),joke_img_url.get(i));
                                arrayList.add(listViewItem_girl);
                            }
                            closeProgressDialog();
                            contentadapter.notifyDataSetChanged();
                            contentlistView.smoothScrollToPosition(0);//刷新之后重新回到顶部


                        } catch (JSONException e) {
                            closeProgressDialog();
                            Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
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
     * 根据传入的页数返回Json数据
     * @param tcurrentpage
     */
    private void queryJson(final int tcurrentpage){

        String address;
        java.util.Calendar rightNow = java.util.Calendar.getInstance();
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        String timedate = sim.format(rightNow.getTime());
        java.text.SimpleDateFormat sim2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String timedate2 = sim2.format(rightNow.getTime());

        address ="https://route.showapi.com/197-1?num=10"+
                "&page=" +Integer.toString(tcurrentpage)+
                "&showapi_appid=Your Id" +
                "&showapi_timestamp=" +timedate+
                "&showapi_sign=Your sign";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //获取所有的笑话的页数
                        try {
                            pagenum = Utility.getJokepagenum_four(getActivity(), response.toString());
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("ImgJoke", getActivity().MODE_PRIVATE).edit();
                            editor.putInt("PageNum", pagenum);
                            editor.apply();

                            //解析json数据并存储第一页的数据，以便于重新进入时加载数据
                            joke_img_title = Utility.handleJokeResponse_get_img_title_four(getActivity(), response.toString(), currentpage);
                            joke_img_url  =Utility.handleJokeResponse_get_img_four(getActivity(), response.toString(), currentpage);
                            for (int i = 0; i < joke_img_url.size(); i++) {
                                listViewItem_girl = new ListViewItem_girl("This is " + i, joke_img_title.get(i),joke_img_url.get(i));
                                arrayList.add(listViewItem_girl);
                            }
                            contentadapter.notifyDataSetChanged();
                            closeProgressDialog();

                        } catch (JSONException e) {
                            currentpage--;
                            Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                currentpage--;
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);
    }


    public static void backToTop(){
        contentlistView.smoothScrollToPosition(0);
    }

    public static void clean(){
        imageLoader.clearMemoryCache();
        imageLoader.clearDiskCache();
    }


    /**
     * 显示进度对话框
     */
    private static void showProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("一大波妹子来袭中...");
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
    private static void closeProgressDialog() {
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
