package uz.rdu.ucell_utolov.helpers

import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import java.util.regex.Pattern


class PatternInputFilter(private val pattern: Pattern) : InputFilter {



    override fun filter(charSeq: CharSequence?, start: Int, end: Int, span: Spanned?, dStart: Int, dEnd: Int): CharSequence? {
        Log.i("Span",span?.toString())
        charSeq?.let { source ->
            Log.i("Source",charSeq.toString())
            for (inputChar in source) {
                if (!inputChar.toString().matches(pattern.toRegex())) {
                    return if (source.length == 1) {
                        charSeq
                    } else {
                        charSeq.removeRange(charSeq.length-1,charSeq.length)
                    }
                }
            }
        }
        return charSeq
    }
}
