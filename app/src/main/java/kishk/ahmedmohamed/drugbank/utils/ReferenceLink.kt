package kishk.ahmedmohamed.drugbank.utils

object ReferenceLink {
    internal fun isReferenceLink(link: String): Boolean {
        return link.startsWith("reference://")
    }

    fun getReferenceId(link: String): String? {
        if (!isReferenceLink(link))
            return null

        return link.split("://").getOrNull(1)
    }
}