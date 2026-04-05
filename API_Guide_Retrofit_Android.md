# Retrofit for Android — Complete Guide

## 1. Overview

Retrofit is a widely used, type-safe HTTP client for Android created by Square (the same creators of OkHttp). It sits on top of OkHttp and simplifies networking by:

- Automatically converting JSON into Kotlin objects.
- Turning API endpoints into simple Kotlin interfaces.
- Handling complex request types (like Multipart files or secure Forms) easily.

---

## 2. Core Concept

The process of making a network request using Retrofit follows a clean, structured flow:

- Define Kotlin Data Classes (to hold the JSON data).
- Define an Interface (the API menu).
- Build the Retrofit Client (the engine).
- Run code in a specific Coroutine Scope.
- Execute the request and read the response.
- Update the UI.

---

## 3. Adding Retrofit Dependencies

Before using Retrofit, you need to add the core library and the JSON converter (Gson).

Open your `build.gradle (Module: app)` file and add:

```gradle
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
```

After adding the dependencies, click **Sync Now** in Android Studio.

---

## 4. Adding Internet Permission

To allow your app to access the internet, you must add permission in the `AndroidManifest.xml` file.

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

Place this above the `<application>` tag.

---

## 5. Defining the Data Models (Handling Nested JSON)

Retrofit automatically converts raw JSON from the server into Kotlin objects. To do this, you must build Data Classes that perfectly match the "shape" of the JSON.

### Understanding Nested JSON (The Russian Doll Concept)

Often, servers wrap data in multiple layers. For example, if a server returns:

```json
{
  "data": {
    "url": "https://example.com/image.jpg"
  }
}
```

You cannot just make one class. You must create nested classes to unwrap it layer by layer:

```kotlin
// The outer box matching { "data": ... }
data class MediaModel(val data: MediaData)

// The inner box matching { "url": "..." }
data class MediaData(val url: String)
```

When accessing the data later, you will unwrap it sequentially:

```kotlin
response.body()?.data?.url
```

---

## 6. Defining the API Interface

Specify the endpoints and HTTP methods. Use the `suspend` keyword so these functions can run safely in the background.

```kotlin
interface ApiInterface {
    // 1. A Simple GET Request
    @GET("/api/v1/users")
    suspend fun getUsers(): Response<List<UserModel>>

    // 2. A POST Request sending JSON
    @POST("/api/v1/create")
    suspend fun createUser(@Body user: UserModel): Response<UserModel>

    // 3. A POST Request uploading a file (Multipart)
    @Multipart
    @POST("/api/v1/upload")
    suspend fun uploadMedia(@Part media: MultipartBody.Part): Response<MediaModel>

    // 4. Secure POST Request with Forms & Headers (e.g., Stripe)
    @Headers("Authorization: Bearer YOUR_SECRET_KEY")
    @FormUrlEncoded
    @POST("/api/v1/payment")
    suspend fun makePayment(@Field("amount") amount: String): Response<PaymentResponse>
}
```

---

## 7. Creating the Retrofit Client

Create a Singleton object to handle the network requests. This ensures the app only builds the heavy Retrofit engine once.

```kotlin
object RetrofitClient {
    // IMPORTANT: Base URLs must always end with a trailing slash (/).
    // Endpoints in your interface should NOT start with a slash.
    private const val BASE_URL = "https://api.example.com/"

    val api: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Translates JSON to Kotlin
            .build()
            .create(ApiInterface::class.java)
    }
}
```

---

## 8. Running Network Calls (Choosing the Right Scope)

Network operations must not run on the main thread. We use Coroutines with `Dispatchers.IO`, but you must choose the correct Scope depending on where you are writing the code:

| Scope | Use When |
|---|---|
| `lifecycleScope` | Inside an Activity or Fragment. Auto-cancels when the screen closes. |
| `viewModelScope` | Inside a ViewModel. Survives screen rotations. *(Industry standard)* |
| `CoroutineScope` | In a Singleton Object or pure Kotlin class with no UI lifecycle. |

For `CoroutineScope`, define it manually:

```kotlin
val scope = CoroutineScope(Dispatchers.IO)
```

### Example inside an Activity

```kotlin
lifecycleScope.launch(Dispatchers.IO) {
    // Network code here
}
```

---

## 9. Executing the Request

Send the request using the interface you built.

```kotlin
val response = RetrofitClient.api.uploadMedia(filePart)
```

---

## 10. Validating the Response

Always check whether the server returned a successful HTTP code (200–299) and that the body is not empty.

```kotlin
if (response.isSuccessful && response.body() != null) {
    // Success! Unpack the nested JSON data:
    val finalUrl = response.body()!!.data.url
} else {
    // Handle server error (e.g., 404 Not Found)
}
```

---

## 11. Updating the UI

UI updates must be performed on the main thread.

```kotlin
withContext(Dispatchers.Main) {
    imageView.load(finalUrl) // Or update a TextView
}
```

---

## 12. Error Handling

Wrap the network call in a try-catch block to prevent app crashes if the user loses their internet connection during the request.

```kotlin
catch (e: Exception) {
    e.printStackTrace()
    withContext(Dispatchers.Main) {
        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
    }
}
```

---

## 13. Complete Example

Here is how it all looks put together inside an Activity:

```kotlin
lifecycleScope.launch(Dispatchers.IO) {
    try {
        // 1. Execute the Request
        val response = RetrofitClient.api.uploadMedia(filePart)

        // 2. Validate the Response
        if (response.isSuccessful && response.body() != null) {

            // 3. Extract the Nested JSON Data
            val finalUrl = response.body()!!.data.url

            // 4. Update the UI
            withContext(Dispatchers.Main) {
                textView.text = "Uploaded to: $finalUrl"
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Server Error: ${response.code()}", Toast.LENGTH_SHORT).show()
            }
        }

    } catch (e: Exception) {
        e.printStackTrace()

        // 5. Handle Network Failures
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Something went wrong. Check internet.", Toast.LENGTH_SHORT).show()
        }
    }
}
```

---

## 14. Best Practices

- Always run network calls on `Dispatchers.IO`.
- Always update the UI inside `withContext(Dispatchers.Main)`.
- Match your Kotlin Data Classes exactly to the JSON response structure.
- Use `viewModelScope` whenever possible to survive screen rotations.
- Handle both Server Errors (`!response.isSuccessful`) and Network Errors (`catch (e: Exception)`).

---

## 15. Summary

Retrofit is a powerful and efficient tool for making network requests in Android. By letting Retrofit handle the JSON translation and using Coroutines to manage background threads, you can write much cleaner, safer, and shorter networking code compared to raw OkHttp.
