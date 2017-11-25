package com.thanh.android.polyhackathon;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by stephen on 25/11/2017.
 */

public class ServerConnect {
    private Socket mSocket;
    {
        try{
//            mSocket = IO.socket("http://10.200.202.239:3000");
            mSocket = IO.socket("http://192.168.56.1:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket(){
        return mSocket;
    }
}
