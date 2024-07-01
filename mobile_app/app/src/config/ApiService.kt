package com.alice.rodexapp.config

import com.alice.rodexapp.response.AddStoryResponse
import com.alice.rodexapp.response.DetailStoryResponse
import com.alice.rodexapp.response.InspectionDetailResponse
import com.alice.rodexapp.response.LoginResponse
import com.alice.rodexapp.response.RegistResponse
import com.alice.rodexapp.response.StartInspectionResponse
import com.alice.rodexapp.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val data: List<InferenceResult>
)
data class InferenceResult(
    val xcenter: Float,
    val ycenter: Float,
    val width: Float,
    val height: Float,
    val `class`: Int,
    val name: String,
    val confidence: Float
)
interface ApiService {

    @Multipart
    @POST("v1/predict/MnRcxWacBneU22cjBj3a")
    fun sendImageForInference(
        @Part image: MultipartBody.Part,
        @Part("size") size: Int,
        @Part("confidence") confidence: Float,
        @Part("iou") iou: Float,
        @Header("x-api-key") apiKey: String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegistResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("start_inspection")
    suspend fun startInspection(
        @Part("name_of_office") nameOfOffice: String,
        @Part("name_of_road") nameOfRoad: String,
        @Part("length_of_road") lengthOfRoad: Int,
        @Part("type_of_road_surface") typeOfRoadSurface: String,
        @Part("location_start") locationStart: RequestBody
    ): StartInspectionResponse

    @Multipart
    @POST("inspection_detail")
    suspend fun inspectionDetail(
        @Part("name_of_office") nameOfOffice: String,
        @Part("name_of_road") nameOfRoad: String,
        @Part("length_of_road") lengthOfRoad: Int,
        @Part("type_of_road_surface") typeOfRoadSurface: String,
        @Part("location_start") locationStart: RequestBody
    ): InspectionDetailResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(@Path("id") id: String): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): AddStoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImageWithLocation(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody
    ): AddStoryResponse
}
