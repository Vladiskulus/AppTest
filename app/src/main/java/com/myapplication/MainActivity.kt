package com.myapplication

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity: AppCompatActivity(),View.OnClickListener{

    var wifi: TextView? = null
    var nfc: TextView? = null
    var gps: TextView? = null
    var battery: TextView? = null
    var airplane: TextView? = null
    var service_info: TextView? =null
    lateinit var message: Button
    lateinit var download: Button
    val SEND_MESSAGE: String = "my.action.SEND_MESSAGE"
    val TEXT_MESSAGE: String = "Academy"
    val sendMessage: SendMessage = SendMessage()
    val downloadLink: String = "TEST"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity: ","onCreate")
        setContentView(R.layout.activity_main)
        wifi = findViewById(R.id.wifi)
        nfc = findViewById(R.id.nfc)
        gps = findViewById(R.id.gps)
        battery = findViewById(R.id.battery)
        airplane = findViewById(R.id.airplane)
        message = findViewById(R.id.send_message)
        message.setOnClickListener(this)
        download = findViewById(R.id.download)
        download.setOnClickListener(this)
        service_info = findViewById(R.id.service_info)



        val airplanesMode = AirplanesModeReceiver()
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            Log.d("MainActivity: ","registerReceiver >>> Airplanes Mode")
            registerReceiver(airplanesMode, it)
        }
        val batteryReceiver = BatteryReceiver()
        IntentFilter(Intent.ACTION_BATTERY_CHANGED).also {
            Log.d("MainActivity: ","registerReceiver >>> Battery Receiver")
            registerReceiver(batteryReceiver, it)
        }
        val wifiReceiver = WifiReceiver()
        IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION).also {
            Log.d("MainActivity: ","registerReceiver >>> Wi-Fi Receiver")
            registerReceiver(wifiReceiver, it)
        }
        val gpsReceiver = GPSReceiver()
        IntentFilter(LocationManager.MODE_CHANGED_ACTION).also {
            Log.d("MainActivity: ","registerReceiver >>> GPS Receiver")
            registerReceiver(gpsReceiver,it)
        }
        val bluetoothReceiver = BluetoothReceiver()
        IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED).also {
            Log.d("MainActivity: ","registerReceiver >>> Bluetooth Receiver")
            registerReceiver(bluetoothReceiver,it)
        }
    }
    override fun onClick(view: View?) {
        Log.d("MainActivity: ","onClick")
        if (view?.id == R.id.send_message){
            Intent().also { 
                intent ->
                intent.action = SEND_MESSAGE
                intent.putExtra("com.myapplication", TEXT_MESSAGE)
                sendBroadcast(intent)
                Log.d("MainActivity: ","Send Message to Broadcast")
                IntentFilter(SEND_MESSAGE).also {
                    Log.d("MainActivity: ","registerReceiver >>> Send Message")
                    registerReceiver(sendMessage,it)
                }
            }
        }else if (view?.id == R.id.download){
            val intent = Intent(this, AppService::class.java).also {
                it.putExtra("Download_link", downloadLink)
                startService(it)
                service_info!!.text = downloadLink
            }

        }
    }

    private inner class AirplanesModeReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("AirplanesModeReceiver: ","onReceive")
            val isAirplaneModeEnabled = intent?.getBooleanExtra("state", false) ?:return
            if (isAirplaneModeEnabled){
                airplane!!.text ="Увімкнено режим політ"
                Toast.makeText(context, "Увімкнено режим політ", Toast.LENGTH_SHORT).show()
            }else{
                airplane!!.text ="Вимкнено режим політ"
                Toast.makeText(context, "Вимкнено режим політ", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private inner class BatteryReceiver:BroadcastReceiver(){
        override fun onReceive(c: Context?, i: Intent?) {
            Log.d("BatteryReceiver: ","onReceive")
            val level = i?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            battery!!.text = "$level%"
        }
    }
    private inner class WifiReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("WifiReceiver: ","onReceive")
           val wifiState = intent?.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
            if (wifiState == WifiManager.WIFI_STATE_ENABLED){
                wifi!!.text="WIFI підключено"
            }else if(wifiState == WifiManager.WIFI_STATE_DISABLED){
                wifi!!.text="WIFI виключено"
            }
        }
    }
    private inner class BluetoothReceiver:BroadcastReceiver() {
        override fun onReceive(c: Context?, intent: Intent) {
            Log.d("BluetoothReceiver: ","onReceive")
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                        Toast.makeText(c, "STATE_OFF", Toast.LENGTH_SHORT)
                        nfc!!.text = "Bluetooth виключено"
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        Toast.makeText(c, "STATE_TURNING_OFF", Toast.LENGTH_SHORT)
                        nfc!!.text = "Bluetooth виключається..."
                    }
                    BluetoothAdapter.STATE_ON -> {
                        Toast.makeText(c, "STATE_ON", Toast.LENGTH_SHORT)
                        nfc!!.text = "Bluetooth включено"
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> {
                        Toast.makeText(c, "STATE_TURNING_ON", Toast.LENGTH_SHORT)
                        nfc!!.text = "Bluetooth підключення..."
                    }
                }
            }
        }
    }
    private inner class GPSReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("GPSReceiver: ","onReceive")
                val locationManager =
                        context?.getSystemService(LOCATION_SERVICE) as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    gps!!.text = "GPS включен"
                else gps!!.text = "GPS выключен"
            }
        }
}


