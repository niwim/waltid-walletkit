package id.walt.webwallet.backend.context

import com.google.common.cache.CacheLoader
import id.walt.services.hkvstore.FileSystemHKVStore
import id.walt.services.hkvstore.FilesystemStoreConfig
import id.walt.services.keystore.HKVKeyStoreService
import id.walt.services.vcstore.HKVVcStoreService
import id.walt.webwallet.backend.WALTID_DATA_ROOT

object UserContextLoader : CacheLoader<String, UserContext>() {
  override fun load(key: String): UserContext {
    //TODO: get user context preferences from user database
    return UserContext(HKVKeyStoreService(), HKVVcStoreService(), FileSystemHKVStore(FilesystemStoreConfig("${WALTID_DATA_ROOT}/data/${key}")))
  }
}