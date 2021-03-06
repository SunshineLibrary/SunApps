package com.ssl.support.api;

import android.content.Context;
import com.ssl.support.config.Configurations;

/**
 * @author Bowen Sun
 * @version 1.0
 */
public class ApiClientFactory {

    public static ApiClient newApiClient(Context context) {
        Configurations configs = getConfigurations(context);
        return new ApiClient(context, configs);
    }

    private static Configurations getConfigurations(Context context) {
        return new Configurations(context);
    }
}
