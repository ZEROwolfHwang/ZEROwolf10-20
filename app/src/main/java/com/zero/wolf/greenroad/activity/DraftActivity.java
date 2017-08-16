package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.DraftPhotoAdapter;
import com.zero.wolf.greenroad.adapter.PreviewPhotoAdapter;
import com.zero.wolf.greenroad.adapter.RecycleViewDivider;
import com.zero.wolf.greenroad.bean.SortPreviewTime;
import com.zero.wolf.greenroad.litepalbean.SupportDraft;
import com.zero.wolf.greenroad.litepalbean.SupportPhotoLite;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.FileUtils;
import com.zero.wolf.greenroad.tools.ImageProcessor;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.TimeUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DraftActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.toolbar_draft)
    Toolbar mToolbarPreview;
    @BindView(R.id.draft_photo_recycler_view)
    RecyclerView mDraftPhotoRecyclerView;
    @BindView(R.id.recycler_view_preview)
    RecyclerView mRecyclerViewPreview;

    private DraftActivity mActivity;
    private Context mContext;
    private List<SupportDraft> mPhotoList;

    private PreviewPhotoAdapter mAdapter;
    private File mGoodsFile;
    private String mGoodsFilePath;
    private String mFilePath;


    private DraftPhotoAdapter mPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        ButterKnife.bind(this);

        mActivity = this;
        mContext = this;

        getIntentData();

        initToolbar();
        initData();
        initView();
        initPhotoRecycler();
    }

    private void initPhotoRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mDraftPhotoRecyclerView.setLayoutManager(manager);
        // enterSureActivity(GlobalManager.ENTERTYPE_PHOTO);
        for (int i = 0; i < mPhotoList.size(); i++) {
            Logger.i(mPhotoList.get(i).getDraftTime());
            if (mPhotoList.get(i).getBitmapPaths() != null) {

            Logger.i(mPhotoList.get(i).getBitmapPaths().toString());
            }
        }
        mPhotoAdapter = new DraftPhotoAdapter(getContext(), mPhotoList.get(0).getBitmapPaths(), () -> {
            // enterSureActivity(GlobalManager.ENTERTYPE_PHOTO);
        });
        mDraftPhotoRecyclerView.setAdapter(mPhotoAdapter);

    }



    private void initView() {

        LinearLayoutManager manager = new LinearLayoutManager(mActivity,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerViewPreview.setLayoutManager(manager);

        mRecyclerViewPreview.addItemDecoration(new RecycleViewDivider(mContext,
                LinearLayoutManager.HORIZONTAL, 10, Color.WHITE));

        mAdapter = new PreviewPhotoAdapter(mContext, mActivity, (ArrayList<SupportDraft>) mPhotoList, () -> {

                    ToastUtils.singleToast("点击了条目");

                });

        mRecyclerViewPreview.setAdapter(mAdapter);
    }


    private void initData() {
        mPhotoList = DataSupport.findAll(SupportDraft.class);

        for (int i = 0; i < mPhotoList.size(); i++) {
            Logger.i("------------"+mPhotoList.get(i).toString());

        }
        SortPreviewTime sortPreviewTime = new SortPreviewTime();

        Collections.sort(mPhotoList, sortPreviewTime);
        for (int i = 0; i < mPhotoList.size(); i++) {
            Logger.i("++++++++++++"+mPhotoList.get(i).toString());
        }

       /* mPreviewList = new ArrayList<>();
        for (int i = 0; i < mPhotoList.size(); i++) {
            SupportDraft photoLite = mPhotoList.get(i);

            for (int j = 0; j < 5; j++) {

                SerializablePreview preview = new SerializablePreview();
                preview.setCar_number(photoLite.getLicense_plate());
                preview.setCar_goods(photoLite.getGoods());
                preview.setIsPost(photoLite.getIsPost());
                preview.setShutTime(photoLite.getShutTime());
                preview.setStation(photoLite.getStation());
                preview.setOperator(photoLite.getOperator());
                preview.setColor(photoLite.getLicense_color());
                preview.setPhotoPath1(photoLite.getPhotoPath1());
                preview.setPhotoPath2(photoLite.getPhotoPath2());
                preview.setPhotoPath3(photoLite.getPhotoPath3());

                mPreviewList.add(preview);
            }
            Logger.i(mPreviewList.get(i).getShutTime());
        }*/


    }

    private void initToolbar() {

        setSupportActionBar(mToolbarPreview);


        TextView title_text_view = ActionBarTool.getInstance(mActivity, 990).getTitle_text_view();
        title_text_view.setText("草稿文件");

        mToolbarPreview.setNavigationIcon(R.drawable.back_up_logo);
        mToolbarPreview.setNavigationOnClickListener(v -> finish());
    }

    /**
     * 创建并传递数据
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DraftActivity.class);
       /* Bundle bundle = new Bundle();
        bundle.putParcelable(MDRAFT_CONFIG, configInfoBean);
        bundle.putParcelable(MAIN2DRAFT_DETAIL, detailInfoBean);*/

        //bundle.putParcelableArrayList("myBitmapList", (ArrayList<? extends Parcelable>) myBitmaps);
        // intent.putExtras(bundle);
        //      intent.setType(type);
        context.startActivity(intent);
    }

    /**
     * 得到从上一个activity中拿到的数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
      /*  Bundle bundle = intent.getExtras();
        mConfigInfoBean = bundle.getParcelable(MAIN2DRAFT_CONFIG);
        mDetailInfoBean = bundle.getParcelable(MAIN2DRAFT_DETAIL);
        Logger.i(mDetailInfoBean.toString());
        Logger.i(mConfigInfoBean.toString());*/

    }

    /**
     * @param photoPath 得到缩小的Bitmap
     * @return
     */
    private Bitmap getBitmap(String photoPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);

        ImageProcessor processor = new ImageProcessor(bitmap);
        return processor.scale((float) 0.25);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // ACache.get(this).put("preview", (ArrayList<SerializableNumber>) mNumberList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_preview_7:
                ToastUtils.singleToast("清除七天前记录");
                deleteInfos(7);
                break;
            case R.id.delete_preview_15:
                ToastUtils.singleToast("清除15天前记录");
                deleteInfos(15);

                break;
            case R.id.delete_preview_30:
                ToastUtils.singleToast("清除30天前记录");
                deleteInfos(30);
                break;
            case R.id.delete_preview_all:
                ToastUtils.singleToast("清空所有记录");
                deleteAllInfos();
                break;


            default:
                break;
        }
        return true;
    }

    /**
     * 清除所有的记录
     */
    private void deleteAllInfos() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("清空本地保存的拍摄数据");
        dialog.setMessage("点击“确定”将删除所有拍摄记录" + "\"" +
                "点击“取消”将取消删除操作");
        dialog.setCancelable(false);
        dialog.setPositiveButton(getString(R.string.dialog_messge_OK), (dialog1, which) -> {

            DataSupport.deleteAll(SupportPhotoLite.class);
            if (mGoodsFile == null) {
                mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                mGoodsFile = new File(mFilePath, "GreenShoot");
                mGoodsFile.mkdirs();
            }
            mGoodsFilePath = mGoodsFile.getPath();

            FileUtils.deleteJpg(new File(mGoodsFilePath));
            //顺便将计数清空，进行重新计数
            SPUtils.cancel_count(getApplicationContext(), SPUtils.CAR_COUNT);
            SPUtils.cancel_count(getApplicationContext(), SPUtils.CAR_NOT_COUNT);

            onResume();
        });
        dialog.setNegativeButton(getString(R.string.dialog_message_Cancel), (dialog1, which) -> {
            dialog1.dismiss();
        });
        dialog.show();
    }


    /**
     * 清除几天之前的记录
     */
    private void deleteInfos(int day) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("清除" + day + "天之前本地保存的拍摄数据");
        dialog.setMessage("点击“确定”将删除" + day + "天以前的拍摄记录" + "\"" +
                "点击“取消”将取消删除操作");
        dialog.setCancelable(false);
        dialog.setPositiveButton(getString(R.string.dialog_messge_OK), (dialog1, which) -> {
            String currentTimeToDate = TimeUtil.getCurrentTimeToDate();
            List<SupportPhotoLite> photoLiteList = DataSupport.findAll(SupportPhotoLite.class);

            for (int i = 0; i < photoLiteList.size(); i++) {
                String shutTime = photoLiteList.get(i).getShutTime();
                int dayGap = TimeUtil.differentDaysByMillisecond(shutTime, currentTimeToDate);
                if (dayGap > day) {
                    DataSupport.deleteAll(SupportPhotoLite.class, "shutTime = ?", shutTime);
                    //删除三张本地照片
                    FileUtils.deleteJpgPreview(photoLiteList.get(i).getPhotoPath1());
                    FileUtils.deleteJpgPreview(photoLiteList.get(i).getPhotoPath2());
                    FileUtils.deleteJpgPreview(photoLiteList.get(i).getPhotoPath3());
                }
            }
            onResume();
        });
        dialog.setNegativeButton(getString(R.string.dialog_message_Cancel), (dialog1, which) -> {
            dialog1.dismiss();
        });
        dialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preview_item_car_number:

                break;

            default:
                break;
        }
    }
}
