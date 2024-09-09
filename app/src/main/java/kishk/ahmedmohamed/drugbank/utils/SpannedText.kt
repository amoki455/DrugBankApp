package kishk.ahmedmohamed.drugbank.utils

import android.graphics.Typeface
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import androidx.annotation.ColorInt
import kishk.ahmedmohamed.drugbank.data.models.DrugInfoReference
import kishk.ahmedmohamed.drugbank.data.models.InfoReferenceListItem

object SpannedText {
    fun createReferenceSpannedText(
        reference: InfoReferenceListItem.InfoReference,
        drugInfoReference: DrugInfoReference,
        @ColorInt referenceOrderColor: Int,
        @ColorInt highlightColor: Int? = null
    ): Spanned {
        val order = drugInfoReference.infoReferenceOrder.toString() + "."
        val details = Html.fromHtml(
            order + " " + reference.details,
            Html.FROM_HTML_MODE_COMPACT
        ).restyleLinks()

        return details.apply {
            setSpan(
                ForegroundColorSpan(referenceOrderColor),
                0,
                order.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(StyleSpan(Typeface.BOLD), 0, order.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (highlightColor != null) {
                setSpan(
                    BackgroundColorSpan(highlightColor),
                    0,
                    details.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    fun Spanned.restyleLinks(): SpannableString {
        val spannable = SpannableString(this)
        for (u in spannable.getSpans(0, spannable.length, URLSpan::class.java)) {
            spannable.setSpan(
                object : URLSpan(u.url) {
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }
                },
                spannable.getSpanStart(u),
                spannable.getSpanEnd(u),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannable
    }
}