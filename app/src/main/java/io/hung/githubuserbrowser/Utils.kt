package io.hung.githubuserbrowser

import android.content.res.Resources
import android.view.View
import com.google.android.material.snackbar.Snackbar

class Utils {

    companion object {

        fun showError(errorMessage: String?, parent: View, resource: Resources) {
            Snackbar.make(parent, errorMessage ?: resource.getString(R.string.generic_error), Snackbar.LENGTH_LONG).show()
        }
    }
}