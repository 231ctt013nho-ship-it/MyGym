package com.example.mygym.data

import android.content.Context

/**
 * AccountStorage
 *
 * Dùng để lưu và lấy thông tin tài khoản
 * bằng SharedPreferences của Android.
 */
object AccountStorage {

    // Tên vùng lưu dữ liệu
    private const val PREF_NAME = "MyGymAccount"

    // Tên các dữ liệu được lưu
    private const val USERNAME = "username"
    private const val PASSWORD = "password"
    private const val IS_LOGGED_IN = "isLoggedIn"

    /**
     * Đăng ký tài khoản mới.
     *
     * Lưu username và password.
     * Khi đăng ký xong thì trạng thái đăng nhập = false.
     */
    fun register(
        context: Context,
        username: String,
        password: String
    ) {

        val preferences =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        preferences.edit()
            .putString(
                USERNAME,
                username
            )
            .putString(
                PASSWORD,
                password
            )
            .putBoolean(
                IS_LOGGED_IN,
                false
            )
            .apply()
    }

    /**
     * Kiểm tra tài khoản đã tồn tại hay chưa.
     */
    fun accountExists(
        context: Context,
        username: String
    ): Boolean {

        val preferences =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        return preferences.getString(
            USERNAME,
            null
        ) == username
    }

    /**
     * Lấy mật khẩu đã lưu của tài khoản.
     */
    fun getPassword(
        context: Context,
        username: String
    ): String? {

        val preferences =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        val savedUsername =
            preferences.getString(
                USERNAME,
                null
            )

        if (savedUsername == username) {

            return preferences.getString(
                PASSWORD,
                null
            )
        }

        return null
    }

    /**
     * Lấy username đã lưu.
     */
    fun getUsername(
        context: Context
    ): String {

        val preferences =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        return preferences.getString(
            USERNAME,
            ""
        ) ?: ""
    }

    /**
     * Lưu trạng thái đăng nhập.
     */
    fun saveLogin(
        context: Context,
        username: String
    ) {

        val preferences =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        preferences.edit()
            .putString(
                USERNAME,
                username
            )
            .putBoolean(
                IS_LOGGED_IN,
                true
            )
            .apply()
    }

    /**
     * Lấy danh sách những ngày đã hoàn thành.
     */
    fun getCompletedDays(
        context: Context
    ): Set<String> {

        val preferences =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        return preferences
            .getStringSet(
                "completedDays",
                emptySet()
            )
            ?.toSet()
            ?: emptySet()
    }

    /**
     * Lưu những ngày đã hoàn thành.
     */
    fun saveCompletedDays(
        context: Context,
        days: Set<String>
    ) {

        val preferences =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        preferences.edit()
            .putStringSet(
                "completedDays",
                days
            )
            .apply()
    }

    /**
     * Đăng xuất tài khoản.
     *
     * Chỉ thay đổi trạng thái đăng nhập thành false.
     */
    fun logout(
        context: Context
    ) {

        val preferences =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        preferences.edit()
            .putBoolean(
                IS_LOGGED_IN,
                false
            )
            .apply()
    }
}