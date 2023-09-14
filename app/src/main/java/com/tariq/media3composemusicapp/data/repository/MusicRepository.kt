package com.tariq.media3composemusicapp.data.repository

import com.tariq.media3composemusicapp.data.local.ContentResolverHelper
import com.tariq.media3composemusicapp.data.local.models.AudioItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicRepository @Inject constructor(
    private val contentResolverHelper: ContentResolverHelper,
) {
    suspend fun getAudioData(): List<AudioItem> = withContext(Dispatchers.IO) {
        contentResolverHelper.getAudioData()
    }
}