package com.gersion.multiselecter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.gersion.library.fragment.SelectionFragment;
import com.gersion.library.multitype.typepool.MultiBeanListPool;

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
        MultiBeanListPool typePool = new MultiBeanListPool();
        typePool.register(UserBean.class,R.layout.item_user);
        SelectionFragment selectionFragment = new SelectionFragment.Builder(this, new UserAdapter(), typePool).build();
        List<UserBean> items = new ArrayList<>();
        for (int i=0;i<100;i++){
            UserBean bean = new UserBean();
            bean.age = i;
            bean.userName = "名称 "+i;
            if (i%7==0){
               bean.icon = R.mipmap.ic_lock_purple_200_24dp;
            }else if (i%5==0){
                bean.icon = R.mipmap.ic_access_time_purple_200_24dp;
            }else if (i%3==0){
                bean.icon = R.mipmap.ic_archive_purple_200_24dp;
            }else if (i%2==0){
                bean.icon = R.mipmap.ic_assignment_turned_in_purple_200_24dp;
            }else {
                bean.icon = R.mipmap.ic_backup_purple_200_24dp;
            }
            items.add(bean);
        }
        selectionFragment.handleData(items);
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
