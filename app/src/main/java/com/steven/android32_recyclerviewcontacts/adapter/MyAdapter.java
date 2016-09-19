package com.steven.android32_recyclerviewcontacts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.squareup.picasso.Picasso;
import com.steven.android32_recyclerviewcontacts.R;
import com.steven.android32_recyclerviewcontacts.decoration.CircleTransformation;
import com.steven.android32_recyclerviewcontacts.decoration.MyItemAnimator;
import com.steven.android32_recyclerviewcontacts.model.GroupModel;
import com.steven.android32_recyclerviewcontacts.model.UserModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by StevenWang on 16/6/17.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context = null;
    private List<Object> list = null;
    private LayoutInflater mInflater = null;
    private RecyclerView recyclerView = null;
    private MyItemAnimator animator = null;
    public static final int GROUP = 0, CHILD = 1;
    private ImageLoader imageLoader = null;
    private DisplayImageOptions options = null;

    public MyAdapter(Context context, List<Object> list, RecyclerView recyclerView,
                     MyItemAnimator animator) {
        this.context = context;
        this.list = list;
        this.recyclerView = recyclerView;
        this.animator = animator;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                //.displayer(new FadeInBitmapDisplayer(500))
                .displayer(new RoundedBitmapDisplayer(45))
                .build();
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = list.get(position);
        if (obj instanceof GroupModel) {
            return GROUP;
        } else if (obj instanceof UserModel) {
            return CHILD;
        }
        return GROUP;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case GROUP:
                view = mInflater.inflate(R.layout.item_recyclerview_group, parent, false);

                //给item增加点击事件
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = recyclerView.getChildAdapterPosition(v);
                        GroupModel group = (GroupModel) list.get(position);
                        if (!group.isExpand()) {
                            group.setIsExpand(true);
                            list.addAll(position + 1, group.getUserModels());
                            animator.setXY(v.getLeft(), v.getTop());
                            group.setResId(android.R.drawable.arrow_up_float);
                            notifyItemRangeInserted(position, group.getUserModels().size());
                        } else {
                            group.setIsExpand(false);
                            list.removeAll(group.getUserModels());
                            animator.setXY(v.getLeft(), v.getTop());
                            group.setResId(android.R.drawable.arrow_down_float);
                            notifyItemRangeRemoved(position, group.getUserModels().size());
                        }
                    }
                });
                return new ViewHolderGroup(view);
            case CHILD:
                view = mInflater.inflate(R.layout.item_recyclerview_child, parent, false);
                return new ViewHolderChild(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderGroup) {
            GroupModel groupModel = (GroupModel) list.get(position);
            ((ViewHolderGroup) holder).textView_item_group.setText(groupModel.getFirstLetter());
            ((ViewHolderGroup) holder).imageView_item_icon.setImageResource(groupModel.getResId());
        } else if (holder instanceof ViewHolderChild) {
            UserModel userModel = (UserModel) list.get(position);
            ((ViewHolderChild) holder).textView_item_username.setText(userModel.getUsername());
            //异步加载图片
            //((ViewHolderChild) holder).imageView_item_icon.setImageDrawable();
            String urlString = userModel.getIconUrl();
            //            imageLoader.displayImage(urlString, ((ViewHolderChild) holder)
            // .imageView_item_icon,
            //                    options);

            Picasso.with(context)
                    .load(urlString)
                    //增加圆角遮罩
                    .transform(new CircleTransformation())
                    .into(((ViewHolderChild) holder)
                    .imageView_item_icon);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderChild extends RecyclerView.ViewHolder {
        @Bind(R.id.imageView_item_icon)
        ImageView imageView_item_icon;

        @Bind(R.id.textView_item_username)
        TextView textView_item_username;

        public ViewHolderChild(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ViewHolderGroup extends RecyclerView.ViewHolder {
        @Bind(R.id.textView_item_group)
        TextView textView_item_group;

        @Bind(R.id.imageView_item_icon)
        ImageView imageView_item_icon;

        public ViewHolderGroup(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}