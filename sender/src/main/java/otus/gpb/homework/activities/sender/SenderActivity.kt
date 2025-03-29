package otus.gpb.homework.activities.sender

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import otus.gpb.homework.activities.receiver.R
import otus.gpb.homework.activities.receiver.databinding.ActivitySenderBinding

class SenderActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    private val binding by lazy {
        ActivitySenderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.buttonToGoogleMaps.setOnClickListener() {
            checkPermissions()
        }
        binding.buttonSendEmail.setOnClickListener() {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, "android@otus.ru")
                putExtra(Intent.EXTRA_SUBJECT, "activity2")
                putExtra(Intent.EXTRA_TEXT, "Привет, я мобильный разработчик")
            }
            startActivity(intent)
        }
        binding.buttonOpenReceiver.setOnClickListener() {
            binding.buttonOpenReceiver.setOnClickListener() {
                val intent = Intent(Intent.ACTION_SEND)
                intent.apply {
                    putExtra("title", "Interstellar")
                    putExtra("year", "2014")
                    putExtra(
                        "description",
                        "Когда засуха, пыльные бури и вымирание растений приводят человечество к продовольственному кризису, коллектив исследователей и учёных отправляется сквозь червоточину (которая предположительно соединяет области пространства-времени через большое расстояние) в путешествие, чтобы превзойти прежние ограничения для космических путешествий человека и найти планету с подходящими для человечества условиями."
                    )
                }
                startActivity(intent)
            }
        }

    }
    private fun getCurrentLocation(): Location?{
        var currentLocation: Location? = null
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.lastLocation
            .addOnSuccessListener() { location ->
                if(location != null) {
                    currentLocation = location
                } else {
                    currentLocation = null
                }
            }.addOnFailureListener() {
                currentLocation = null
            }
        return currentLocation
    }

    private fun checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            currentLocation = getCurrentLocation()
            sendIntent()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ), 1)
        }
    }

    private fun sendIntent(){
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:${currentLocation?.latitude},${currentLocation?.longitude}?q=Рестораны")
        ).setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            currentLocation = getCurrentLocation()
            sendIntent()
        }
    }
}