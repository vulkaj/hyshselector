package com.example.hyshselector.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.hyshselector.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TutorialActivity extends AppCompatActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.image)
    ImageView image;
    //@BindView(R.id.circle_indicator)
    //me.relex.circleindicator.CircleIndicator circleIndicator;
    //TODO Circluindicator???
    private List<String> stringList;
    private List<Drawable> listDrawable;
    private Context context;
    private TutorialImagesAdapter tutorialImagesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        ButterKnife.bind(this);
        context = this;
        listDrawable = new ArrayList<Drawable>();


        fillImageSwipeTutorial();
        settingAdapterTutorial();
    }


    private void fillImageSwipeTutorial() {

        Drawable tut1 = getResources().getDrawable(R.drawable.tut1);
        Drawable tut2 = getResources().getDrawable(R.drawable.tut2);
        Drawable tut3 = getResources().getDrawable(R.drawable.tut3);
        Drawable tut4 = getResources().getDrawable(R.drawable.tut4);
        listDrawable.add(tut1);
        listDrawable.add(tut2);
        listDrawable.add(tut3);
        listDrawable.add(tut4);

    }

    private void settingAdapterTutorial() {

        tutorialImagesAdapter = new TutorialImagesAdapter(listDrawable, context);
        viewPager.setAdapter(tutorialImagesAdapter);

    }
}
