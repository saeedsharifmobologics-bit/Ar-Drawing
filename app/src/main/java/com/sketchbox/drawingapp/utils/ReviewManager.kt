package com.sketchbox.drawingapp.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.android.play.core.review.ReviewManagerFactory

object ReviewManager {
    fun showInAppReview(context: Context) {
        val manager = ReviewManagerFactory.create(context)

        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                if (context is Activity) {
                    val flow = manager.launchReviewFlow(context, reviewInfo)
                    flow.addOnCompleteListener {
                        Toast.makeText(context, "Thanks for your feedback!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Review not available right now", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
