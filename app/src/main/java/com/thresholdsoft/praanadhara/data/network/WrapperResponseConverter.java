package com.thresholdsoft.praanadhara.data.network;

import com.thresholdsoft.praanadhara.data.network.pojo.WrapperResponse;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created on : March 30, 2020
 * Author     : JAGADEESH
 */
public class WrapperResponseConverter<T>
        implements Converter<ResponseBody, T> {
    private Converter<ResponseBody, WrapperResponse<T>> converter;

    public WrapperResponseConverter(Converter<ResponseBody,
            WrapperResponse<T>> converter) {
        this.converter = converter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        WrapperResponse<T> response = converter.convert(value);
        if (response.getSuccess()) {
            return response.getData();
        }
        // RxJava will call onError with this exception
        throw new WrapperError(response.getMessage());
    }
}