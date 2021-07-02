package com.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast


class SendMessage:BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("SendMessage: ","onReceive")
        Toast.makeText(context,intent?.getStringExtra("com.myapplication") ,Toast.LENGTH_SHORT).show()
    }
}