package kishk.ahmedmohamed.drugbank.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class ReferenceLinkTest {

    @Test
    fun isReferenceLink() {
        var link = "reference://ref-A123456"

        val result1 = ReferenceLink.isReferenceLink(link)
        assert(result1)

        link = "https://www.example.com"
        val result2 = ReferenceLink.isReferenceLink(link)
        assert(!result2)
    }

    @Test
    fun getReferenceId() {
        var link = "https://www.example.com"
        var id = ReferenceLink.getReferenceId(link)
        assertEquals(null, id)

        link = "reference://"
        ReferenceLink.getReferenceId(link)
        assertEquals(null, id)

        link = "reference://ref-A123456"
        id = ReferenceLink.getReferenceId(link)
        assertEquals("ref-A123456", id)
    }
}