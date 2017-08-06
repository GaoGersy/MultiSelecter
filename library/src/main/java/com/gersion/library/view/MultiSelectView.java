package com.gersion.library.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gersion.library.MultiSelecter;
import com.gersion.library.R;
import com.gersion.library.adapter.BaseMultiAdapter;
import com.gersion.library.adapter.SelectIconRvAdapter;
import com.gersion.library.bean.FloatImgBean;
import com.gersion.library.inter.Filter;
import com.gersion.library.inter.OnItemClickListener;
import com.gersion.library.multitype.typepool.TypePool;
import com.gersion.library.utils.ScreenUtils;
import com.gersion.library.utils.SizeUtils;
import com.gersion.library.view.smartrecycleview.SmartRecycleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gersy on 2017/8/6.
 */

public class MultiSelectView extends FrameLayout {

    private LinearLayout mContainer;
    private RecyclerView mIconRecyclerView;
    private EditText mEtSearch;
    private SmartRecycleView mSmartRecycleView;
    private LinearLayout mLlSelectAll;
    private ImageView mIvSelectAll;
    private SelectIconRvAdapter mIconListRvAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Filter> mSelectList;
    private TextView mTvConfirm;
    private int mMaxWidth;
    private BaseMultiAdapter mAdapter;
    private List<Filter> mData;
    private ImageView mFloatImg_1;
    private int mStartX;
    private int mStartY;
    private FrameLayout mFlContainer;
    private View mSourceView;
    private TypePool mTypePool;
    private ImageView mFloatImg_2;
    private ImageView mFloatImg_3;
    private List<FloatImgBean> mImagePool = new ArrayList<>();
    private List<Filter> mMatchList = new ArrayList<>();
    private View mPlaceHolder;
    private int mSelectType;
    private Context mContext;
    private View mView;
    private int mItemWidth;
    private int mWidth5;

    public MultiSelectView(@NonNull Context context) {
        this(context, null);
    }

    public MultiSelectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.fragment_selection, this);
    }

    public void init() {
        initView();
        initData();
        initListener();
    }

    private void initData() {
        mItemWidth = SizeUtils.dp2px(mContext, 45);
        mWidth5 = SizeUtils.dp2px(mContext, 5);
    }

    protected <T extends View> T findView(int id) {
        return (T) mView.findViewById(id);
    }

    private void initView() {
        mContainer = findView(R.id.container);
        mIconRecyclerView = findView(R.id.icon_recyclerView);
        mEtSearch = findView(R.id.et_search);
        mSmartRecycleView = findView(R.id.smartRecycleView);
        mLlSelectAll = findView(R.id.ll_select_all);
        mIvSelectAll = findView(R.id.iv_select_all);
        mTvConfirm = findView(R.id.tv_confirm);
        mFloatImg_1 = findView(R.id.float_img_1);
        mFloatImg_2 = findView(R.id.float_img_2);
        mFloatImg_3 = findView(R.id.float_img_3);
        mFlContainer = findView(R.id.fl_container);
        mPlaceHolder = findView(R.id.placeholder);

        initFloatPool();

        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        mMaxWidth = screenWidth * 2 / 3;
        initRecyclerView();
    }

    private void initFloatPool() {
        FloatImgBean bean1 = new FloatImgBean();
        bean1.mImageView = mFloatImg_1;
        FloatImgBean bean2 = new FloatImgBean();
        bean2.mImageView = mFloatImg_2;
        FloatImgBean bean3 = new FloatImgBean();
        bean3.mImageView = mFloatImg_3;
        mImagePool.add(bean1);
        mImagePool.add(bean2);
        mImagePool.add(bean3);
    }

    private FloatImgBean getFloatImg() {
        for (FloatImgBean bean : mImagePool) {
            if (!bean.mIsAnimator) {
                return bean;
            }
        }
        return null;
    }

    private void initRecyclerView() {
        mIconListRvAdapter = new SelectIconRvAdapter();
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setAutoMeasureEnabled(true);

        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mIconRecyclerView.setLayoutManager(mLinearLayoutManager);
        mIconRecyclerView.setAdapter(mIconListRvAdapter);

        mSelectList = mIconListRvAdapter.getList();

        mAdapter.setTypePool(mTypePool);
        mSmartRecycleView.setFirstPage(1)
                .setAutoRefresh(false)
                .setPageSize(20)
                .setAdapter(mAdapter)
                .loadMoreEnable(false)
                .refreshEnable(false)
                .setLayoutManger(SmartRecycleView.LayoutManagerType.LINEAR_LAYOUT);

        mSmartRecycleView.handleData(mData);

    }

    public void setTypePool(TypePool typePool) {
        this.mTypePool = typePool;
    }

    public void setAdapter(BaseMultiAdapter adapter) {
        this.mAdapter = adapter;
    }

    private void getParentPoint() {
        int[] locations = new int[2];
        mIconRecyclerView.getLocationOnScreen(locations);
        mStartX = locations[0];
        mStartY = locations[1];
    }

    public void getSourcePoint(final View itemView, final Filter item) {
        getParentPoint();
        itemView.setClickable(false);
        final FloatImgBean floatImg = getFloatImg();
        floatImg.mImageView.setVisibility(View.VISIBLE);
        mPlaceHolder.setVisibility(View.VISIBLE);
        floatImg.mImageView.setImageResource(item.getImageResource());
        MultiSelecter.mImageLoader.showImage(mContext, item.getImageUrl(), floatImg.mImageView);
        floatImg.mIsAnimator = true;

        int[] sourceLocation = new int[2];
        mSourceView.getLocationOnScreen(sourceLocation);
        int startX = sourceLocation[0];
        int startY = sourceLocation[1];

        int[] tagetLocation = new int[2];
        mIconRecyclerView.getLocationOnScreen(tagetLocation);
        int endX = tagetLocation[0] + mIconRecyclerView.getWidth() + mWidth5;

        int endY = tagetLocation[1] + mWidth5 * 2;

        animator(itemView, item, floatImg, startX, startY, endX, endY);
    }

    private void animator(final View itemView, final Filter item, final FloatImgBean floatImg, int startX, int startY, int endX, int endY) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(floatImg.mImageView, "translationX", startX - mStartX, endX - mStartX);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(floatImg.mImageView, "translationY", startY - mStartY, endY - mStartY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX, animatorY);
        animatorSet.setDuration(calcDuration(startX - endX, startY - endY));
        animatorSet.setInterpolator(new OvershootInterpolator(1.1f));
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                floatImg.mImageView.setVisibility(View.GONE);
                floatImg.mIsAnimator = false;
                mIconListRvAdapter.add(item);
                mIconRecyclerView.smoothScrollToPosition(mIconListRvAdapter.getItemCount());
                mPlaceHolder.setVisibility(View.GONE);
                refreshLayout(false);
                itemView.setClickable(true);
            }
        });
    }


    private long calcDuration(float targetX, float targetY) {
        return (long) (Math.sqrt((targetX * targetX + targetY * targetY)) * 0.7f);
    }

    public void handleData(List data) {
        mData = data;
        if (mSmartRecycleView == null) {
            return;
        }
        mSmartRecycleView.handleData(data);
    }

    private void initListener() {
        mLlSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIvSelectAll.isSelected()) {
                    List selectionList = mAdapter.getSelectionList();
                    mIconListRvAdapter.addAllData(selectionList);
                } else {
                    mIconListRvAdapter.clear();
                }
                mAdapter.changeAllDataStatus(!mIvSelectAll.isSelected());
                refreshLayout(!mIvSelectAll.isSelected());
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, Filter item, boolean isSelected) {
                if (mSelectType == MultiSelecter.MULTI_SELECT) {
                    if (isSelected) {
                        mSourceView = v.findViewById(R.id.iv_icon);
                        getSourcePoint(v, item);

                    } else {
                        mIconListRvAdapter.remove(item);
                    }
                } else {
                    if (isSelected) {
                        mIconListRvAdapter.addHiddenItem(item);
                        mIconRecyclerView.smoothScrollToPosition(mIconListRvAdapter.getItemCount());
                    } else {
                        mIconListRvAdapter.remove(item);
                    }
                }
                refreshLayout(false);
            }
        });

        mIconListRvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, Filter item, boolean isSelected) {
                mAdapter.updateItem(item);
                mIconListRvAdapter.remove(item);
                refreshLayout(false);
            }
        });

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String trim = mEtSearch.getText().toString().trim();
                if (trim.length() == 0) {
                    mAdapter.setItems(mData);
                } else {
                    mMatchList.clear();
                    for (Filter item : mData) {
                        if (item.isMatch(trim)) {
                            mMatchList.add(item);
                        }
                    }
                    mAdapter.setItems(mMatchList);
                }
            }
        });
    }

    private void refreshLayout(boolean isSelected) {
        mIvSelectAll.setSelected(isSelected);

        if (mAdapter.getSelectionList().size() == mIconListRvAdapter.getItemCount()) {
            mIvSelectAll.setSelected(true);
        }

        int size = mSelectList.size();
        mTvConfirm.setText(size == 0 ? "确定" : "确定(" + size + ")");
        if (size == 0) {
            mTvConfirm.setEnabled(false);
        } else {
            mTvConfirm.setEnabled(true);
        }

        int width = mItemWidth * size;
        if (width > mMaxWidth) {
            width = mMaxWidth;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        mIconRecyclerView.setLayoutParams(params);
        mContainer.requestLayout();
    }

    public List getSelectResult(){
        return mIconListRvAdapter.getList();
    }

    public void setSelectType(int selectType) {
        mSelectType = selectType;
        mAdapter.setSelectType(selectType);
    }

}
