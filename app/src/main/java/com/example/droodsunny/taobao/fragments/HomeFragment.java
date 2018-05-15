package com.example.droodsunny.taobao.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.droodsunny.taobao.R;
import com.example.droodsunny.taobao.Unit.Goods;
import com.example.droodsunny.taobao.Unit.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HomeFragment extends Fragment {
    public HomeFragment(){}

    public static HomeFragment newInstance(ArrayList<Goods> list){
        HomeFragment homeFragment=new HomeFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelableArrayList("goods",list);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }
    private List<Goods> goodsList;
    private Bundle args;
    private View view;
    private List<String> bmobFiles;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Nullable
    @Override
    //不建议执行耗时操作
    //每次显示就执行
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("HomeFragment","onCreateView");
        args=getArguments();
        if (args != null) {
            goodsList=args.getParcelableArrayList("goods");
        }
        view=inflater.inflate(R.layout.activity_main,container,false);
       /*加载子项布局*/
        mSwipeRefreshLayout =view.findViewById(R.id.swiperefresh);
        bmobFiles=new ArrayList<>();
        mRecyclerView=view.findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerAdapter=new RecyclerAdapter(goodsList);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobQuery<Goods> bmobQuery=new BmobQuery<>();
                bmobQuery.findObjects(new FindListener<Goods>() {
                    @Override
                    public void done(List<Goods> list, BmobException e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if(e==null){
                            if(list!=null) {
                                goodsList.clear();
                                goodsList.addAll(list);
                                mRecyclerAdapter.notifyDataSetChanged();
                            }
                        }else {
                            Toast.makeText(getActivity(),"没有数据",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

       return view;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HomeFragment","onCreate");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("HomeFragment","onAttach");
    }

    @Override
    public void onStart() {
        Log.d("HomeFragment","onStart");
        super.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        BmobQuery<Goods> bmobQuery=new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> list, BmobException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if(e==null){
                    if(list!=null) {
                        goodsList.clear();
                        goodsList.addAll(list);
                        mRecyclerAdapter.notifyDataSetChanged();
                    }
                }else {
                    Toast.makeText(getActivity(),"没有数据",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("HomeFragment","onResume");
    }

    @Override
    public void onPause() {
        Log.d("HomeFragment","onPause");
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("HomeFragment","onDestroyView");
    }

    @Override
    public void onDetach() {
        Log.d("HomeFragment","onDetach");
        super.onDetach();
    }
}
