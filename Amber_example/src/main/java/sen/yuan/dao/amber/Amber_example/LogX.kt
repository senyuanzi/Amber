package sen.yuan.dao.amber.Amber_example

import android.util.Log

/**
 * Created by senyuanzi on 2018/1/6.
 */


fun Any.e(tag: String, message: String) {
    Log.e(tag, message)
}

fun Any.e(tag: String, functor: () -> String) = e(tag, functor())

fun Any.e(functor: () -> String) = e(this.javaClass.simpleName, functor)
