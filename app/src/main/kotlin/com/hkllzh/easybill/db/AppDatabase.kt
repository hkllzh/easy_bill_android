package com.hkllzh.easybill.db

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase


/**
 * page
 *
 * @author lizheng on 2017/12/2
 */

object Database {

    private var db: AppDatabase? = null

    fun initialize(app: Application) {
        db = Room.databaseBuilder(app.applicationContext,
                AppDatabase::class.java, "database-name").build()
    }

    fun saveUser(u: User) {
        db?.userDao()?.insertAll(u)
    }

    fun getUserData() = db?.userDao()


}

@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}