package com.steven.android32_recyclerviewcontacts.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.steven.android32_recyclerviewcontacts.R;
import com.steven.android32_recyclerviewcontacts.adapter.MyAdapter;
import com.steven.android32_recyclerviewcontacts.decoration.MyItemAnimator;
import com.steven.android32_recyclerviewcontacts.decoration.SpacesItemDecoration;
import com.steven.android32_recyclerviewcontacts.helper.ChineseToPinyinHelper;
import com.steven.android32_recyclerviewcontacts.model.GroupModel;
import com.steven.android32_recyclerviewcontacts.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private Context mContext = this;
    private List<Object> totalList = new ArrayList<>();
    private MyAdapter adater = null;

    // 获取数组资源中所有的数据，放在该集合中
    private List<Map<String, UserModel>> list = new ArrayList<Map<String, UserModel>>();
    // 获取不重复的首字母的集合
    private TreeSet<String> set_group = new TreeSet<String>();

    @Bind(R.id.recyclerView_main)
    RecyclerView recyclerView_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        createData();
        
        initView();
    }

    // 初始化数据，给set_parent和totalList集合赋值
    private void initData() {
        String[] arrUsernames = getResources().getStringArray(R.array.arrUsernames);
        String[] arrIconUrls = getResources().getStringArray(R.array.arrIconUrls);

        for (int i = 0; i < arrIconUrls.length; i++) {
            Map<String, UserModel> map = new HashMap<String, UserModel>();
            UserModel userModel = new UserModel();
            userModel.setUsername(arrUsernames[i]);
            userModel.setIconUrl(arrIconUrls[i]);
            String pinyin = ChineseToPinyinHelper.getInstance().getPinyin(
                    arrUsernames[i]);
            String firstLetter = pinyin.substring(0, 1).toUpperCase();
            userModel.setPinyin(pinyin);

            // 判断首字母是否属于A-Z
            // 汉字的正则表达式：[\u4e00-\u9fa5]
            if (firstLetter.matches("[A-Z]")) {
                userModel.setFirstLetter(firstLetter);
                map.put(firstLetter, userModel);
                set_group.add(firstLetter);
            } else {
                userModel.setFirstLetter("#");
                map.put("#", userModel);
                set_group.add("#");
            }
            list.add(map);
        }
    }

    // 将原始数据重组， 给list_parent和map_child集合赋值
    private void createData() {
        for (String str : set_group) {
            GroupModel groupModel = new GroupModel(str);
            totalList.add(groupModel);

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).containsKey(str)) {
                    groupModel.getUserModels().add(list.get(i).get(str));
                }
            }
        }
    }

    private void initView() {
        ButterKnife.bind(this);

        recyclerView_main.setHasFixedSize(true);
        recyclerView_main.addItemDecoration(new SpacesItemDecoration(5));
        MyItemAnimator animator = new MyItemAnimator();
        recyclerView_main.setItemAnimator(animator);

        GridLayoutManager manager = new GridLayoutManager(mContext , 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adater.getItemViewType(position) == MyAdapter.GROUP) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });

        recyclerView_main.setLayoutManager(manager);

        adater = new MyAdapter(mContext , totalList , recyclerView_main , animator);
        recyclerView_main.setAdapter(adater);



    }
}
