package com.pyrapps.pyrtools.core.storage.base

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class StorageControllerTest {
  @Test
  fun `Checks loaded object equals saved one`() {
    val storage = createMemoryStorageSystem()
    val store = createStore(storage)
    val toStore = TestObject("test1", 2)
    store.save(toStore)
    val loaded = store.load()

    assertThat(loaded, `is`(toStore))
  }

  @Test
  fun `Checks loaded object equals default one if there is no one stored`() {
    val storage = createMemoryStorageSystem()
    val store = createStore(storage)

    val loaded = store.load()

    assertThat(loaded, `is`(TestObject("default", 0)))
  }

  @Test
  fun `loaded object equals saved one using typetoken`() {
    val storage = createMemoryStorageSystem()
    val store = createListStore(storage)
    val toStore = TestObject("test1", 2)
    store.save(listOf(toStore))

    val loaded = store.load()

    assertThat(loaded[0], `is`(toStore))
  }

  @Test
  fun `Checks loaded object equals default one if there is no one stored using typetoken`() {
    val storage = createMemoryStorageSystem()
    val store = createListStore(storage)
    val loaded = store.load()

    assertThat(loaded.size, `is`(0))
  }

  private fun createMemoryStorageSystem(): StorageSystem {
    return object : StorageSystem {
      var stored = ""
      override fun load(
        key: String,
        default: String
      ): String {
        return if (stored.isEmpty()) default else stored
      }

      override fun save(
        key: String,
        value: String
      ) {
        stored = value
      }
    }
  }

  private fun createStore(storage: StorageSystem) = StorageController(
      "key",
      TestObject("default", 0),
      storage
  )

  private fun createListStore(storage: StorageSystem): StorageController<List<TestObject>> {
    return StorageController(
        "key",
        mutableListOf(),
        storage
    )
  }

  data class TestObject(
    val name: String,
    val id: Int
  )
}