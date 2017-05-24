package com.liucheng.android.mylife;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by liucheng on 2017/5/17.
 */

public class LifePageActivity extends AppCompatActivity{

    private EditText mLifeTitle;
    private EditText mLifeFeel;
    private GridView mGridView;
    private Button mSubmitButton;
    private PictureAdapter mPictureAdapter;

    private Life mLife;

    private static final int RESULT_PICK = 0;
    private static final String EXTRE_LIFE_ID = "com.liucheng.android.mylife.lifeid";

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, LifePageActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, UUID lifeId){
        Intent intent = new Intent(packageContext, LifePageActivity.class);
        intent.putExtra(EXTRE_LIFE_ID, lifeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_page);

        if (getIntent().hasExtra(EXTRE_LIFE_ID)){
            mLife = LifeLab.get(this).getLifeById((UUID) getIntent().getSerializableExtra(EXTRE_LIFE_ID));
        } else {
            mLife = new Life();
        }
        mLifeTitle = (EditText) findViewById(R.id.life_title);
        mLifeFeel = (EditText) findViewById(R.id.life_feel);
        if (mLife != null){
            mLifeTitle.setText(mLife.getTitle());
            mLifeFeel.setText(mLife.getFeel());
        }

        mLifeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mLife.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mLifeFeel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mLife.setFeel(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mGridView = (GridView) findViewById(R.id.life_picture_grid_view);

        updatePictures();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, RESULT_PICK);
                }
            }
        });

        mSubmitButton = (Button) findViewById(R.id.life_submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LifeLab.get(LifePageActivity.this).isHasLife(mLife)){
                    LifeLab.get(LifePageActivity.this).updateLife(mLife);
                    LifePageActivity.this.finish();
                }else {
                    LifeLab.get(LifePageActivity.this).addLife(mLife);
                    LifePageActivity.this.finish();
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == RESULT_PICK){
            if (data != null){
                Uri uri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = this.managedQuery(uri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                mLife.addPicturePath(cursor.getString(column_index));
                Log.i("photoPath", cursor.getString(column_index));
            }
        }
    }

    /**
     * 更新图片视图
     */
    private void updatePictures(){
        List<Bitmap> mBitmaps = new ArrayList<>();
        Bitmap addPic = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic);
        mBitmaps.add(addPic);
        if (mLife.getPicturePath() != null){
            String[] pictures = mLife.getPicturePath().split("&");
            for (int i = 0; i < pictures.length; i++){
                Bitmap bitmap = PictureUtils.getScaledBitmap(pictures[i], this);
                mBitmaps.add(bitmap);
            }
        }
        if (mPictureAdapter == null) {
            mPictureAdapter = new PictureAdapter(mBitmaps);
            mGridView.setAdapter(mPictureAdapter);
        } else {
            mPictureAdapter.setBitmaps(mBitmaps);
            mPictureAdapter.notifyDataSetChanged();
        }
    }

    private class PictureAdapter extends BaseAdapter {

        private List<Bitmap> mBitmaps;

        public PictureAdapter(List<Bitmap> bitmaps) {
            mBitmaps = bitmaps;
        }

        public void setBitmaps(List<Bitmap> bitmaps){
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

            PictureHolder holder;
            if (view == null){
                LayoutInflater mInflater = LayoutInflater.from(LifePageActivity.this);
                view = mInflater.inflate(R.layout.grid_view_item, viewGroup, false);
                holder = new PictureHolder();
                holder.mImageView = (ImageView) view.findViewById(R.id.grid_view_item_image_view);
                view.setTag(holder);
            } else {
                holder = (PictureHolder) view.getTag();
            }

            holder.mImageView.setImageBitmap(mBitmaps.get(i));

            return view;
        }
    }

    private class PictureHolder{
        private ImageView mImageView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePictures();
    }

}
