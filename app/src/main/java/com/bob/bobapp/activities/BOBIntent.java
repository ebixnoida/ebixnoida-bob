package com.bob.bobapp.activities;
import android.content.Context;
import android.content.Intent;

public class BOBIntent  extends Intent {

    private static final String ARG_CUSTOMER_ID = "arg_customer_id";

    private static final String ARG_SESSION_ID = "arg_login_session_id";

    private static final String ARG_HEARTBEAT_TOKEN = "arg_heartbeat_token";

    private static final String ARG_CHANNEL_ID = "arg_channel_id";

    public BOBIntent(Context ctx) {

        super(ctx, BOBActivity.class);
    }

    public BOBIntent(Intent intent) {

        super(intent);
    }

    public void setData(String customerId, String sessionId, String heartbeatToken, String channelId) {

        putExtra(ARG_CUSTOMER_ID, customerId);

        putExtra(ARG_SESSION_ID, sessionId);

        putExtra(ARG_HEARTBEAT_TOKEN, heartbeatToken);

        putExtra(ARG_CHANNEL_ID, channelId);
    }

    public String getCustomerId() {

        return getStringExtra(ARG_CUSTOMER_ID);
    }

    public String getLoginSessionId() {

        return getStringExtra(ARG_SESSION_ID);
    }

    public String getHeartbeatToken() {

        return getStringExtra(ARG_HEARTBEAT_TOKEN);
    }

    public String getChannelId() {

        return getStringExtra(ARG_CHANNEL_ID);
    }
}