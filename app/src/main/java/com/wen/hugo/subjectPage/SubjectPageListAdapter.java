package com.wen.hugo.subjectPage;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wen.hugo.R;
import com.wen.hugo.bean.Subject;

public class SubjectPageListAdapter extends BaseQuickAdapter<Subject, BaseViewHolder> {

    public SubjectPageListAdapter() {
        super(R.layout.subject_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Subject subject) {
        helper.setText(R.id.tv_subject,subject.getTitle());
        helper.setText(R.id.tv_name,"from : "+subject.getUsername());
    }
}
