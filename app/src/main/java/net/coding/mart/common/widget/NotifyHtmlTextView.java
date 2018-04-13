package net.coding.mart.common.widget;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.style.QuoteSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import net.coding.mart.common.htmltext.GrayQuoteSpan;
import net.coding.mart.common.htmltext.URLSpanNoUnderline;

/**
 * Created by chenchao on 16/3/7.
 */
public class NotifyHtmlTextView extends TextView {

    public NotifyHtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setMovementMethod(NotifyClickLinkMovementMethod.getInstance());
    }

    public void setHtmlText(String htmlText) {
        Spannable s = (Spannable) Html.fromHtml(htmlText);
        setText(getCustomSpannable(0xFF4289DB, s));
    }

    private static Spannable getCustomSpannable(int color, Spannable s) {
        URLSpan[] urlSpan = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : urlSpan) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL(), color);
            s.setSpan(span, start, end, 0);
        }

        QuoteSpan quoteSpans[] = s.getSpans(0, s.length(), QuoteSpan.class);
        for (QuoteSpan span : quoteSpans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            GrayQuoteSpan grayQuoteSpan = new GrayQuoteSpan();
            s.setSpan(grayQuoteSpan, start, end, 0);
        }

        return s;

    }
}
