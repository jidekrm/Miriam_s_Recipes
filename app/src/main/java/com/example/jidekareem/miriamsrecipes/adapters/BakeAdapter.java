package com.example.jidekareem.miriamsrecipes.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jidekareem.miriamsrecipes.R;
import com.example.jidekareem.miriamsrecipes.data.Steps;

import java.util.List;

public class BakeAdapter extends RecyclerView.Adapter<BakeAdapter.BakingAdapterViewHolder> {

    final private ItemClickListener mOnClickListener;
    private List<Steps> stepsList;

    public BakeAdapter(ItemClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public BakingAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_description_list; //check here
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutIdForListItem, viewGroup, false);
        return new BakingAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BakingAdapterViewHolder bakingAdapterViewHolder, int position) {
        bakingAdapterViewHolder.descriptionTextView.setText(stepsList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (null == stepsList) return 0;
        return stepsList.size();
    }


    public void setMoviesData(final List<Steps> newSteps) {
        if (stepsList == null) {
            stepsList = newSteps;
            notifyDataSetChanged();
        } else {

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return stepsList.size();
                }

                @Override
                public int getNewListSize() {
                    return newSteps.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return stepsList.get(oldItemPosition).getId() ==
                            newSteps.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Steps newStep = newSteps.get(newItemPosition);
                    Steps oldStep = stepsList.get(oldItemPosition);
                    return newStep.getId() == oldStep.getId()
                            && newStep.getId() == (oldStep.getId());
                }
            });
            stepsList = newSteps;
            result.dispatchUpdatesTo(this);
        }

    }

    public interface ItemClickListener {
        void onItemClick(String clickedItemIndex, String stepInstruction, int clickedPosition);
    }

    public class BakingAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView descriptionTextView;

        BakingAdapterViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.description_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            String stepInstruction = stepsList.get(clickedPosition).getDescription();
            String mVideoUrl = stepsList.get(clickedPosition).getVideoURL();
            String mThumbnailUrl = stepsList.get(clickedPosition).getThumbnailURL();
            String urlUsed = mVideoUrl;
            final String anObject = "";
            if (mVideoUrl.equals(anObject)) {
                urlUsed = mThumbnailUrl;
            }
            mOnClickListener.onItemClick(urlUsed, stepInstruction, clickedPosition);
        }
    }
}

