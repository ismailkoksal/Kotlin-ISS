package fr.ismailkoksal.kotlin_iss.services

import fr.ismailkoksal.kotlin_iss.models.IssLocation
import fr.ismailkoksal.kotlin_iss.models.PeopleInSpace
import retrofit2.Call
import retrofit2.http.GET

interface IssService {
    @GET("/iss-now")
    fun getIssLocation(): Call<IssLocation>

    @GET("/astros")
    fun getPeopleInSpace(): Call<PeopleInSpace>
}