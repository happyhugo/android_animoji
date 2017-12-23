/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wen.hugo.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.wen.hugo.bean.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void toast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static boolean filterException(Context ctx, Exception e) {
        if (e != null) {
            toast(ctx, e.getMessage());
            return false;
        } else {
            return true;
        }
    }

    private static List<Subject> lv  = new ArrayList();

    public static boolean addSubject(Subject subject){
        boolean add = true;
        for(Subject getData: lv){
            if(getData.getObjectId().equals(subject.getObjectId())){
                add = false;
                break;
            }
        }
        if(add){
            lv.add(subject);
            return true;
        }else{
            return false;
        }
    }

    public static List<Subject> getSubjects(){
        return lv;
    }
}
