package fr.ismailkoksal.kotlin_iss

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import fr.ismailkoksal.kotlin_iss.entities.Astro
import fr.ismailkoksal.kotlin_iss.models.PeopleInSpace
import fr.ismailkoksal.kotlin_iss.models.Person
import fr.ismailkoksal.kotlin_iss.services.IssService
import kotlinx.android.synthetic.main.activity_people_in_space.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class PeopleInSpaceActivity : AppCompatActivity() {
    private val url = "http://api.open-notify.org/"
    private val contentType = "application/json".toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    private val service: IssService = retrofit.create(IssService::class.java)
    private val issRequest = service.getPeopleInSpace()
    private lateinit var astros: List<Astro>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people_in_space)

        initAstros().invokeOnCompletion {
            println(astros)

            runOnUiThread {
                val listItems = arrayOfNulls<String>(astros.size)
                for (i in astros.indices) {
                    val astro = astros[i]
                    listItems[i] = astro.name
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
                astros_list_view.adapter = adapter
            }
        }
    }

    private fun initAstros(): Job {
        return GlobalScope.launch {
            astros = DataInterface.getAll(this@PeopleInSpaceActivity)
            if (astros.isNullOrEmpty()) {
                fetchAstros()
                delay(5000)
            }
        }
    }

    private fun fetchAstros() {
        issRequest.enqueue(object : Callback<PeopleInSpace> {
            override fun onResponse(call: Call<PeopleInSpace>, response: Response<PeopleInSpace>) {
                val peopleInSpace = response.body()
                if (peopleInSpace != null && peopleInSpace.message == "success") {
                    GlobalScope.launch {
                        this@PeopleInSpaceActivity.astros = saveAstrosToDb(peopleInSpace.people)
                    }
                }
            }

            override fun onFailure(call: Call<PeopleInSpace>, t: Throwable) {
                error("${t.message}")
            }
        })
    }

    private suspend fun saveAstrosToDb(astros: List<Person>): List<Astro> {
        astros.forEach {
            DataInterface.addItem(
                this@PeopleInSpaceActivity,
                Astro(craft = it.craft, name = it.name)
            )
        }
        return DataInterface.getAll(this@PeopleInSpaceActivity)
    }
}