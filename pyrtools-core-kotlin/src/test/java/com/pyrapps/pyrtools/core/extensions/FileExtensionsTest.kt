package com.pyrapps.pyrtools.core.extensions

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import java.io.File
import java.io.FileReader


class FileExtensionsTest {
    @Test
    fun `Checks if string is saved correctly`() {
        val destinationDir = obtainFakeDir("/destination")
        val destination = File("${destinationDir.absolutePath}/foo.json")

        destination.write("""{"name": "fake"}""")
        val reader = JsonReader(FileReader("$destinationDir/foo.json"))
        val foo = Gson().fromJson<Foo>(reader, Foo::class.java)

        assertThat(foo.name, `is`("fake"))
    }

    @Test
    fun `Checks no crash if dir doesn't exist when save`() {
        val destinationDir = File("/fake")
        val destination = File("${destinationDir.absolutePath}/foo.json")

        val status = destination.write("""{"name": "fake"}""")

        assertThat(status, `is`(false))
    }

    @Test
    fun `Checks right number of elements in dir`() {
        val dir = obtainFakeDir("/sources")

        val list = dir.recursiveLs()

        assertThat(list.size, `is`(1))
    }

    @Test
    fun `Checks no crash if dir doesn't exist when list`() {
        val fakeDir = File("/foo")

        val list = fakeDir.recursiveLs()

        assertThat(list.size, `is`(0))
    }

    private fun obtainFakeDir(resourceName: String): File {
        val url = javaClass.getResource(resourceName)
        return File(url.toURI())
    }

    data class Foo(val name: String)
}