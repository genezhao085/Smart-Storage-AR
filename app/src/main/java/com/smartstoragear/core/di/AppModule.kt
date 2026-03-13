package com.smartstoragear.core.di

import android.content.Context
import androidx.room.Room
import com.smartstoragear.core.data.db.AppDatabase
import com.smartstoragear.core.data.repository.ItemRepository
import com.smartstoragear.core.data.repository.ItemRepositoryImpl
import com.smartstoragear.core.data.repository.SpaceRepository
import com.smartstoragear.core.data.repository.SpaceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "smart_storage.db").build()

    @Provides fun provideSpaceDao(db: AppDatabase) = db.spaceDao()
    @Provides fun provideNodeDao(db: AppDatabase) = db.spaceNodeDao()
    @Provides fun provideItemDao(db: AppDatabase) = db.itemDao()
    @Provides fun provideScanDao(db: AppDatabase) = db.scanDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds abstract fun bindSpaceRepository(impl: SpaceRepositoryImpl): SpaceRepository
    @Binds abstract fun bindItemRepository(impl: ItemRepositoryImpl): ItemRepository
}
