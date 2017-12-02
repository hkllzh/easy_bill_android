package com.hkllzh.easybill.db

import android.arch.persistence.room.*


/**
 * page
 *
 * @author lizheng on 2017/12/2
 */
//@Entity
//class User {
//    @PrimaryKey
//    var uid: Int = 0
//
//    @ColumnInfo(name = "first_name")
//    var firstName: String? = null
//
//    @ColumnInfo(name = "last_name")
//    var lastName: String? = null
//}


@Entity
data class User(
        @PrimaryKey val uid: Int,
        @ColumnInfo(name = "username") val username: String,
        @ColumnInfo(name = "token") val token: String
)


@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}