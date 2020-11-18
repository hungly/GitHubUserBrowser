package io.hung.githubuserbrowser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import io.hung.githubuserbrowser.databinding.UserActivityBinding

class UserActivity : AppCompatActivity() {

    private lateinit var binding: UserActivityBinding

    private var searchButton: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = UserActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        checkSearchIntent(intent)

        setupViews()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let { checkSearchIntent(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.search))?.apply {
            searchButton = this
            (this.actionView as SearchView).apply {
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
            }
        }

        return true
    }

    private fun checkSearchIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                Log.d("HUNGLY", query)
            }
        }
    }

    private fun setupViews() {
        setSupportActionBar(binding.tbUserScreenToolbar)

        findNavController(R.id.main_nav_host_fragment).let {
            it.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.search_user_fragment -> {
                        searchButton?.isVisible = true
                    }
                    else -> {
                        searchButton?.isVisible = false
                    }
                }
            }
            setupActionBarWithNavController(it, AppBarConfiguration.Builder(R.id.search_user_fragment).build())
        }
    }
}