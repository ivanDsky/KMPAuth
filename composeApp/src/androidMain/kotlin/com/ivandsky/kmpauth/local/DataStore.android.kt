package com.ivandsky.kmpauth.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ivandsky.kmpauth.ApplicationContext
import kotlinx.coroutines.runBlocking

actual fun createDataStore(): DataStore<Preferences> {
    return runBlocking {
        getDataStore(producePath = { ApplicationContext.filesDir.resolve(dataStoreFileName).absolutePath })
    }
}