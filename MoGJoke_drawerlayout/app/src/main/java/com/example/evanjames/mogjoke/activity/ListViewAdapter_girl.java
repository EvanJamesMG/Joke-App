package com.example.evanjames.mogjoke.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
 * Created by EvanJames on 2015/9/17.
 */
public class ListViewAdapter_girl extends BaseAdapter {

    private ArrayList<ListViewItem_girl> mArrayList;
    private Context mContext;
    private ImageLoader mImageLoader;
    private GridView mcontentlistview;
    private DisplayImageOptions mDisplayImageOptions;
    private ImageLoadingListenerImpl mImageLoadingListenerImpl;

    public ListViewAdapter_girl(ArrayList<ListViewItem_girl> arrayList,Context context, ImageLoader imageLoader,GridView contentlistView) {
        super();
        this.mArrayList = arrayList;
        this.mContext = context;
        this.mImageLoader = imageLoader;
        this.mcontentlistview = contentlistView;
        int defaultImageId = R.drawable.load_fail_img;
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showStubImage(defaultImageId)
                .showImageForEmptyUri(defaultImageId)
                .showImageOnFail(defaultImageId)
                .cacheInMemory(true)
                .cacheOnDisc(true)
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
            convertView= LayoutInflater.from(this.mContext).inflate(R.layout.photo_layout, null, false);
            holder.ImageView=(ImageView) convertView.findViewById(R.id.girl_photo);
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder) convertView.getTag();
        }


        if (this.mArrayList!=null) {
            final ListViewItem_girl listViewItem_girl=this.mArrayList.get(position);

            if (holder.ImageView!=null) {
                try {
                    //加载图片
                    //Glide.with(mContext).load(listViewItem.getImageURL()).asGif().into(holder.ImageView);
                    mImageLoader.displayImage(
                            listViewItem_girl.imageURL,
                            holder.ImageView,
                            mDisplayImageOptions,
                            mImageLoadingListenerImpl);
                    holder.ImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ImageDetailsActivity_girl.class);
                            intent.putExtra("image_path",listViewItem_girl.imageURL);
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
