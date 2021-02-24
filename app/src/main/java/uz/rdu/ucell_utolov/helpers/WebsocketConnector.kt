package uz.rdu.ucell_utolov.helpers

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.SimpleAdapter
import android.widget.Toast
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import ua.naiksoftware.stomp.dto.StompMessage
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.models.websocket.ChatMessage
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class WebsocketConnector(context: Context){


    var context = context


    private val TAG = "MainActivity"

    private val mDataSet: MutableList<String> = ArrayList()
        private var mStompClient: StompClient? = Stomp.over(
        Stomp.ConnectionProvider.OKHTTP,Constants.WEBSOCKETWS + "chat/websocket"
    )
    private val mAdapter: SimpleAdapter? = null
    private var compositeDisposable: CompositeDisposable? = null
    private val mTimeFormat: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    val LOGIN = "token"

    val PASSCODE = "passcode"

    fun init(){
        MainApplication.component.inject(this)
    }


    fun connectStomp(token: String, username: String) {
        val headers: MutableList<StompHeader> = ArrayList()
        headers.add(StompHeader(LOGIN, "$token"))

        //mStompClient!!.withClientHeartbeat(1000).withServerHeartbeat(1000)
        resetSubscriptions()
        val dispLifecycle = mStompClient!!.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lifecycleEvent: LifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> Log.i(
                        "STOMP Connection:",
                        "Stomp connection opened"
                    )
                    LifecycleEvent.Type.ERROR -> {
                        Log.e(TAG, lifecycleEvent.message.toString())
                        Log.e(TAG, "Stomp connection error", lifecycleEvent.exception)
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        resetSubscriptions()
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> Log.i(
                        "STOMP Connection:",
                        "Stomp failed server heartbeat"
                    )
                }
            }
        compositeDisposable?.add(dispLifecycle)

        // Receive greetings


        val dispTopic = mStompClient!!.topic("" +
                "/queue/messages")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage: StompMessage ->
                Log.d(TAG, "Received " + topicMessage.payload)
            }
            ) { throwable: Throwable? ->
                Log.e(
                    TAG,
                    "Error on subscribe topic",
                    throwable
                )
            }
        val userTopic = mStompClient!!.topic("/user/queue/messages")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.i("MESSAGE", "Subscribed")
                Toast.makeText(context, it.payload, Toast.LENGTH_LONG).show()
            }

        compositeDisposable?.add(dispTopic)
        compositeDisposable?.add(userTopic)
        mStompClient!!.connect(headers)
    }


    fun SendMessage(message:ChatMessage) {
        mStompClient?.send("/topic/chat", Gson().toJson(message))?.compose(applySchedulers())?.subscribe();
    }


    protected fun applySchedulers(): CompletableTransformer? {
        return CompletableTransformer { upstream: Completable ->
            upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun resetSubscriptions() {
        compositeDisposable?.dispose()
        compositeDisposable = CompositeDisposable()
    }
}