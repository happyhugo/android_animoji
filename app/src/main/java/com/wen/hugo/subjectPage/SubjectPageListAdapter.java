package com.wen.hugo.subjectPage;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wen.hugo.R;
import com.wen.hugo.bean.Subject;
import com.wen.hugo.util.ActivityUtils;

public class SubjectPageListAdapter extends BaseQuickAdapter<Subject, BaseViewHolder> {

    public SubjectPageListAdapter() {
        super(R.layout.subject_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Subject subject) {
        helper.setText(R.id.tv_subject,subject.getTitle());
        TextView textView = helper.getView(R.id.tv_name);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityUtils.addSubject(subject)) {
                    Toast.makeText(view.getContext(), "添加题目到题库成功", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(view.getContext(), "添加失败，已经添加该题目", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
