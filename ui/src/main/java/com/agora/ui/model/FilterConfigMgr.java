package com.agora.ui.model;

import android.util.Log;

import com.blankj.utilcode.utils.FileUtils;
import com.google.gson.Gson;
import com.agora.tracker.bean.AGFilter;
import com.agora.tracker.common.Config;
import com.agora.ui.bean.FilterSetConfig;

import java.io.File;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by song.ding on 2017/3/6.
 */

public class FilterConfigMgr{

    /**
     * read filters from filters.json
     * @return fitlers
     */
    public static List<AGFilter> getFilters() {
        File file = new File(Config.getFilterConfigPath());
        String jsonStr = FileUtils.readFile2String(file, Config.UTF_8);
        FilterSetConfig filterSetConfig = new Gson().fromJson(jsonStr,FilterSetConfig.class);
        List<AGFilter> filters = filterSetConfig.getFilters();

        Log.d(TAG,"read filters from filters.json,size:"+filters.size());
        return filters;
    }
}
