package com.example.hyshselector.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hyshselector.R;
import com.example.hyshselector.entities.PhotoHysh;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewImageExtended extends AppCompatDialogFragment {

    public Bitmap bitmap;
    public Bundle bundle;
    private PhotoHysh photoHysh;
    private Context context;

    TextView texting;


    public static ViewImageExtended newInstance(Bundle arguments) {
        Bundle args = arguments;
        ViewImageExtended fragment = new ViewImageExtended();
        fragment.setArguments(args);
        return fragment;
    }

    public ViewImageExtended() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        bundle = getArguments();


        // Esta linea de código hace que tu DialogFragment sea Full screen
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_NoTitleBar);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_view_image, container, false);

        ImageView picture = (ImageView) view.findViewById(R.id.ivImage);
        ImageView imageSelection = (ImageView) view.findViewById(R.id.image_icon_add);


        //Glide.with(this).load("http://3.bp.blogspot.com/-uct5OX4Npe0/Vp3dqhe97uI/AAAAAAAABP8/Ij1na2vZb_M/s1600/jasdhjas.jpg").into(picture);

        photoHysh = bundle.getParcelable("info");

        bitmap = bundle.getParcelable("bitmap");


        if (bitmap != null) {
            picture.setImageBitmap(bitmap);
        }


        if (photoHysh.isSelected()) {
            imageSelection.setColorFilter(ContextCompat.getColor(context, R.color.hyshPink));
        } else {
            imageSelection.setColorFilter(ContextCompat.getColor(context, R.color.colorBlack));
        }


        return view;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                dismiss();
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}