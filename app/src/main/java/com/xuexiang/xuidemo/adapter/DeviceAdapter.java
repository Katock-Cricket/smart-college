/*
 * Copyright (C) 2023 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xuidemo.adapter;

import androidx.annotation.NonNull;

import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xuidemo.R;
import com.xuexiang.xuidemo.adapter.entity.Device;
import com.xuexiang.xutil.common.CollectionUtils;
import com.xuexiang.xutil.common.logger.Logger;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeviceAdapter extends BaseRecyclerAdapter<Device> {
    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_news_card_view_list_item;
    }

    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, int position, Device model) {
        if (model != null) {
            holder.text(R.id.tv_device_name, model.getDeviceName());
            holder.text(R.id.tv_position_tag, model.getPositionTag());
            holder.text(R.id.tv_info, model.getInfo());
            holder.checked(R.id.tv_switch, model.isOpen());
            holder.text(R.id.tv_device_level_name, model.getLevelName());
            holder.setLevel(R.id.tv_seek, model.getLevel());
            holder.image(R.id.iv_image, model.getImgUrl());

            holder.checkedListener(R.id.tv_switch, (compoundButton, b) -> {
                System.out.println(model.getDeviceName());
                System.out.println("changed: " + b);
            });

            holder.setXSeekBarListener(R.id.tv_seek, (xSeekBar, val) -> {
                System.out.println(model.getDeviceName());
                System.out.println("changed" + val);
            });
        }
    }

    public static String createJson(String id, String value) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("value", value);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendControl(String url, String jsonInput) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonInput);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        System.out.println("成功");
                    } else {
                        System.out.println("失败");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (CollectionUtils.isEmpty(payloads)) {
            Logger.e("正在进行全量刷新:" + position);
            onBindViewHolder(holder, position);
            return;
        }
//        // payloads为非空的情况，进行局部刷新
//
//        //取出我们在getChangePayload（）方法返回的bundle
//        Bundle payload = WidgetUtils.getChangePayload(payloads);
//        if (payload == null) {
//            return;
//        }
//
//        Logger.e("正在进行增量刷新:" + position);
//        NewInfo newInfo = getItem(position);
//        for (String key : payload.keySet()) {
//            switch (key) {
//                case DiffUtilCallback.PAYLOAD_USER_NAME:
//                    //这里可以用payload里的数据，不过newInfo也是新的 也可以用
//                    holder.text(R.id.tv_user_name, newInfo.getUserName());
//                    break;
//                case DiffUtilCallback.PAYLOAD_PRAISE:
//                    holder.text(R.id.tv_praise, payload.getInt(DiffUtilCallback.PAYLOAD_PRAISE) == 0 ? "点赞" : String.valueOf(payload.getInt(DiffUtilCallback.PAYLOAD_PRAISE)));
//                    break;
//                case DiffUtilCallback.PAYLOAD_COMMENT:
//                    holder.text(R.id.tv_comment, payload.getInt(DiffUtilCallback.PAYLOAD_COMMENT) == 0 ? "评论" : String.valueOf(payload.getInt(DiffUtilCallback.PAYLOAD_COMMENT)));
//                    break;
//                case DiffUtilCallback.PAYLOAD_READ_NUMBER:
//                    holder.text(R.id.tv_read, "阅读量 " + payload.getInt(DiffUtilCallback.PAYLOAD_READ_NUMBER));
//                    break;
//                default:
//                    break;
//            }
//        }
    }

}
