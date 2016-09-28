package com.richrelevance.richrelevance;

import android.app.Application;

import com.richrelevance.ClientConfiguration;
import com.richrelevance.RRLog;
import com.richrelevance.RichRelevance;

import java.util.UUID;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // First create a configuration and use it to configure the default client.
        ClientConfiguration config = new ClientConfiguration("showcaseparent", "bccfa17d092268c0");
        config.setApiClientSecret("r5j50mlag06593401nd4kt734i");
        config.setUserId("RZTestUserTest");
        config.setSessionId(UUID.randomUUID().toString());

        RichRelevance.init(this, config);

        // Enable all logging
        RichRelevance.setLoggingLevel(RRLog.VERBOSE);
    }
}
