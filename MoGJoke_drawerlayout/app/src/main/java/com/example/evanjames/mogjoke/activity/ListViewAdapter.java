package com.example.evanjames.mogjoke.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evanjames.mogjoke.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by EvanJames on 2015/9/15.
 */
public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> mArrayList;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private ImageLoadingListenerImpl mImageLoadingListenerImpl;

    public ListViewAdapter(ArrayList<ListViewItem> arrayList,Context context, ImageLoader imageLoader) {
        super();
        this.mArrayList = arrayList;
        this.mContext = context;
        this.mImageLoader = imageLoader;
        int defaultImageId = R.drawable.load_fail_img;
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showStubImage(defaultImageId)
                .showImageForEmptyUri(defaultImageId)
                .showImageOnFail(defaultImageId)
                .cacheInMemory(true) //设置下载的图片是否缓存则内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .resetViewBeforeLoading()
                .build();
        mImageLoadingListenerImpl=new ImageLoadingListenerImpl();
    }

    public int getCount() {
        if (mArrayList==null) {
            return 0;
        } else {
            return mArrayList.size();
        }

    }

    public Object getItem(int position) {
        if (mArrayList==null) {
            return null;
        } else {
            return mArrayList.get(position);
        }
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null) {
            holder=new ViewHolder();
            convertView= LayoutInflater.from(this.mContext).inflate(R.layout.main_img_listitem, null, false);
            holder.textViewTitle=(TextView) convertView.findViewById(R.id.joke_img_title);
            holder.textViewTime =(TextView) convertView.findViewById(R.id.joke_time);
            holder.ImageView=(ImageView) convertView.findViewById(R.id.joke_img);
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder) convertView.getTag();
        }


        if (this.mArrayList!=null) {
            final ListViewItem listViewItem=this.mArrayList.get(position);
            if (holder.textViewTitle!=null) {
                holder.textViewTitle.setText(listViewItem.getImageTitle());
            }
            if(holder.textViewTime != null){
                holder.textViewTime.setText(listViewItem.getImageTime());
            }
            if (holder.ImageView!=null) {
                try {
                    //加载图片
                    //Glide.with(mContext).load(listViewItem.getImageURL()).asGif().into(holder.ImageView);
                    mImageLoader.displayImage(
                            listViewItem.imageURL,
                            holder.ImageView,
                            mDisplayImageOptions,
                            mImageLoadingListenerImpl);
                    holder.ImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ImageDetailsActivity.class);
                            intent.putExtra("image_path",listViewItem.imageURL);
                            mContext.startActivity(intent);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return convertView;
    }

    //监听图片异步加载
    public static class ImageLoadingListenerImpl extends SimpleImageLoadingListener {

        public static final List<String> displayedImages =
                Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,Bitmap bitmap) {
            if (bitmap != null) {
                ImageView ImageView = (ImageView) view;
                boolean isFirstDisplay = !displayedImages.contains(imageUri);
                if (isFirstDisplay) {
                    //图片的淡入效果
                    FadeInBitmapDisplayer.animate(ImageView, 500);
                    displayedImages.add(imageUri);
                    System.out.println("===> loading "+imageUri);
                }
            }
        }
    }



    private class ViewHolder{
        ImageView ImageView;
        TextView textViewTitle;
        TextView textViewTime;
    }

}
