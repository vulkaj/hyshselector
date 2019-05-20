package com.example.hyshselector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.hyshselector.utils.Constants.AMOUNT_EXTRA_PHOTO;

public class ResumeOfSelection extends AppCompatActivity {

    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_final_price)
    TextView textFinalPrice;
    @BindView(R.id.linear_brief)
    LinearLayout linearBrief;
    @BindView(R.id.text_wich_pack)
    TextView textWichPack;
    @BindView(R.id.text_total_extra)
    TextView textTotalExtra;
    @BindView(R.id.text_total_sel)
    TextView textTotalSel;
    private Context context;
    private Intent intent;
    private int amount;
    private int totalSelected;
    private int pack;
    private String sessionName;
    private String wichPack;
    private int extra = 0;
    private int extraPhotos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_of_selection);
        ButterKnife.bind(this);


        settingInfo();
        listeners();

    }

    private void settingInfo() {
        intent = getIntent();
        amount = intent.getIntExtra("amount", 0);
        totalSelected = intent.getIntExtra("total_selected", 0);
        pack = intent.getIntExtra("pack", 0);
        sessionName = intent.getStringExtra("session_name");
        wichPack = wichPack(pack);


        textTitle.setText(sessionName);
        textWichPack.setText(wichPack);
        textTotalSel.setText(getString(R.string.total_chosen) + " " + totalSelected);
        if (extraPhotos == 0) {
            textTotalExtra.setVisibility(View.GONE);
        } else {
            extra = extraPhotos * AMOUNT_EXTRA_PHOTO;
            textTotalExtra.setText(getString(R.string.total_extra) + " " + extraPhotos);
            textTotalExtra.setVisibility(View.VISIBLE);
        }
        textFinalPrice.setText(getString(R.string.final_price) + " = " + amount + " €");
    }

    private void listeners() {
        linearBrief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SessionSelector.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private String wichPack(int pack) {
        switch (pack) {
            case 1:
                wichPack = "Pack de 5 fotografías";
                extraPhotos = totalSelected - 5;
                break;
            case 2:
                wichPack = "Pack de 10 fotografías";
                extraPhotos = totalSelected - 10;
                break;
            case 3:
                wichPack = "Pack de 20 fotografías";
                extraPhotos = totalSelected - 20;
                break;
        }
        return wichPack;
    }
}
