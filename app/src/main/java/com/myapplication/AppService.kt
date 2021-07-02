package com.myapplication

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class AppService: Service() {



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AppService","onStartCommand")
        val downloadLink = intent?.getStringExtra("Download_link")
        Log.d("AppService","getLink")
        intent?.setData(Uri.parse(downloadLink))
        //startService(intent)
        Log.d("AppService","start downloading")
        Toast.makeText(this, downloadLink,Toast.LENGTH_SHORT).show()
        Log.d("AppService","start Thread 5 sec")
        Thread.sleep(5000)
        Log.d("AppService","finish Thread")
        Toast.makeText(this, "Загрузка завершена",Toast.LENGTH_SHORT).show()
        return START_STICKY;
    }
    override fun onBind(p0: Intent?): IBinder? {
        Log.d("AppService", "OnBind")
        return null;
    }


}