package com.example.hyshselector.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyshselector.R;
import com.example.hyshselector.entities.PhotoHysh;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.hyshselector.utils.Constants.TAG_BITMAP;
import static com.example.hyshselector.utils.Constants.TAG_INFO;

public class ViewImageExtended extends AppCompatDialogFragment  {

    public Bitmap bitmap;
    public Bundle bundle;
    private Context context;
    private ImageView picture;
    private ImageView imageNext;
    private ImageView imagePrevious;
    private ImageView imageSelection;
    private RelativeLayout relativeLeft;
    private RelativeLayout relativeRight;
    private ArrayList<PhotoHysh> listImages;
    private String sessionName;
    private int position;
    private String path;

    public ArrayList<PhotoHysh> getListImages() {
        return listImages;
    }

    public void setListImages(ArrayList<PhotoHysh> listImages) {
        this.listImages = listImages;
    }

    public boolean isImageSelected() {
        return listImages.get(position).isSelected();
    }

    public void setImageSelected(boolean imageSelected) {
        isImageSelected = imageSelected;
    }

    private boolean isImageSelected;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static ViewImageExtended newInstance(Bundle arguments) {
        Bundle args = arguments;
        ViewImageExtended fragment = new ViewImageExtended();
        fragment.setArguments(args);
        return fragment;
    }

    public ViewImageExtended() {
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

        picture = (ImageView) view.findViewById(R.id.ivImage);
        imageNext = (ImageView) view.findViewById(R.id.image_next);
        imagePrevious = (ImageView) view.findViewById(R.id.image_previous);
        imageSelection = (ImageView) view.findViewById(R.id.image_icon_add);
        relativeLeft = (RelativeLayout) view.findViewById(R.id.relative_previous);
        relativeRight = (RelativeLayout) view.findViewById(R.id.relative_next);


        listeners();


        //Glide.with(this).load("http://3.bp.blogspot.com/-uct5OX4Npe0/Vp3dqhe97uI/AAAAAAAABP8/Ij1na2vZb_M/s1600/jasdhjas.jpg").into(picture);

        listImages = bundle.getParcelableArrayList("list_images");

        bitmap = bundle.getParcelable(TAG_BITMAP);
        sessionName = bundle.getString("session_name");
        position = bundle.getInt("position");
        path = Environment.getExternalStorageDirectory().toString() + "/HyshSelections/" + sessionName;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 2;
        File realFile = new File(path, listImages.get(position).getName());
        Bitmap realBitmap = BitmapFactory.decodeFile(realFile.getAbsolutePath(), bmOptions);


        if (bitmap != null) {
            picture.setImageBitmap(realBitmap);
        }


        isSelected();


        return view;
    }

    private void isSelected() {
        if (listImages.get(position).isSelected()) {
            imageSelection.setColorFilter(ContextCompat.getColor(context, R.color.hyshPink));
        } else {
            imageSelection.setColorFilter(ContextCompat.getColor(context, R.color.colorBlack));
        }
    }

    private void listeners() {
        relativeRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextOrPreviousPicture(true);
            }
        });

        relativeLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextOrPreviousPicture(false);
            }
        });

        imageSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listImages.get(position).isSelected()) {
                    listImages.get(position).setSelected(false);
                } else {
                    listImages.get(position).setSelected(true);
                }
                isSelected();
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void getNextOrPreviousPicture(boolean isNext) {

        if (isNext) {
            if (position < listImages.size() - 1) {
                position = position + 1;

            }

        } else {
            if (position > 0) {
                position = position - 1;

            }

        }
        isSelected();
        selectPicture();
    }

    private void selectPicture() {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 2;
        File realFile = new File(path, listImages.get(position).getName());

        Bitmap realBitmap = BitmapFactory.decodeFile(realFile.getAbsolutePath(), bmOptions);

        picture.setImageBitmap(realBitmap);
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
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}