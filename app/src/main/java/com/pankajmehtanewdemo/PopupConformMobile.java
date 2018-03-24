package com.pankajmehtanewdemo;

/**
 * Created by raju on 02-08-2017.
 */

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.Calendar;


@SuppressLint("ValidFragment")
public class PopupConformMobile extends DialogFragment implements OnClickListener {
    public onSubmitListener mListener;
    Button btn_ok, btn_close;
    String mobile_number;
    TextView tv_mobile_number_title;
    ProgressDialog dialog;
    String codeid,hDate;
    int Year, Month, Day;
    String otp,usermob;
    JSONArray resultsarray;
    Context context=getActivity();

    @SuppressLint("ValidFragment")
    public PopupConformMobile(String no) {
        this.mobile_number = no;
        Calendar calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        int mnth = Month + 1;
        String date = Day + "-" + mnth + "-" + Year;
        hDate = Year + "-" + mnth + "-" + Day;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
               /* startActivity(new Intent(getActivity().getApplicationContext(), EnterPasswordActivity.class));
               // getActivity().finish();*/
                break;

            case R.id.btn_close:
                dismiss();
                break;

        }
    }





    public interface onSubmitListener {
        void setOnSubmitListener(String arg);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.custom_dialog_mobile_verify);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        tv_mobile_number_title = (TextView) dialog.findViewById(R.id.tv_mobile_number_title);
        tv_mobile_number_title.setText(this.mobile_number);
        btn_close = (Button) dialog.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);
        return dialog;
    }

}
