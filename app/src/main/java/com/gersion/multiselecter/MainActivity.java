package com.gersion.multiselecter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.gersion.library.MultiSelecter;
import com.gersion.library.inter.Filter;
import com.gersion.library.view.MultiSelectView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        initRecycleView();

//        FragmentManager supportFragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
//        SelectionFragment fragment = new SelectionFragment();
//        MultiBeanListPool typePool = new MultiBeanListPool();
//        typePool.register(UserBean.class,R.layout.item_user);
//        fragment.setTypePool(typePool);
//        fragmentTransaction.add(R.id.container, fragment);
//        fragmentTransaction.commit();
        LinearLayout  container = (LinearLayout) findViewById(R.id.container);
//        MultiSelectView multiSelectView = (MultiSelectView) findViewById(R.id.multiSelectView);
//        MultiSelecter.mImageLoader = new GlideImageLoader();
//        MultiBeanListPool typePool = new MultiBeanListPool();
//        typePool.register(UserBean.class,R.layout.item_user);
//        typePool.register(TitleBean.class,R.layout.item_title);
//        multiSelectView.setAdapter(new UserAdapter());
//        multiSelectView.setTypePool(typePool);
//        multiSelectView.init();
//        SelectionFragment selectionFragment = new SelectionFragment.Builder(this, new UserAdapter(), typePool).build();
        MultiSelectView selectView = new MultiSelecter.Builder(this, container)
                .setImageLoader(new GlideImageLoader())
                .setMultiAdapter(new UserAdapter())
                .setSelectType(MultiSelecter.MULTI_SELECT)
                .register(UserBean.class, R.layout.item_user)
                .register(TitleBean.class, R.layout.item_title)
                .build();
        List<Filter> items = new ArrayList<>();
        for (int i=0;i<100;i++){
            if (i%13==0){
                TitleBean titleBean = new TitleBean();
                titleBean.title = "标题："+i;
                items.add(titleBean);
                continue;
            }
            UserBean bean = new UserBean();
            bean.age = i;
            bean.userName = "名称 "+i;
//            if (i%7==0){
//               bean.icon = R.mipmap.ic_lock_purple_200_24dp;
//            }else if (i%5==0){
//                bean.icon = R.mipmap.ic_access_time_purple_200_24dp;
//            }else if (i%3==0){
//                bean.icon = R.mipmap.ic_archive_purple_200_24dp;
//            }else if (i%2==0){
//                bean.icon = R.mipmap.ic_assignment_turned_in_purple_200_24dp;
//            }else {
//                bean.icon = R.mipmap.ic_backup_purple_200_24dp;
//            }

            if (i%7==0){
                bean.iconUrl = "http://www.qq1234.org/uploads/allimg/150420/100A5G63-1.jpg";
            }else if (i%5==0){
                bean.iconUrl = "http://www.jdxcjs.com/cimg/bd13698490.jpg";
            }else if (i%3==0){
                bean.iconUrl = "http://qq1234.org/uploads/allimg/140504/3_140504160752_2.jpg";
            }else if (i%2==0){
                bean.iconUrl = "http://img3.imgtn.bdimg.com/it/u=2124504335,2887169219&fm=27&gp=0.jpg";
            }else {
                bean.iconUrl = "http://www.qq1234.org/uploads/allimg/150420/100A52c6-0.jpg";
            }
            items.add(bean);
        }
        selectView.handleData(items);
    }

    private void initRecycleView() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(VERTICAL);
//        MultiTypeAdapter<UserBean,UserViewBinder.UserViewHolder> adapter = new MultiTypeAdapter<>();
//        adapter.register(UserBean.class,new UserViewBinder());
//        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mRecyclerView.setAdapter(adapter);
//        List<UserBean> items = new ArrayList<>();
//        for (int i=0;i<100;i++){
//            UserBean bean = new UserBean();
//            bean.age = i;
//            bean.userName = "名称 "+i;
//            items.add(bean);
//        }
//        adapter.setItems(items);

    }
}
