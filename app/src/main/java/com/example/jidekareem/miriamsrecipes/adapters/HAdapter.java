package com.example.jidekareem.miriamsrecipes.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jidekareem.miriamsrecipes.R;
import com.example.jidekareem.miriamsrecipes.data.MirriamData;
import com.example.jidekareem.miriamsrecipes.mirriamActivities.MainActivity;

import java.util.HashMap;
import java.util.List;

public class HAdapter extends RecyclerView.Adapter<HAdapter.MainAdapterViewHolder> {

    final private MItemClickListener mOnClickListener;
    private List<MirriamData> mirriamDataList;
    private Context context;

    public HAdapter(MItemClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public MainAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.main_adapter_list;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MainAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapterViewHolder bakingAdapterViewHolder, int position) {
        bakingAdapterViewHolder.recipeTextView.setText(mirriamDataList.get(position).name);


        int i = 1;
        String videoLink = mirriamDataList.get(position).steps.get(i).getVideoURL();
        final String anObject = "";
        while (videoLink.equals(anObject)) {
            i++;
            videoLink = mirriamDataList.get(position).steps.get(i).getVideoURL();
        }

        try {
            Drawable drawable = new BitmapDrawable(context.getResources(), retriveVideoFrameFromVideo(videoLink));
            bakingAdapterViewHolder.recipeTextView.setBackground(drawable);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        if (null == mirriamDataList) return 0;
        return mirriamDataList.size();
    }


    public void setMoviesData(final List<MirriamData> newSteps) {
        if (mirriamDataList == null) {
            mirriamDataList = newSteps;
            notifyDataSetChanged();
        } else {

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mirriamDataList.size();
                }

                @Override
                public int getNewListSize() {
                    return newSteps.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mirriamDataList.get(oldItemPosition).getId() ==
                            newSteps.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    MirriamData newStep = newSteps.get(newItemPosition);
                    MirriamData oldStep = mirriamDataList.get(oldItemPosition);
                    return newStep.getId() == oldStep.getId()
                            && newStep.getId() == (oldStep.getId());
                }
            });
            mirriamDataList = newSteps;
            result.dispatchUpdatesTo(this);
        }

    }

    public interface MItemClickListener {
        void onMItemClick(MirriamData mirriamData, int clickedPosition);
    }

    public class MainAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView recipeTextView;

        MainAdapterViewHolder(View itemView) {
            super(itemView);

            recipeTextView = itemView.findViewById(R.id.main_adapter_text);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            MirriamData mirriamData = mirriamDataList.get(clickedPosition);
            mOnClickListener.onMItemClick(mirriamData, clickedPosition);
        }

    }


    @SuppressWarnings("SuspiciousNameCombination")
    private Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();

            }
        }
        int newWidth = 600;
        if (!MainActivity.TABLET) {
            bitmap = getResizedBitmap(bitmap, newWidth, newWidth);
        } else {

            int newHeight = 350;
            bitmap = getResizedBitmap(bitmap, newWidth, newHeight);
        }

        return bitmap;
    }


    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}