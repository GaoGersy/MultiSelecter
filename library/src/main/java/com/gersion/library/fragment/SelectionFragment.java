package com.gersion.library.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gersion.library.R;
import com.gersion.library.adapter.BaseMultiAdapter;
import com.gersion.library.adapter.SelectIconRvAdapter;
import com.gersion.library.bean.FloatImgBean;
import com.gersion.library.inter.Filter;
import com.gersion.library.inter.OnItemClickListener;
import com.gersion.library.multitype.typepool.TypePool;
import com.gersion.library.smartrecycleview.SmartRecycleView;
import com.gersion.library.utils.ScreenUtils;
import com.gersion.library.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gersy on 2017/7/25.
 */

public class SelectionFragment extends Fragment {
    private Activity activity;
    private View mView;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_selection, container, false);
        initView();
        initListener();
        return mView;
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

//        int[] location = new int[2];
//        mContainer.getLocationOnScreen(location);
//        mStartX = location[0];
//        mStartY = location[1];
        FloatImgBean bean1 = new FloatImgBean();
        bean1.mImageView = mFloatImg_1;
        FloatImgBean bean2 = new FloatImgBean();
        bean2.mImageView = mFloatImg_2;
        FloatImgBean bean3 = new FloatImgBean();
        bean3.mImageView = mFloatImg_3;
        mImagePool.add(bean1);
        mImagePool.add(bean2);
        mImagePool.add(bean3);

        int screenWidth = ScreenUtils.getScreenWidth(activity);
        mMaxWidth = screenWidth * 2 / 3;
        initRecyclerView();
    }

    private FloatImgBean getFloatImg(){
        for (FloatImgBean bean : mImagePool) {
            if (!bean.mIsAnimator){
                return bean;
            }
        }
        return null;
    }

    private void initRecyclerView() {
        mIconListRvAdapter = new SelectIconRvAdapter();
        mLinearLayoutManager = new LinearLayoutManager(activity);
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

    public void setTypePool(TypePool typePool){
        this.mTypePool = typePool;
    }
    public void setAdapter(BaseMultiAdapter adapter){
        this.mAdapter = adapter;
    }

    private void getParentPoint() {
        int[] locations = new int[2];
        mIconRecyclerView.getLocationOnScreen(locations);
        mStartX = locations[0];
        mStartY = locations[1];
    }

    int count = 0;

    public void getSourcePoint(final View itemView, final Filter item) {
        getParentPoint();
//        mFloatImg_1.setVisibility(View.VISIBLE);
        itemView.setClickable(false);
        final FloatImgBean floatImg = getFloatImg();
        floatImg.mImageView.setVisibility(View.VISIBLE);
        mPlaceHolder.setVisibility(View.VISIBLE);
        floatImg.mImageView.setImageResource(item.getImageResource());
        floatImg.mIsAnimator = true;
        int[] targetCoordinates = getTarget(mIconRecyclerView, count++);
        Log.d("haha", "调用结果 x = " + targetCoordinates[0] + ", y = " + targetCoordinates[1]);

        int[] sourceLocation = new int[2];
        mSourceView.getLocationOnScreen(sourceLocation);
        int startX = sourceLocation[0];
        int startY = sourceLocation[1];

//        ViewGroup parent = (ViewGroup) mSourceView.getParent();
//        parent.removeView(mSourceView);
//        mFlContainer.addView(mSourceView);

        int[] tagetLocation = new int[2];
        mIconRecyclerView.getLocationOnScreen(tagetLocation);
        int endX = tagetLocation[0] + mIconRecyclerView.getWidth() + SizeUtils.dp2px(activity, 5);
        int endY = tagetLocation[1] + SizeUtils.dp2px(activity, 5) * 2;
//        if (endX >= mMaxWidth) {
//            endX = mMaxWidth - SizeUtils.dp2px(activity, 40);
//        }

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
//                mIconListRvAdapter.showLastItem();
                mIconListRvAdapter.add(item);
                mIconRecyclerView.smoothScrollToPosition(mIconListRvAdapter.getItemCount());
                mPlaceHolder.setVisibility(View.GONE);
                refreshLayout(false);
                itemView.setClickable(true);
            }
        });
    }

    private void animate(RecyclerView sourceRecycler, RecyclerView targetRecycler, int position) {
        View view = sourceRecycler.getLayoutManager().findViewByPosition(position);
        if (view == null) {
            return;
        }
        getParentPoint();

        mFloatImg_1.setPivotX(mFloatImg_1.getWidth() / 2);
        mFloatImg_1.setPivotY(mFloatImg_1.getHeight() / 2);
        mFloatImg_1.setVisibility(View.VISIBLE);
        int[] initial = new int[2];
        view.getLocationOnScreen(initial);

        sourceRecycler.getLayoutManager().removeViewAt(position);

        BaseMultiAdapter sourceRecyclerAdapter = (BaseMultiAdapter) sourceRecycler.getAdapter();
        Filter removedItem = sourceRecyclerAdapter.getItem(position);

        int width = view.getWidth();
//        view.removeFromParent();
//        parent.addView(view)
//        view.layoutParams = view.layoutParams.apply {
//            this.width = width
//        }
        int[] container = new int[2];
        sourceRecycler.getLocationOnScreen(container);

        view.setTranslationX(initial[0] + 0.5f);
        view.setTranslationY((initial[1] - container[1]) + 0.5f);

//        @Suppress("UNCHECKED_CAST")
        SelectIconRvAdapter adapter = (SelectIconRvAdapter) targetRecycler.getAdapter();
        int newPos = adapter.add(removedItem);
        int[] targetCoordinates = getTarget(targetRecycler, newPos);


        float targetX = (targetCoordinates[0] - initial[0]) + 0.5f;
        float targetY = (targetCoordinates[1] - initial[1]) + 0.5f;
        long duration = calcDuration(targetX, targetY);

        animateTranslation(mFloatImg_1, targetX, targetY, duration);

//        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mFloatImg_1, "translationX", initial[0], targetCoordinates[0]);
//        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mFloatImg_1, "translationY", initial[1] - mStartY, targetCoordinates[1] - mStartY);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(animatorX, animatorY);
//        animatorSet.setDuration(500);
//        animatorSet.start();

//        animateAlpha(removedItem, targetRecycler, view, duration)
//        animateTranslation(view, deltaX = targetX, deltaY = targetY, duration = duration)
    }

    private void animateTranslation(View view, float deltaX, float deltaY, Long duration) {
        view.animate().setDuration(duration)
                .setInterpolator(new OvershootInterpolator(1.1f))
                .translationXBy(deltaX)
                .translationYBy(deltaY)
                .start();
    }

    private long calcDuration(float targetX, float targetY) {
        return (long) (Math.sqrt((targetX * targetX + targetY * targetY)) *0.7f);
    }

    private int[] getTarget(RecyclerView targetRecycler, int index) {
        int prev = Math.max(0, index - 0);
        RecyclerView.ViewHolder viewHolderForAdapterPosition = targetRecycler.findViewHolderForAdapterPosition(prev);
        View targetView = viewHolderForAdapterPosition == null ? null : viewHolderForAdapterPosition.itemView;
        if (targetView == null) {
            viewHolderForAdapterPosition = targetRecycler.findViewHolderForAdapterPosition(prev - 1);
            targetView = viewHolderForAdapterPosition == null ? null : viewHolderForAdapterPosition.itemView;
            if (targetView != null) {
                int[] targetCoordinates = new int[2];
                targetView.getLocationOnScreen(targetCoordinates);
                targetCoordinates[1] += targetView.getHeight();
                Log.d("haha", "targetView !=null x = " + targetCoordinates[0] + ", y = " + targetCoordinates[1]);
                return targetCoordinates;
            }
        }

        if (targetView == null) {
            int[] targetCoordinates = new int[2];
            targetRecycler.getLocationOnScreen(targetCoordinates);
            if (targetRecycler.getChildCount() != 0) {
                targetCoordinates[1] += targetRecycler.getHeight();
            }
            Log.d("haha", "targetView == null x = " + targetCoordinates[0] + ", y = " + targetCoordinates[1]);
            return targetCoordinates;
        }

        return new int[]{0, 0};
    }


    public void handleData(List data) {
        mData = data;
        if (mSmartRecycleView == null) {
            return;
        }
        mSmartRecycleView.handleData(data);
    }

    private void initListener() {
//        mIconListRvAdapter.setOnLastItemLoadListenr(new OnLastItemLoadListener() {
//            @Override
//            public void onLastLoaded(View v) {
//                getSourcePoint(v);
//            }
//        });
        mLlSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIvSelectAll.isSelected()) {
                    mIconListRvAdapter.addAllData(mAdapter.getSelectionList());
                    mIconRecyclerView.smoothScrollToPosition(mAdapter.getSelectionList().size());
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
                if (isSelected) {
//                    mIconListRvAdapter.addHiddenItem(item);
//                    mIconRecyclerView.smoothScrollToPosition(mIconListRvAdapter.getItemCount());
                    mSourceView = v.findViewById(R.id.iv_icon);
                    getSourcePoint(v,item);

                } else {
                    mIconListRvAdapter.remove(item);
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
                if (trim.length()==0){
                    mAdapter.setItems(mData);
                }else {
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
        int width = SizeUtils.dp2px(activity, 45) * size;
        if (width > mMaxWidth) {
            width = mMaxWidth;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        mIconRecyclerView.setLayoutParams(params);
        mContainer.requestLayout();
    }

    public static class Builder{
        private FragmentActivity mActivity;
        private BaseMultiAdapter mMultiAdapter;
        private TypePool mTypePool;

        public Builder(FragmentActivity activity, BaseMultiAdapter multiAdapter, TypePool typePool){
            mActivity = activity;
            mMultiAdapter = multiAdapter;
            mTypePool = typePool;
        }
        public SelectionFragment build(){
            SelectionFragment selectionFragment = new SelectionFragment();
            FragmentManager supportFragmentManager = mActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, selectionFragment);
            fragmentTransaction.commit();
            selectionFragment.setTypePool(mTypePool);
            selectionFragment.setAdapter(mMultiAdapter);
            return selectionFragment;
        }
    }

}
