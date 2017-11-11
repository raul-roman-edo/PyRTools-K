package com.pyrapps.pyrtools.core.repository

import com.pyrapps.pyrtools.core.Result
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Test

class RepositoryTest {
    @Test
    fun `Checks valid cache data is returned`() {
        val source1 = createUpdateableSource("source1", true, "data")
        val source2 = createSource("source2", true, "data")
        val sources = listOf(source1, source2)
        val repository = Repository(sources)

        val data = repository.obtain()

        Assert.assertThat(data.payload, `is`("data-source1"))
    }

    @Test
    fun `Checks valid data from another source is returned`() {
        val source1 = createUpdateableSource("source1", false, "data")
        val source2 = createSource("source2", true, "data")
        val sources = listOf(source1, source2)
        val repository = Repository(sources)

        val data = repository.obtain()

        Assert.assertThat(data.payload, `is`("data-source2"))
    }

    @Test
    fun `Checks valid data from another source is updated in a previous updateable source`() {
        val source1 = createUpdateableSource("source1", false, "data")
        val source2 = createSource("source2", true, "data")
        val sources = listOf(source1, source2)
        val repository = Repository(sources)

        repository.obtain()
        val data = repository.obtain()

        Assert.assertThat(data.payload, `is`("data-source2-source1"))
    }

    @Test
    fun `Checks clear method is working`() {
        val source1 = createClearableSource("source1", true, "data")
        val repository = Repository(listOf(source1))

        repository.clear()
        val data = repository.obtain()

        Assert.assertThat(data.isValid, `is`(false))
    }

    private fun createUpdateableSource(name: String, initialValidity: Boolean, data: String)
            : Source<Unit, Result<String>> {
        return object : Source<Unit, Result<String>>, Updateable<Unit, Result<String>> {
            var payload = data
            var isValid = initialValidity
            override fun request(params: Unit?) = Result(isValid, "$payload-$name")

            override fun update(params: Unit?, data: Result<String>) {
                if (data.isValid) {
                    payload = data.payload!!
                    isValid = true
                }
            }

        }
    }

    private fun createSource(name: String, initialValidity: Boolean, data: String)
            : Source<Unit, Result<String>> {
        return object : Source<Unit, Result<String>> {
            override fun request(params: Unit?) = Result(initialValidity, "$data-$name")
        }
    }

    private fun createClearableSource(name: String, initialValidity: Boolean, data: String)
            : Source<Unit, Result<String>> {
        return object : Source<Unit, Result<String>>, Clearable<Unit> {
            var result: Result<String>? = Result(initialValidity, "$data-$name")

            override fun request(params: Unit?) = result?.apply { result } ?: Result()

            override fun clear(params: Unit?) {
                result = null
            }
        }
    }
}