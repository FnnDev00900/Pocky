package com.fnndev.pocky.di

import android.app.Application
import androidx.room.Room
import com.fnndev.pocky.data.local.database.AccountDatabase
import com.fnndev.pocky.data.local.repository.account.AccountRepository
import com.fnndev.pocky.data.local.repository.account.AccountRepositoryImpl
import com.fnndev.pocky.data.local.repository.login.LoginRepository
import com.fnndev.pocky.data.local.repository.login.LoginRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PockyAppModule {
    @Provides
    @Singleton
    fun providePockyDatabase(app: Application): AccountDatabase {
        return Room.databaseBuilder(
            app,
            AccountDatabase::class.java,
            "pocky_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBankAccountDao(db: AccountDatabase) = db.bankAccountDao

    @Provides
    @Singleton
    fun provideAccountRepository(db: AccountDatabase): AccountRepository {
        return AccountRepositoryImpl(db.bankAccountDao, db.transactionDao)
    }

    @Provides
    @Singleton
    fun provideLoginDao(db: AccountDatabase) = db.loginDao

    @Provides
    @Singleton
    fun provideLoginRepository(db: AccountDatabase): LoginRepository {
        return LoginRepositoryImpl(db.loginDao)
    }
}