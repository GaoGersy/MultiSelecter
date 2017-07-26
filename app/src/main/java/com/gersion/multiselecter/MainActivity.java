package com.gersion.multiselecter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

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

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        UserFragment fragment = new UserFragment();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.commit();
        List<UserBean> items = new ArrayList<>();
        for (int i=0;i<100;i++){
            UserBean bean = new UserBean();
            bean.age = i;
            bean.userName = "名称 "+i;
            items.add(bean);
        }
        fragment.handleData(items);
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
