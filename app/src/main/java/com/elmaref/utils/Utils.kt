package com.quranscreen.utils


import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.elmaref.ui.app.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }

fun Activity.io(runnable:suspend ()->Unit){
    CoroutineScope(Dispatchers.IO).launch {
        runnable.invoke()
    }
}
fun MyApplication.io(runnable:suspend ()->Unit){
    CoroutineScope(Dispatchers.IO).launch {
        runnable.invoke()
    }
}
fun MyApplication.main(runnable:suspend ()->Unit){
    CoroutineScope(Dispatchers.Main).launch {
        runnable.invoke()
    }
}
fun Fragment.io(runnable:suspend ()->Unit){
    CoroutineScope(Dispatchers.IO).launch {
        runnable.invoke()
    }
}
fun Activity.main(runnable:suspend ()->Unit){
    CoroutineScope(Dispatchers.Main).launch {
        runnable.invoke()
    }
}

fun Fragment.main(runnable:suspend ()->Unit){
    CoroutineScope(Dispatchers.Main).launch {
        runnable.invoke()
    }
}
// io CoroutineScope is used for heavy work like reading from database
fun RecyclerView.Adapter<*>.io(runnable:suspend ()->Unit){
    CoroutineScope(Dispatchers.IO).launch {
        runnable.invoke()
    }
}
// main CoroutineScope is used for updating UI
fun RecyclerView.Adapter<*>.main(runnable:suspend ()->Unit){
    CoroutineScope(Dispatchers.Main).launch {
        runnable.invoke()
    }
}
// Default CoroutineScope is used for heavy work like sorting
fun RecyclerView.Adapter<*>.default(runnable:suspend ()->Unit){
    CoroutineScope(Dispatchers.Default).launch {
        runnable.invoke()
    }
}

fun Context.io(runnable: suspend () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        runnable.invoke()
    }
}
fun Context.main(runnable: suspend () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        runnable.invoke()
    }
}
fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun dpToPx(dp: Float): Float {
    return (dp * Resources.getSystem().displayMetrics.density)
}
suspend fun <T> Flow<List<T>>.flattenToList() =
    flatMapConcat { it.asFlow() }.toList()
