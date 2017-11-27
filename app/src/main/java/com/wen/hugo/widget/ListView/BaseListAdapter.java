package com.wen.hugo.widget.ListView;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


public class BaseListAdapter<T> extends BaseAdapter {
  protected Context ctx;
  protected LayoutInflater inflater;
  public List<T> datas = new ArrayList<T>();

  public BaseListAdapter(Context ctx) {
    initWithContext(ctx);
  }

  public void initWithContext(Context ctx) {
    this.ctx = ctx;
    inflater = LayoutInflater.from(ctx);
  }

  public BaseListAdapter(Context ctx, List<T> datas) {
    initWithContext(ctx);
    this.datas = datas;
  }

  public void setDatas(List<T> datas) {
    this.datas = datas;
  }


  public List<T> getDatas() {
    return datas;
  }

  public void addAll(List<T> subDatas) {
    datas.addAll(subDatas);
    notifyDataSetChanged();
  }

  public void add(T object) {
    datas.add(object);
    notifyDataSetChanged();
  }

  public void remove(int position) {
    datas.remove(position);
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return datas.size();
  }

  @Override
  public Object getItem(int position) {
    return datas.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return null;
  }

  public void clear() {
    datas.clear();
    notifyDataSetChanged();
  }

  public <T extends View> T findViewById(View view, int id) {
      SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
      if (viewHolder == null) {
        viewHolder = new SparseArray<View>();
        view.setTag(viewHolder);
      }
      View childView = viewHolder.get(id);
      if (childView == null) {
        childView = view.findViewById(id);
        viewHolder.put(id, childView);
      }
      return (T) childView;
  }
}
