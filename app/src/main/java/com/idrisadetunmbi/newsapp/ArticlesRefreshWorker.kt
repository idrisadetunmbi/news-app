package com.idrisadetunmbi.newsapp

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy.KEEP
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.idrisadetunmbi.newsapp.core.data.repositories.articlesrepository.ArticlesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class ArticlesRefreshWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: ArticlesRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        try {
            repository.refreshArticles()
        } catch (ex: Exception) {
            // ignore all exceptions since it is a periodic work
        }
        return Result.success()
    }

    companion object {
        fun scheduleWork(context: Context) {
            val request = PeriodicWorkRequestBuilder<ArticlesRefreshWorker>(30, TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(REFRESH_ARTICLES_WORK, KEEP, request)
        }

        private const val REFRESH_ARTICLES_WORK = "REFRESH_ARTICLES_WORK"
    }
}
