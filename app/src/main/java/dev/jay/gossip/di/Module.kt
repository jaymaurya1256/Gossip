package dev.jay.gossip.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.jay.gossip.database.UserDAO
import dev.jay.gossip.database.UserDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context): UserDatabase {
        return Room.databaseBuilder(
            applicationContext, UserDatabase::class.java, "user"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(db: UserDatabase): UserDAO{
        return db.userDao()
    }

    @Provides
    @Singleton
    fun provideFireStoreDatabase(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

}