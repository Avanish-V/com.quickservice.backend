package example.com

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import org.junit.Test


class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        client.get("/category").apply {
            assertEquals(HttpStatusCode.OK, status)
            
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
