package io.hung.githubuserbrowser

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.hung.githubuserbrowser.api.model.User
import io.hung.githubuserbrowser.db.GitHubUserBrowserDatabase
import io.hung.githubuserbrowser.db.dao.UserDao
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var userDao: UserDao
    private lateinit var db: GitHubUserBrowserDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, GitHubUserBrowserDatabase::class.java).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndGet() {
        val user = User(0, "test", "", "", "", 0, 0, "", "")
        userDao.insert(user)
        val byLogin = userDao.get("test")
        assertThat(byLogin, equalTo(user))
    }

    @Test
    @Throws(Exception::class)
    fun deleteUser() {
        val user = User(1, "dump", "", "", "", 0, 0, "", "")
        userDao.insert(user)

        val deleteTarget = User(1, "dump", "", "", "", 0, 0, "", "")
        userDao.delete(deleteTarget)

        val byLogin = userDao.get("dump")
        assertThat(byLogin, equalTo(null))
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() {
        val user = User(2, "dump", "", "", "", 0, 0, "", "")
        userDao.insert(user)

        userDao.deleteAll()

        val users = userDao.getAllUsers()
        assertThat(users.size, equalTo(0))
    }
}