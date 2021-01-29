package fr.ismailkoksal.kotlin_iss

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import fr.ismailkoksal.kotlin_iss.models.IssLocation
import fr.ismailkoksal.kotlin_iss.services.IssService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {
    private val url = "http://api.open-notify.org/"
    private val contentType = "application/json".toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    private val service: IssService = retrofit.create(IssService::class.java)
    private val issRequest = service.getIssLocation()
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Configuration.getInstance().load(this, getDefaultSharedPreferences(this))

        map.setTileSource(TileSourceFactory.MAPNIK)

        val startPoint = GeoPoint(48.8583, 2.2944)
        map.controller.setCenter(startPoint)
        map.controller.setZoom(9.5)


        timer.schedule(0, 1000) {
            refreshIss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.show_astros -> {
            val intent = Intent(this, PeopleInSpaceActivity::class.java)
            this.startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun refreshIss() {
        issRequest.clone().enqueue(object : Callback<IssLocation> {
            override fun onResponse(call: Call<IssLocation>, response: Response<IssLocation>) {
                val iss = response.body()
                if (iss != null && iss.message == "success") {
                    updateIssPositionOnMap(
                        iss.issPosition.latitude.toDouble(),
                        iss.issPosition.longitude.toDouble()
                    )
                }
            }

            override fun onFailure(call: Call<IssLocation>, t: Throwable) {
                error("${t.message}")
            }
        })
    }

    private fun updateIssPositionOnMap(latitude: Double, longitude: Double) {
        val issPosition = GeoPoint(latitude, longitude)
        val issMarker = Marker(map)
        issMarker.icon = ContextCompat.getDrawable(this, R.drawable.ic_vector_iss)
        issMarker.position = issPosition
        map.overlayManager.clear()
        map.overlayManager.add(issMarker)
        map.controller.setCenter(issPosition)
    }
}