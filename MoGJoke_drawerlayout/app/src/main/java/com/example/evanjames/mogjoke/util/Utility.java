package com.example.evanjames.mogjoke.util;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by EvanJames on 2015/8/24.
 * 由于服务器返回的省市县数据都是“代号|城市,代号|城市”这种格式的，所以我们最好再提供一个工具类来解析和处理这种数据。
 */
public class Utility {

    /**
     * 返回json数据中的总的页数
     *
     * @param context
     * @param response
     * @return
     * @throws JSONException
     */


    public static int getJokepagenum_one(Context context, String response) throws JSONException {
        int pagenum = 0;
        if (!TextUtils.isEmpty(response)) {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject res_body = jsonObject.getJSONObject("showapi_res_body");
            pagenum = res_body.getInt("allPages");
        }
        return pagenum;
    }
    public static int getJokepagenum_two(Context context, String response) throws JSONException {
        int pagenum = 0;
        if (!TextUtils.isEmpty(response)) {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject res_body = jsonObject.getJSONObject("showapi_res_body");

            JSONObject pagebean = res_body.getJSONObject("pagebean");
            pagenum = pagebean.getInt("allPages");
        }
        return pagenum;
    }




    public static int getJokepagenum_three(Context context, String response) throws JSONException {
        int pagenum = 0;
        if (!TextUtils.isEmpty(response)) {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject res_body = jsonObject.getJSONObject("showapi_res_body");
            pagenum = res_body.getInt("allPages");
        }
        return pagenum;
    }
    public static int getJokepagenum_four(Context context, String response) throws JSONException {
        int pagenum = 0;
        if (!TextUtils.isEmpty(response)) {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject res_body = jsonObject.getJSONObject("showapi_res_body");
            JSONObject num  = res_body.getJSONObject("9");
            if(num==null){
                return -1;
            }
        }
        return 1;
    }

    /**
     * 解析服务器返回的JSON数据，返回题目
     * 并将解析出的第一页数据存储到本地。
     */
    public static ArrayList<String> handleJokeResponse_gettitle_one(Context context, String response, int currentpage) {

        ArrayList<String> joketitle = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject res_bode = jsonObject.getJSONObject("showapi_res_body");

                JSONObject pagebean = res_bode.getJSONObject("pagebean");

                JSONArray contentlist = pagebean.getJSONArray("contentlist");

                for (int i = 0; i < 20; i++) {
                    JSONObject content = contentlist.getJSONObject(i);
                    joketitle.add(content.getString("name"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return joketitle;
    }

    /**
     * 解析服务器返回的JSON数据，返回内容
     * 并将解析出的第一页数据存储到本地。
     */
    public static ArrayList<String> handleJokeResponse_getcontent_one(Context context, String response, int currentpage) {

        ArrayList<String> jokecontent = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject res_bode = jsonObject.getJSONObject("showapi_res_body");

                JSONObject pagebean = res_bode.getJSONObject("pagebean");

                JSONArray contentlist = pagebean.getJSONArray("contentlist");


                for (int i = 0; i < 20; i++) {
                    JSONObject content = contentlist.getJSONObject(i);
                    jokecontent.add(content.getString("text"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return jokecontent;
    }


    /**
     * 解析服务器返回的JSON数据，返回时间
     * 并将解析出的第一页数据存储到本地。
     */
    public static ArrayList<String> handleJokeResponse_gettime_one(Context context, String response, int currentpage) {

        ArrayList<String> jokecontent = new ArrayList<>();
        ArrayList<String> joketime = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject res_bode = jsonObject.getJSONObject("showapi_res_body");

                JSONObject pagebean = res_bode.getJSONObject("pagebean");

                JSONArray contentlist = pagebean.getJSONArray("contentlist");


                for (int i = 0; i < 20; i++) {
                    JSONObject content = contentlist.getJSONObject(i);

                    joketime.add(content.getString("create_time"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return joketime;
    }

    /**
     * 解析服务器返回的JSON数据，返回题目
     * 并将解析出的第一页数据存储到本地。
     */
    public static ArrayList<String> handleJokeResponse_gettitle_two(Context context, String response, int currentpage) {

        ArrayList<String> joketitle = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject res_bode = jsonObject.getJSONObject("showapi_res_body");

                JSONArray contentlist = res_bode.getJSONArray("contentlist");

                for (int i = 0; i < 20; i++) {
                    JSONObject content = contentlist.getJSONObject(i);
                    joketitle.add(content.getString("title"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return joketitle;
    }

    /**
     * 解析服务器返回的JSON数据，返回内容
     * 并将解析出的第一页数据存储到本地。
     */
    public static ArrayList<String> handleJokeResponse_getcontent_two(Context context, String response, int currentpage) {

        ArrayList<String> jokecontent = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject res_bode = jsonObject.getJSONObject("showapi_res_body");

                JSONArray contentlist = res_bode.getJSONArray("contentlist");


                for (int i = 0; i < 20; i++) {
                    JSONObject content = contentlist.getJSONObject(i);
                    jokecontent.add(content.getString("text"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return jokecontent;
    }


    /**
     * 解析服务器返回的JSON数据，返回时间
     * 并将解析出的第一页数据存储到本地。
     */
    public static ArrayList<String> handleJokeResponse_gettime_two(Context context, String response, int currentpage) {

        ArrayList<String> jokecontent = new ArrayList<>();
        ArrayList<String> joketime = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject res_bode = jsonObject.getJSONObject("showapi_res_body");

                JSONArray contentlist = res_bode.getJSONArray("contentlist");


                for (int i = 0; i < 20; i++) {
                    JSONObject content = contentlist.getJSONObject(i);
                    joketime.add(content.getString("ct"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return joketime;
    }


    /**
     * 解析服务器返回的JSON数据，返回题目
     * 并将解析出的第一页数据存储到本地。
     */
    public static ArrayList<String> handleJokeResponse_get_img_title_three(Context context, String response, int currentpage) {

        ArrayList<String> joketitle = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject res_bode = jsonObject.getJSONObject("showapi_res_body");

                JSONArray contentlist = res_bode.getJSONArray("contentlist");

                for (int i = 0; i < 10; i++) {
                    JSONObject content = contentlist.getJSONObject(i);
                    joketitle.add(content.getString("title"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return joketitle;
    }

    /**
     * 解析服务器返回的JSON数据，返回时间
     * 并将解析出的第一页数据存储到本地。
     */
    public static ArrayList<String> handleJokeResponse_gettime_three(Context context, String response, int currentpage) {

        ArrayList<String> joke_time = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject res_bode = jsonObject.getJSONObject("showapi_res_body");

                JSONArray contentlist = res_bode.getJSONArray("contentlist");


                for (int i = 0; i < 10; i++) {
                    JSONObject content = contentlist.getJSONObject(i);
                    joke_time.add(content.getString("ct"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return joke_time;
    }


    /**
     * 解析服务器返回的JSON数据，返回图片地址
     * 并将解析出的第一页数据存储到本地。
     */
    public static ArrayList<String> handleJokeResponse_get_img_three(Context context, String response, int currentpage) {

        ArrayList<String> joke_img_url = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject res_bode = jsonObject.getJSONObject("showapi_res_body");

                JSONArray contentlist = res_bode.getJSONArray("contentlist");


                for (int i = 0; i < 10; i++) {
                    JSONObject content = contentlist.getJSONObject(i);
                    joke_img_url.add(content.getString("img"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return joke_img_url;
    }

    /**
     * 解析服务器返回的JSON数据，返回题目
     * 并将解析出的第一页数据存储到本地。
     */
    public static ArrayList<String> handleJokeResponse_get_img_title_four(Context context, String response, int currentpage) {

        ArrayList<String> joketitle = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject res_bode = jsonObject.getJSONObject("showapi_res_body");


                for (int i = 0; i < 10; i++) {

                    String tem = Integer.toString(i);
                    JSONObject index = res_bode.getJSONObject(tem);

                    joketitle.add(index.getString("title"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return joketitle;
    }

    /**
     * 解析服务器返回的JSON数据，返回图片地址
     * 并将解析出的第一页数据存储到本地。
     */
    public static ArrayList<String> handleJokeResponse_get_img_four(Context context, String response, int currentpage) {

        ArrayList<String> joke_img_url = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject res_bode = jsonObject.getJSONObject("showapi_res_body");


                for (int i = 0; i < 10; i++) {

                    String tem = Integer.toString(i);
                    JSONObject index = res_bode.getJSONObject(tem);

                    joke_img_url.add(index.getString("picUrl"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return joke_img_url;
    }


//    /**
//     * 将服务器解析的第一页的数据存储到SharedPreferences文件中。
//     */
//    private static void savejokeimfo(Context context,ArrayList<String> inputdata) {
//        SharedPreferences.Editor editor = context.getSharedPreferences("TextJoke",Context.MODE_PRIVATE).edit();
//        editor.put
//
//        editor.commit();
//
//    }
}
