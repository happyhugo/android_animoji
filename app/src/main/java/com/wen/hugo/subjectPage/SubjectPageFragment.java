package com.wen.hugo.subjectPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wen.hugo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.agora.openvcall.ui.MainActivity;

/**
 * Created by hugo on 11/22/17.
 */

public class SubjectPageFragment extends Fragment  {



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.subjectpage_frag, container, false);
        ButterKnife.bind(this,root);
        return root;
    }

    @OnClick(R.id.match)
    void match(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

}
