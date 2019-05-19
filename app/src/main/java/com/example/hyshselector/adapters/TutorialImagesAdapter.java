package com.example.hyshselector.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;


public class TutorialImagesAdapter extends PagerAdapter {


    private List<Drawable> listTutorial = new ArrayList<>();
    private Context context;

    public TutorialImagesAdapter() {

    }


    public TutorialImagesAdapter(List<Drawable> listTutorial, Context context) {
        this.listTutorial = listTutorial;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (listTutorial.size() == 0) {
            return 1;
        }
        return listTutorial.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }


    @Override
    public Object instantiateItem(ViewGroup holder, int position) {

        ImageView imageView = new ImageView(context);
        Drawable drawable = listTutorial.get(position);
        imageView.setImageDrawable(drawable);
        holder.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup holder, int position, Object object) {
        holder.removeView((View) object);
    }


}
