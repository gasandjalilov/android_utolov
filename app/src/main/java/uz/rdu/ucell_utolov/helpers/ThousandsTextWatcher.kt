package uz.rdu.ucell_utolov.helpers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.*


class ThousandsTextWatcher(var editText: EditText?):TextWatcher {


    fun NumberTextWatcherForThousand(editText: EditText?) {
        this.editText = editText
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        try {
            editText!!.removeTextChangedListener(this)
            val value = editText!!.text.toString()
            if (value != null && value != "") {
                if (value.startsWith(".")) {
                    editText!!.setText("0.")
                }
                if (value.startsWith("0") && !value.startsWith("0.")) {
                    editText!!.setText("")
                }
                val str = editText!!.text.toString().replace(",".toRegex(), "")
                if (value != "") editText!!.setText(getDecimalFormattedString(str))
                editText!!.setSelection(editText!!.text.toString().length)
            }
            editText!!.addTextChangedListener(this)
            return
        } catch (ex: Exception) {
            ex.printStackTrace()
            editText!!.addTextChangedListener(this)
        }
    }

    fun getDecimalFormattedString(value: String): String? {
        val lst = StringTokenizer(value, ".")
        var str1 = value
        var str2 = ""
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken()
            str2 = lst.nextToken()
        }
        var str3 = ""
        var i = 0
        var j = -1 + str1.length
        if (str1[-1 + str1.length] == '.') {
            j--
            str3 = "."
        }
        var k = j
        while (true) {
            if (k < 0) {
                if (str2.length > 0) str3 = "$str3.$str2"
                return str3
            }
            if (i == 3) {
                str3 = ",$str3"
                i = 0
            }
            str3 = str1[k].toString() + str3
            i++
            k--
        }
    }

    fun trimCommaOfString(string: String): String? {
//        String returnString;
        return if (string.contains(",")) {
            string.replace(",", "")
        } else {
            string
        }
    }

}