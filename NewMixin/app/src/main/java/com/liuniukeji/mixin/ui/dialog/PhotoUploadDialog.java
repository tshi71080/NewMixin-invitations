package com.liuniukeji.mixin.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuniukeji.mixin.R;


/**
 *
 */

public class PhotoUploadDialog extends DialogAlarm {
    TextView camero;
    TextView album;
    TextView cancel;
    private String firstItemName;
    private String SecondItemName;

    PhotoUpLisenter lisenter;

    public PhotoUploadDialog(Context context) {
        super(context, R.style.UIKit_Dialog_Fixed);
    }

    public PhotoUploadDialog(Context context, String firstItemName, String SecondItemName) {
        super(context, R.style.UIKit_Dialog_Fixed);
        this.firstItemName = firstItemName;
        this.SecondItemName = SecondItemName;
    }

    @Override
    public View getView(ViewGroup parent) {
        return LayoutInflater.from(getContext()).inflate(R.layout.dialog_photo_up, parent, true);
    }

    @Override
    public void onViewCreated(View view) {

        camero = view.findViewById(R.id.dialog_camera);
        album = view.findViewById(R.id.dialog_album);
        cancel = view.findViewById(R.id.dialog_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != lisenter) {
                    lisenter.cancel();
                    dismiss();
                }
            }
        });

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != lisenter) {
                    lisenter.joinPhoto();
                    dismiss();
                }
            }
        });

        camero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != lisenter) {
                    lisenter.tackPhoto();
                    dismiss();
                }
            }
        });
        if (!TextUtils.isEmpty(firstItemName)) {
            camero.setText(firstItemName);
        }
        if (!TextUtils.isEmpty(SecondItemName)) {
            album.setText(SecondItemName);
        }
    }

    public void setPhotoUpListener(PhotoUpLisenter listener) {
        this.lisenter = listener;
    }

    public interface PhotoUpLisenter {
        void tackPhoto();

        void joinPhoto();

        void cancel();
    }
}
