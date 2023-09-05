package com.tariq.media3composemusicapp.di

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import com.tariq.media3composemusicapp.media_player.media_notification.MusicNotificationManager
import com.tariq.media3composemusicapp.media_player.service.MusicServiceHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MusicModule {
    @Provides
    @Singleton
    fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @Provides
    @Singleton
    @UnstableApi
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setHandleAudioBecomingNoisy(true)
        .setTrackSelector(DefaultTrackSelector(context))
        .build()

    @Provides
    @Singleton
    fun providesMediaSession(
        @ApplicationContext context: Context,
        exoPlayer: ExoPlayer
    ): MediaSession = MediaSession.Builder(context, exoPlayer)
        .build()

    @Provides
    @Singleton
    fun providesMusicNotificationManager(
        @ApplicationContext context: Context,
        exoPlayer: ExoPlayer
    ): MusicNotificationManager = MusicNotificationManager(
        context = context,
        exoPlayer = exoPlayer
    )

    @Provides
    @Singleton
    fun providesMusicServiceHandler(
        exoPlayer: ExoPlayer
    ): MusicServiceHandler =MusicServiceHandler(exoPlayer)


}