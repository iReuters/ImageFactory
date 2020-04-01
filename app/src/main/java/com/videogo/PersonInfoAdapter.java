package com.videogo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ezviz.ezopensdk.R;

import static com.videogo.openapi.EzvizAPI.TAG;

public class PersonInfoAdapter extends RecyclerView.Adapter<PersonInfoAdapter.ViewHolder> {

    private ArrayList<PersonInfo> mPersonInfoList;

    private Bitmap bitmap;


    public PersonInfoAdapter(ArrayList<PersonInfo> mPersonInfoList) {
        this.mPersonInfoList = mPersonInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.person_info, null);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PersonInfo personInfo = mPersonInfoList.get(position);
        Log.d(TAG, "onBindViewHolder: 1" + "  " + personInfo.getId());
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/0_OpenSDK/Portraits";
        File file = new File(filePath);
        File[] files = file.listFiles();
        for (File mFile : files) {
            Log.d(TAG, "onBindViewHolderaaa: " + mFile.getName() + "  " +personInfo.getId());
            if (mFile.getName().equals(personInfo.getId() + ".jpg")) {
                Log.d(TAG, "onBindViewHolder: " + mFile.getName() + "  " +personInfo.getId());
                bitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath());
                break;
            }
        }
        holder.personPortrait.setImageBitmap(bitmap);
        Log.d(TAG, "onBindViewHolder: 2");
        holder.personName.setText(personInfo.getName() == null? personInfo.getId() : personInfo.getName());
        holder.personAge.setText(String.valueOf(personInfo.getAge()));
        holder.personGender.setText(personInfo.getGender() == 0? "男" : "女");
    }


    @Override
    public int getItemCount() {
        return mPersonInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView personPortrait;
        TextView personName;
        TextView personAge;
        TextView personGender;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            personPortrait = itemView.findViewById(R.id.person_portrait);
            personName = itemView.findViewById(R.id.person_name);
            personAge = itemView.findViewById(R.id.person_age);
            personGender = itemView.findViewById(R.id.person_gender);
        }
    }
}
