package com.music.vivi

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

/**
 * Search requests from other apps that must not bring up a new screen: the X7 car launcher shows Vivi inside one of its
 * panes (an ActivityView on its own display), and starting [MainActivity] from outside would open a second, full-screen
 * window instead of playing in that pane.
 *
 * [ExternalSearchReceiver] hands the request to the running [MainActivity] (any display, any task) through [requests];
 * the activity then searches and plays exactly as for an activity intent ([MainActivity.ACTION_PLAY_FROM_SEARCH]).
 */
object ExternalSearch {
    private val _requests = MutableSharedFlow<Intent>(extraBufferCapacity = 4)
    val requests: SharedFlow<Intent> = _requests

    /** True between [MainActivity] onStart and onStop: its player connection is bound and it can take a request. */
    @Volatile
    var uiStarted = false

    /** Gives [intent] to the running UI; false when there is none, the caller then has to open the activity. */
    fun deliver(intent: Intent): Boolean = uiStarted && _requests.tryEmit(intent)
}

/**
 * Ordered broadcast `com.music.vivi.action.PLAY_FROM_SEARCH` / `SEARCH_QUERY` (same extras as the activity actions).
 * Result code [Activity.RESULT_OK] when the running UI took the request; 0 when Vivi's UI is not up, so the sender
 * should start the activity with the same intent.
 */
class ExternalSearchReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val handled = ExternalSearch.deliver(Intent(intent).setComponent(null))
        Timber.tag("ExternalSearch").i("broadcast ${intent.action}: ${if (handled) "handled by the running UI" else "no UI"}")
        if (isOrderedBroadcast) {
            resultCode = if (handled) Activity.RESULT_OK else 0
            resultData = if (handled) "handled" else "no_ui"
        }
    }
}
