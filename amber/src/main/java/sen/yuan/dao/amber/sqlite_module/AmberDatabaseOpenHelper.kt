package sen.yuan.dao.amber.sqlite_module

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper

/**
 * Created by senyuanzi on 2018/1/6.
 */

class AmberDatabaseOpenHelper(ctx: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : ManagedSQLiteOpenHelper(ctx, name, factory, version) {
    companion object {
        private var instance: AmberDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context, config: AmberConfig): AmberDatabaseOpenHelper {
            if (instance == null) {
                instance = AmberDatabaseOpenHelper(ctx.applicationContext, config.name, config.factory, config.version)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
    }
}