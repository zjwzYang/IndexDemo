package com.example.yangjie.indexdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * 。
 *
 * @author yangjie 2017/12/22 新建.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<IndexBean> dataList;
    private LayoutInflater inflater;

    public MyAdapter(Context context, List<IndexBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        IndexBean indexBean = dataList.get(position);
        holder.mLetterV.setText(indexBean.getFirstLetter());
        holder.mNameV.setText(indexBean.getName());
        if (position == getItemCount() - 1) {
            holder.mDiviV.setVisibility(View.VISIBLE);
        } else {
            if (indexBean.getFirstLetter().equals(dataList.get(position + 1).getFirstLetter())) {
                if (holder.mDiviV.getVisibility() == View.GONE) {
                    holder.mDiviV.setVisibility(View.VISIBLE);
                }
            } else {
                if (holder.mDiviV.getVisibility() == View.VISIBLE) {
                    holder.mDiviV.setVisibility(View.GONE);
                }
            }
        }
        if (position == 0) {
            holder.mLetterV.setVisibility(View.VISIBLE);
        } else {
            if (indexBean.getFirstLetter().equals(dataList.get(position - 1).getFirstLetter())) {
                if (holder.mLetterV.getVisibility() == View.VISIBLE) {
                    holder.mLetterV.setVisibility(View.GONE);
                }
            } else {
                if (holder.mLetterV.getVisibility() == View.GONE) {
                    holder.mLetterV.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public IndexBean getItem(int index) {
        return dataList.get(index);
    }

    /**
     * 当前item和下面的item的首字母是否不相同。
     *
     * @param position 当前item
     * @return true:不相同 false:相同
     */
    public boolean isDiffIndex(int position) {
        if (dataList.get(position).getFirstLetter().equals(dataList.get(position + 1).getFirstLetter())) {
            return false;
        } else {
            return true;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mLetterV;
        TextView mNameV;
        View mDiviV;

        public MyViewHolder(View itemView) {
            super(itemView);
            mLetterV = itemView.findViewById(R.id.item_letter);
            mNameV = itemView.findViewById(R.id.item_name);
            mDiviV = itemView.findViewById(R.id.item_divi);
        }
    }
}
