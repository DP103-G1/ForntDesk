package com.example.ezeats.SUGBox;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezeats.R;
import com.example.ezeats.main.Common;
import com.example.ezeats.main.Url;
import com.example.ezeats.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class ListBoxFragment extends Fragment {
    private static final String TAG = "TAG_ListBoxFragment";
    private TextView tvTitle;
    private RecyclerView rvBoxList;
    private Activity activity;
    private CommonTask boxGetAllTask;
    private List<Box> boxes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_box, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle = activity.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.textconnection);
        rvBoxList = view.findViewById(R.id.rvBoxList);
        rvBoxList.setLayoutManager(new LinearLayoutManager(activity));
        boxes = getBoxes();
        showBoxes(boxes);
    }

    private List<Box> getBoxes(){
        List<Box> boxes = null;
        if (Common.networkConnected(activity)){
            String url = Url.URL + "/BoxServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action","getAll");
            String jsonOut = jsonObject.toString();
            boxGetAllTask = new CommonTask(url,jsonOut);
            try {
                String jsonIn = boxGetAllTask.execute().get();
                Type listType = new TypeToken<List<Box>>(){
                }.getType();
                boxes = new Gson().fromJson(jsonIn,listType);
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }
        }else{
            Common.showToast(activity,R.string.textDisconnected);
        }
        return boxes;
    }

    private void showBoxes(List<Box>boxes){
        if (boxes == null || boxes.isEmpty()){
            Common.showToast(activity,R.string.textMessageFound);
        }
        BoxAdapter boxAdapter = (BoxAdapter) rvBoxList.getAdapter();
        if (boxAdapter == null){
            rvBoxList.setAdapter(new BoxAdapter(activity,boxes));
        }else {
            boxAdapter.setBoxes(boxes);
            boxAdapter.notifyDataSetChanged();
        }
    }

    private class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.MyViewHolder>{
        private  LayoutInflater layoutInflater;
        private  List<Box>boxes;

        BoxAdapter(Context context, List<Box> boxes) {
            layoutInflater = LayoutInflater.from(context);
            this.boxes = boxes;
        }

        void setBoxes(List<Box> boxes){
            this.boxes = boxes;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            ConstraintLayout expandedLayout;
            LinearLayout reMessageLinearlayout;
            TextView tvNumber,tvQuestion,tvDetail,tvIsReply,tvReplyContent;

         public MyViewHolder(View itemView){
                super(itemView);
                tvNumber = itemView.findViewById(R.id.tvNumber);
                tvQuestion = itemView.findViewById(R.id.tvQuestion);
                tvDetail = itemView.findViewById(R.id.tvDetail);
                tvIsReply = itemView.findViewById(R.id.tvIsReply);
                tvReplyContent = itemView.findViewById(R.id.tvReplyContent);
                reMessageLinearlayout = itemView.findViewById(R.id.reMessageLinearlayout);
                expandedLayout = itemView.findViewById(R.id.ExpandedLayout);//給他連線啊！

                tvQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Box box = boxes.get(getAdapterPosition());
                        box.setExpanded(!box.isExpanded());
                        notifyItemChanged(getAdapterPosition());
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return boxes.size();
        }

        @NonNull
        @Override
        public BoxAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.sugbox_item_view,parent,false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull BoxAdapter.MyViewHolder holder, int position) {
            final Box box = boxes.get(position);
            String url = Url.URL + "/BoxServlet";
            holder.tvNumber.setText(String.valueOf(box.getId()));
            holder.tvQuestion.setText(box.getTopic());
            holder.tvDetail.setText(box.getFeed_back());
            if(box.getReply() != null){
                holder.reMessageLinearlayout.setVisibility(View.VISIBLE);
                holder.tvReplyContent.setText(box.getReply());
            }else {
                holder.reMessageLinearlayout.setVisibility(View.GONE);
            }

            boolean isExpanded = boxes.get(position).isExpanded();
            holder.expandedLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (boxGetAllTask != null){
            boxGetAllTask.cancel(true);
            boxGetAllTask = null;
        }
    }
}
