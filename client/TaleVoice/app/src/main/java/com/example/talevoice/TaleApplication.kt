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
{"code":200,"message":"동화 내용을 조회했습니다.","data":{"title":"라푼젤","version":1,"context":["옛날 옛적, 한 부부가 아이를 간절히 원하고 있었어요. 마침내 아내가 아이를 임신하게 되었고, 그들은 큰 기쁨에 휩싸였죠. 아내는 집 옆 마녀의 정원에 있는 신비한 채소인 라푼젤을 보고는 그것이 너무 먹고 싶어졌어요. 남편은 아내의 소원을 들어주기 위해 몰래 마녀의 정원에 들어가 라푼젤을 가져오게 됩니다. 하지만 그만 마녀에게 들키고 말았죠.","마녀는 남편에게 라푼젤을 가져간 대가로 아이가 태어나면 자신에게 넘기라는 조건을 내걸었어요. 부부는 어쩔 수 없이 그 제안을 받아들였고, 얼마 후 아기가 태어나자 마녀는 그 아이를 데려가 ‘라푼젤’이라는 이름을 지어주며 높은 탑에 가두었어요. 라푼젤은 자라면서 긴 금발 머리를 가지게 되었고, 그 탑에서 외롭게 살았어요.","어느 날, 왕자 한 명이 라푼젤이 노래 부르는 소리를 듣고 탑 근처로 오게 되었어요. 그는 라푼젤의 노랫소리에 반해 매일 탑을 찾아갔고, 마침내 그녀가 창문에서 자신의 머리카락을 내려주면 탑에 오를 수 있다는 것을 알게 되었죠. 라푼젤과 왕자는 탑에서 몰래 만나며 서로 사랑에 빠지게 되었어요.","하지만 이 사실을 마녀가 알게 되었고, 라푼젤을 더 먼 곳으로 보내버렸어요. 그리고 왕자를 잡아 그의 눈을 멀게 만들었죠. 왕자는 라푼젤을 잃은 슬픔에 눈이 멀고, 오랫동안 그녀를 찾아 헤맸어요.","마침내 왕자는 라푼젤을 찾아냈고, 그녀의 눈물 덕분에 시력을 되찾았어요. 둘은 다시 만나 행복하게 살게 되었고, 이제는 더 이상 마녀의 위협 없이 자유롭게 사랑을 나눌 수 있게 되었답니다."],"image":["./image/sample1.jpg","./image/sample2.jpg","./image/sample3.jpg","./image/sample4.jpg","./image/sample5.jpg"]}}
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