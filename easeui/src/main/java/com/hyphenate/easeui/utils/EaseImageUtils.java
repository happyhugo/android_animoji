/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.utils;

import android.widget.ImageView;

import com.hyphenate.easeui.R;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;

import java.util.Random;

public class EaseImageUtils extends com.hyphenate.util.ImageUtils{
	
	public static String getImagePath(String remoteUrl)
	{
		String imageName= remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1, remoteUrl.length());
		String path =PathUtil.getInstance().getImagePath()+"/"+ imageName;
        EMLog.d("msg", "image path:" + path);
        return path;
		
	}
	
	public static String getThumbnailImagePath(String thumbRemoteUrl) {
		String thumbImageName= thumbRemoteUrl.substring(thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
		String path =PathUtil.getInstance().getImagePath()+"/"+ "th"+thumbImageName;
        EMLog.d("msg", "thum image path:" + path);
        return path;
    }

	public synchronized static void displayAvatar(String name, ImageView avatarView, ImageView avatarView2) {
		avatarView.setImageResource(getRandomColor(name));
		avatarView2.setImageResource(getRandomDrawable(name));
	}

	private static final Random RANDOM = new Random();

	public static int getRandomColor(String username) {

		switch ((username.length()+username.charAt(1))% 6) {
			default:
			case 0:
				return android.R.color.holo_green_light;
			case 1:
				return android.R.color.holo_blue_bright;
			case 2:
				return android.R.color.holo_orange_light;
			case 3:
				return android.R.color.holo_red_light;
			case 4:
				return android.R.color.holo_purple;
			case 5:
				return android.R.color.darker_gray;
		}
	}

	public static int getRandomDrawable(String username) {
		switch ((username.length()+username.charAt(1))% 13) {
			default:
			case 0:
				return R.drawable.avatar_fa_1;
			case 1:
				return R.drawable.avatar_fa_2;
			case 2:
				return R.drawable.avatar_fa_3;
			case 3:
				return R.drawable.avatar_fa_4;
			case 4:
				return R.drawable.avatar_fa_5;
			case 5:
				return R.drawable.avatar_fa_6;
			case 7:
				return R.drawable.avatar_ma_1;
			case 8:
				return R.drawable.avatar_ma_2;
			case 9:
				return R.drawable.avatar_ma_3;
			case 10:
				return R.drawable.avatar_ma_4;
			case 11:
				return R.drawable.avatar_ma_5;
			case 12:
				return R.drawable.avatar_ma_6;
		}
	}
}
