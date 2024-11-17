package com.example.talevoice

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.talevoice.data.DefaultTaleRepository
import com.example.talevoice.data.TaleRepository
import com.example.talevoice.data.source.local.TaleDatabase
import com.example.talevoice.data.source.server.TaleApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TaleApplication : Application() {

    private lateinit var mockWebServer: MockWebServer
    private val appDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val appScope: CoroutineScope = CoroutineScope(SupervisorJob() + appDispatcher)

    private val database: TaleDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            TaleDatabase::class.java,
            "tale_database"
        ).build()
    }

    lateinit var taleRepository: TaleRepository
        private set

    override fun onCreate() {
        super.onCreate()
        Log.d("TaleApplication","tale application on create")

        appScope.launch(appDispatcher) {
            mockWebServer = setupMockWebServer()

            val mockApiService: TaleApiService = Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TaleApiService::class.java)

            // Repository 초기화
            taleRepository = DefaultTaleRepository(
                localDataSource = database.taleDao(),
                networkApiService = mockApiService,
                dispatcher = Dispatchers.IO
            )
        }

    }

    private fun setupMockWebServer() : MockWebServer {
        mockWebServer = MockWebServer().apply {
            dispatcher = object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse {
                    return when (request.path) {
                        "/api/tales/list" -> MockResponse()
                            .setResponseCode(200)
                            .setBody("""
{"code":200,"message":"동화 리스트를 조회했습니다.","data":[{"id":"6738f9a86107590ff665e195","title":"라푼젤","version":1},{"id":"6738fa3c6107590ff665e198","title":"백설공주","version":1},{"id":"6738fabf6107590ff665e19b","title":"콩쥐 팥쥐","version":1},{"id":"6738fb086107590ff665e19e","title":"아기 돼지 3형제","version":1},{"id":"6738fb496107590ff665e1a1","title":"이상한 나라의 엘리스","version":1},{"id":"6738fba76107590ff665e1a4","title":"성냥팔이 소녀","version":1},{"id":"6738fbe26107590ff665e1a7","title":"토끼와 거북이","version":1},{"id":"6738fc176107590ff665e1ab","title":"여우와 두루미","version":1},{"id":"6738fc486107590ff665e1ae","title":"신데렐라","version":1}],"totla":9}
                            """.trimIndent())
                        "/api/tales/content/1" -> MockResponse()
                            .setResponseCode(200)
                            .setBody("""
                                {
  "status": "success",
  "data": {
    "taleId": "1",
    "title": "The Brave Little Tailor",
    "content": [
      {
        "pageNumber": 1,
        "text": "Once upon a time, there was a tailor who was very brave.",
        "illustrationUrl": "https://example.com/images/tailor_page1.jpg"
      },
      {
        "pageNumber": 2,
        "text": "One day, he defeated seven flies with one blow.",
        "illustrationUrl": "https://example.com/images/tailor_page2.jpg"
      },
      {
        "pageNumber": 3,
        "text": "He made a belt saying 'Seven at one blow'.",
        "illustrationUrl": "https://example.com/images/tailor_page3.jpg"
      },
      {
        "pageNumber": 4,
        "text": "The tailor set off on a journey to find adventures.",
        "illustrationUrl": "https://example.com/images/tailor_page4.jpg"
      },
      {
        "pageNumber": 5,
        "text": "He eventually became a king through his cleverness.",
        "illustrationUrl": "https://example.com/images/tailor_page5.jpg"
      }
    ]
  }
}

                            """.trimIndent())
                        "/api/tales/content/2" -> MockResponse()
                            .setResponseCode(200)
                            .setBody("""
                                {
  "status": "success",
  "data": {
    "taleId": "2",
    "title": "The Frog Prince",
    "content": [
      {
        "pageNumber": 1,
        "text": "Once upon a time, a princess lost her golden ball in a pond.",
        "illustrationUrl": "https://example.com/images/frog_page1.jpg"
      },
      {
        "pageNumber": 2,
        "text": "A frog offered to retrieve it in exchange for her friendship.",
        "illustrationUrl": "https://example.com/images/frog_page2.jpg"
      },
      {
        "pageNumber": 3,
        "text": "The princess reluctantly agreed, and the frog fetched the ball.",
        "illustrationUrl": "https://example.com/images/frog_page3.jpg"
      },
      {
        "pageNumber": 4,
        "text": "The frog came to the castle and asked to be let in.",
        "illustrationUrl": "https://example.com/images/frog_page4.jpg"
      },
      {
        "pageNumber": 5,
        "text": "Through the spell-breaking kiss, the frog turned into a prince.",
        "illustrationUrl": "https://example.com/images/frog_page5.jpg"
      }
    ]
  }
}
                            """.trimIndent())
                        else -> MockResponse()
                            .setResponseCode(404)
                            .setBody("""{"error": "Not Found"}""")
                    }
                }
            }
        }
        Log.d("TaleApplication", "MockWebServer started at ${mockWebServer.url("/")}")
        return mockWebServer
    }

    override fun onTerminate() {
        super.onTerminate()
        mockWebServer.shutdown()
        Log.d("TaleApplication", "MockWebServer stopped")
    }
}