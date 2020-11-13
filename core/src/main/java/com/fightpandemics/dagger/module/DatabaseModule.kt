package com.fightpandemics.dagger.module

// import android.content.Context
// import androidx.room.Room
// import com.osai.core.data.local.JobDao
// import com.osai.core.data.local.JobDatabase
import dagger.Module

/**
 * Dagger module to provide database functionalities.
 */
@Module
class DatabaseModule {

//    @Singleton
//    @Provides
//    fun provideJobDatabase(context: Context): JobDatabase {
//        return Room.databaseBuilder(
//            context,
//            JobDatabase::class.java,
//            /*BuildConfig.JOB_DATABASE_NAME*/"Job.db"
//        ) // .addMigrations(JobsDatabase.MIGRATION_1_2)
//            .build() // todo 6 - Move database migration JobsDatabase.MIGRATION_1_2 to BuildConfig
//    }

//    @Singleton
//    @Provides
//    fun provideJobDao(jobDatabase: JobDatabase): JobDao = jobDatabase.jobDao()

    /*
    /**
     * Create a provider method binding for [CharacterFavoriteRepository].
     *
     * @return Instance of character favorite repository.
     * @see Provides
     */
    @Singleton
    @Provides
    fun provideCharacterFavoriteRepository(
        characterFavoriteDao: CharacterFavoriteDao
    ) = CharacterFavoriteRepository(characterFavoriteDao)

    @Provides
    fun provideFirebaseAnalytics(app: Application): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(app)
    }

    @Provides
    fun provideFirebaseAppIndexing(): FirebaseAppIndex {
        return FirebaseAppIndex.getInstance()
    }


    *//*@Provides
    @Singleton
    CheckConnectionBroadcastReceiver providesCheckConnectionBroadcastReceiver() {
        return new CheckConnectionBroadcastReceiver();
    }*/
}
