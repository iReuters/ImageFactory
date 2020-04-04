package com.videogo.facedetection;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ezviz.ezopensdk.R;

public class GetPersonNameAlertDialog extends AlertDialog implements View.OnClickListener {
    private EditText etPassword;  //编辑框
    private Button btnConfrim, btnCancel;  //确定取消按钮
    private OnEditInputFinishedListener mListener; //接口

    public interface OnEditInputFinishedListener{
        void editInputFinished(String userName);
    }


    public GetPersonNameAlertDialog(Context context, OnEditInputFinishedListener mListener) {
        super(context);
        this.mListener = mListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_person_name);

        etPassword = findViewById(R.id.et_password);
        btnConfrim = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);
        btnConfrim.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_confirm){
                String userName = etPassword.getText().toString();
                if (userName.equals("")) {
                    mListener.editInputFinished(null);
                } else {
                    mListener.editInputFinished(userName);
                }
                dismiss();
        } else {
                dismiss();
        }

    }
}
