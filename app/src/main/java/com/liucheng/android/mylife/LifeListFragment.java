package com.liucheng.android.mylife;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liucheng on 2017/5/17.
 */

public class LifeListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LifeAdapter mAdapter;
    private FloatingActionButton mMusicButton;
    private FloatingActionButton mAddLifeButton;

    private static final String MUSIC_SERVICE = "com.liucheng.android.mylife.MusicService";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_life_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.life_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mMusicButton = (FloatingActionButton) view.findViewById(R.id.float_bgm_button);

        mMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isWorked(getActivity(), MUSIC_SERVICE)){
                    Intent intent = MusicService.newService(getActivity());
                    getActivity().stopService(intent);
                    mMusicButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_play_music));
                } else {
                    Intent intent = MusicService.newService(getActivity());
                    getActivity().startService(intent);
                    mMusicButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_pause_music));
                }

            }
        });

        mAddLifeButton = (FloatingActionButton) view.findViewById(R.id.float_action_button);
        mAddLifeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = LifePageActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });

        updateUI();

        return view;
    }

    /**
     * 判断服务是否已经启动（来源于百度）
     * @param className
     * @return
     */
    private boolean isWorked(Context context, String className) {
        ActivityManager myManager = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService =
                (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(60);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    private class LifeHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView_1;
        private ImageView mImageView_2;
        private ImageView mImageView_3;
        private TextView mMoreTextView;
        private TextView mTitleTextView;
        private TextView mFeelTextView;
        private TextView mDateTextView;
        private Life mLife;

        public LifeHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = LifePageMainActivity.newIntent(getActivity(), mLife.getId());
                    startActivity(intent);
                }
            });

            mImageView_1 = (ImageView) itemView.findViewById(R.id.list_item_image_view_1);
            mImageView_2 = (ImageView) itemView.findViewById(R.id.list_item_image_view_2);
            mImageView_3 = (ImageView) itemView.findViewById(R.id.list_item_image_view_3);
            mMoreTextView = (TextView) itemView.findViewById(R.id.list_item_more_text_view);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_title_text_view);
            mFeelTextView = (TextView) itemView.findViewById(R.id.list_item_feel_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_date_text_view);
        }

        public void bindLife(Life life){
            mLife = life;
            String[] mPictures;
            if (mLife.getPicturePath() != null){
                mPictures = mLife.getPicturePath().split("&");
                switch (mPictures.length){
                    default:
                        mMoreTextView.setText("more...");
                    case 3:
                        mImageView_3.setImageBitmap(PictureUtils.getScaledBitmap(mPictures[2], getActivity()));
                    case 2:
                        mImageView_2.setImageBitmap(PictureUtils.getScaledBitmap(mPictures[1], getActivity()));
                    case 1:
                        mImageView_1.setImageBitmap(PictureUtils.getScaledBitmap(mPictures[0], getActivity()));
                    case 0:break;
                }
                mPictures = null;
            }
            mTitleTextView.setText(life.getTitle());
            mFeelTextView.setText(life.getFeel());
            mDateTextView.setText(life.getDate().toString());
        }
    }

    private class LifeAdapter extends RecyclerView.Adapter<LifeHolder>{

        private List<Life> mLifes;
        public LifeAdapter(List<Life> lifes) {
            mLifes = lifes;
        }

        @Override
        public void onBindViewHolder(LifeHolder holder, int position) {
            Life life = mLifes.get(position);
            holder.bindLife(life);
        }

        @Override
        public LifeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item, parent, false);
            return new LifeHolder(view);
        }

        @Override
        public int getItemCount() {
            return mLifes.size();
        }

        public void setLife(List<Life> lifes){
            mLifes = lifes;
        }

    }

    private void updateUI(){
        LifeLab lifeLab = LifeLab.get(getActivity());
        List<Life> lifes = lifeLab.getLifes();
        if (mAdapter == null){
            mAdapter = new LifeAdapter(lifes);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.setLife(lifes);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
