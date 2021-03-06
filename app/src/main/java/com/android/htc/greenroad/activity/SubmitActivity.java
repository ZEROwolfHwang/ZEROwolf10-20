package com.android.htc.greenroad.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.htc.greenroad.R;
import com.android.htc.greenroad.adapter.PreviewItemAdapter;
import com.android.htc.greenroad.adapter.RecycleViewDivider;
import com.android.htc.greenroad.helper.DeleteHelper;
import com.android.htc.greenroad.helper.SortTime;
import com.android.htc.greenroad.litepalbean.SupportDraftOrSubmit;
import com.android.htc.greenroad.manager.GlobalManager;
import com.android.htc.greenroad.tools.ActionBarTool;
import com.android.htc.greenroad.tools.ImageProcessor;
import com.android.htc.greenroad.tools.SPUtils;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubmitActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.toolbar_draft)
    Toolbar mToolbarPreview;
    @BindView(R.id.recycler_view_preview)
    RecyclerView mRecyclerViewPreview;

    private SubmitActivity mActivity;
    private Context mContext;
    private List<SupportDraftOrSubmit> mSubmitList;
    private PreviewItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        ButterKnife.bind(this);

        mActivity = this;
        mContext = this;

        initToolbar();
        initData();
        initView();
    }


    private void initView() {

        LinearLayoutManager manager = new LinearLayoutManager(mActivity,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerViewPreview.setLayoutManager(manager);

        mRecyclerViewPreview.addItemDecoration(new RecycleViewDivider(mContext,
                LinearLayoutManager.HORIZONTAL, 10, Color.WHITE));

        mAdapter = new PreviewItemAdapter(mContext, mActivity,
                (ArrayList<SupportDraftOrSubmit>) mSubmitList, (support) -> {
            PreviewDetailActivity.actionStart(mContext, support,
                    PreviewDetailActivity.ACTION_SUBMIT_ITEM);
        },(itemView, lite_ID, position) -> {

        });

        mRecyclerViewPreview.setAdapter(mAdapter);
    }


    private void initData() {
        mSubmitList = DataSupport.
                where(GlobalManager.LITE_CONDITION,GlobalManager.TYPE_SUBMIT_LITE).find(SupportDraftOrSubmit.class);

        for (int i = 0; i < mSubmitList.size(); i++) {
            Logger.i("------------" + mSubmitList.get(i).toString());

        }
        SortTime sortDraftTime = new SortTime();

        Collections.sort(mSubmitList, sortDraftTime);
        for (int i = 0; i < mSubmitList.size(); i++) {
            Logger.i("++++++++++++" + mSubmitList.get(i).toString());
        }
    }

    private void initToolbar() {

        setSupportActionBar(mToolbarPreview);

        TextView title_text_view = ActionBarTool.getInstance(mActivity, 992).getTitle_text_view();
        title_text_view.setText("提交列表");

        mToolbarPreview.setNavigationIcon(R.drawable.back_up_logo);
        mToolbarPreview.setNavigationOnClickListener(v -> finish());
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
                DeleteHelper.getInstance().deleteInfos(this,GlobalManager.TYPE_SUBMIT_LITE,SPUtils.MATH_SUBMIT_LITE,7,supportList -> {
                    mAdapter.updateListView(supportList);
                });
                break;
            case R.id.delete_preview_15:
                DeleteHelper.getInstance().deleteInfos(this,GlobalManager.TYPE_SUBMIT_LITE,SPUtils.MATH_SUBMIT_LITE,15,supportList -> {
                    mAdapter.updateListView(supportList);
                });
                break;
            case R.id.delete_preview_30:
                DeleteHelper.getInstance().deleteInfos(this,GlobalManager.TYPE_SUBMIT_LITE,SPUtils.MATH_SUBMIT_LITE,30,supportList -> {
                    mAdapter.updateListView(supportList);
                });
                break;
            case R.id.delete_preview_all:
                DeleteHelper.getInstance().deleteAllInfos(mContext, GlobalManager.TYPE_SUBMIT_LITE, SPUtils.MATH_SUBMIT_LITE,supportList -> {
                    mAdapter.updateListView(supportList);
                });
                break;

            default:
                break;
        }
        return true;
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
