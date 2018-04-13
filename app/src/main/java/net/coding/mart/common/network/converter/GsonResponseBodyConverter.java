/*
 * Copyright (C) 2015 Square, Inc.
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
package net.coding.mart.common.network.converter;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.orhanobut.logger.Logger;

import net.coding.mart.common.Global;
import net.coding.mart.json.SimpleHttpResult;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    public static final String TAG = Global.makeLogTag(GsonResponseBodyConverter.class);

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    /*
     * 当服务器返回的不是 json 格式，会在 convert 里面转换为 json 格式。
     */
    @Override
    public T convert(ResponseBody value) throws IOException {
        String buffer = value.string();
        JsonReader jsonReader = gson.newJsonReader(new StringReader(buffer));
        try {
            return adapter.read(jsonReader);
        } catch (Exception e) {
            int code = 0;

            if (e instanceof JsonParseException) {
                Logger.d(e);
            }

            String jsonReaderString = buffer;
            if (jsonReaderString.equalsIgnoreCase("false")) {
                code = SimpleHttpResult.codeFalse;
            }

            if (jsonReaderString.isEmpty()) { // 有些 api 会返回空字符串
                jsonReaderString = "\"\"";
            }

            String bodyContent = String.format("{\"code\":%s,\"data\":%s}", code, jsonReaderString);
            StringReader reader = new StringReader(bodyContent);
            jsonReader = gson.newJsonReader(reader);
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
