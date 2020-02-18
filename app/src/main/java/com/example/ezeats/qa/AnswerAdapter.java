package com.example.ezeats.qa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ezeats.R;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerVH> {

    private static final String TAG = "AnswerAdapter";
    List<QA> qaList;

    public AnswerAdapter(List<QA> qaList){
        this.qaList = qaList;
    }


    @NonNull
    @Override
    public AnswerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qa_item_view,parent,false);
        return new AnswerVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerVH holder, int position) {

        QA qa = qaList.get(position);
        holder.tvNumber.setText(qa.getId());
        holder.tvQuestion.setText(qa.getTitle());
        holder.tvDetail.setText(qa.getDetail());

        boolean isExpanded = qaList.get(position).isExpanded();
        holder.ExpandedLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return qaList.size();
    }
    class AnswerVH extends RecyclerView.ViewHolder{
        private static final String TAG = "AnswerVH";

        ConstraintLayout ExpandedLayout;
        TextView tvNumber,tvQuestion,tvDetail;

        public AnswerVH(@NonNull View itemView) {
            super(itemView);

            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            ExpandedLayout = itemView.findViewById(R.id.ExpandedLayout);

            tvQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QA qa = qaList.get(getAdapterPosition());
                    qa.setExpanded(!qa.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }
}
