package uz.rdu.ucell_utolov.helpers

import android.text.Editable
import android.text.TextWatcher


class lfCardExpTextWatcher :TextWatcher {

    private var lock = false

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (lock || s.length > 4) {
            return
        }
        lock = true
        var i = 2
        while (i < s.length) {
            if (s.toString()[i] != ' ') {
                s.insert(i, "/")
            }
            i += 5
        }
        lock = false
    }
}