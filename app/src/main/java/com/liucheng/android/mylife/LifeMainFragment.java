package com.liucheng.android.mylife;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by liucheng on 2017/5/23.
 */

public class LifeMainFragment extends Fragment {

    private TextView mLifeTitle;
    private TextView mLifeFeel;
    private GridView mGridView;
    private TextView mLifeDate;
    private Button mDeleteButton;
    private Button mUpdateButton;

    private LifeMainFragment.PictureAdapter mPictureAdapter;

    private Life mLife;

    private static final String ARG_Life_ID = "life_id";

    public static LifeMainFragment newInstance(UUID lifeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_Life_ID, lifeId);

        LifeMainFragment lifeMainFragment = new LifeMainFragment();
        lifeMainFragment.setArguments(args);
        return lifeMainFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID lifeId = (UUID) getArguments().getSerializable(ARG_Life_ID);
        mLife = LifeLab.get(getActivity()).getLifeById(lifeId);
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_life_main, container, false);
        mLifeTitle = (TextView) view.findViewById(R.id.main_life_title);
        mLifeTitle.setText(mLife.getTitle());
        mLifeFeel = (TextView) view.findViewById(R.id.main_life_feel);
        mLifeFeel.setText(mLife.getFeel());

        mGridView = (GridView) view.findViewById(R.id.main_life_picture_grid_view);
        List<Bitmap> mBitmaps = new ArrayList<>();
        if (mLife.getPicturePath() != null){
            String[] pictures = mLife.getPicturePath().split("&");
            for (int i = 0; i < pictures.length; i++){
                Bitmap bitmap = PictureUtils.getScaledBitmap(pictures[i], getActivity());
                mBitmaps.add(bitmap);
            }
        }
        mPictureAdapter = new LifeMainFragment.PictureAdapter(mBitmaps);
        mGridView.setAdapter(mPictureAdapter);

        mLifeDate = (TextView) view.findViewById(R.id.main_life_date_text_view);
        mLifeDate.setText(mLife.getDate().toString());

        mDeleteButton = (Button) view.findViewById(R.id.main_life_delete);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LifeLab.get(getActivity()).deleteLife(mLife.getId());
                getActivity().finish();
            }
        });

        mUpdateButton = (Button) view.findViewById(R.id.main_life_update);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new LifePageActivity().newIntent(getActivity(), mLife.getId());
                startActivity(intent);
            }
        });

        return view;
    }

    private class PictureAdapter extends BaseAdapter{

        private List<Bitmap> mBitmaps;

        public PictureAdapter(List<Bitmap> bitmaps) {
            mBitmaps = bitmaps;
        }

        @Override
        public int getCount() {
            return mBitmaps.size();
        }

        @Override
        public Object getItem(int i) {
            return mBitmaps.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LifeMainFragment.PictureHolder holder;
            if (view == null){
                LayoutInflater mInflater = LayoutInflater.from(getActivity());
                view = mInflater.inflate(R.layout.grid_view_item, viewGroup, false);
                holder = new LifeMainFragment.PictureHolder();
                holder.mImageView = (ImageView) view.findViewById(R.id.grid_view_item_image_view);
                view.setTag(holder);
            } else {
                holder = (LifeMainFragment.PictureHolder) view.getTag();
            }

            holder.mImageView.setImageBitmap(mBitmaps.get(i));

            return view;
        }
    }

    private class PictureHolder{
        private ImageView mImageView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPictureAdapter.notifyDataSetChanged();
    }
}
