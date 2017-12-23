package com.wen.hugo.mySubject;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wen.hugo.R;
import com.wen.hugo.bean.Subject;

public class MySubjectListAdapter extends BaseQuickAdapter<Subject, BaseViewHolder> {

    public MySubjectListAdapter() {
        super(R.layout.subject_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Subject subject) {
        helper.setText(R.id.tv_subject,subject.getTitle());
        helper.getView(R.id.tv_name).setVisibility(View.INVISIBLE);
    }
}

