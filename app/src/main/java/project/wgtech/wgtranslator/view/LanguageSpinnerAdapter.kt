package project.wgtech.wgtranslator.view

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import project.wgtech.wgtranslator.R
import project.wgtech.wgtranslator.model.Language

class LanguageSpinnerAdapter constructor(
    context: Context,
    languages: ArrayList<Language>
) : ArrayAdapter<Language>(context, 0, languages) {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: layoutInflater.inflate(R.layout.item_spinner, parent, false)

        getItem(position)?.let { country ->
            bindCountryView(view, country)
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (position == 0) {
            view = layoutInflater.inflate(R.layout.item_spinner, parent, false)
            view.setOnClickListener {
                val root = parent.rootView
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
            }
            bindTitleView(view)

        } else {
            view = layoutInflater.inflate(R.layout.item_spinner, parent, false)
            getItem(position)?.let { country ->
                bindCountryView(view, country)
            }
        }

        return view
    }

    override fun getItem(position: Int): Language? {
        return if (position == 0) null else super.getItem(position - 1)
    }

    override fun getCount() = super.getCount() + 1

    override fun isEnabled(position: Int) = position != 0

    private fun bindTitleView(view: View) {
        val textViewLanguage = view.findViewById<AppCompatTextView>(R.id.textViewSpinnerItem)
        textViewLanguage.text = "Select language"
        val imageViewLanguage = view.findViewById<AppCompatImageView>(R.id.imageViewSpinnerItem)
        imageViewLanguage.visibility = View.GONE
    }

    private fun bindCountryView(view: View, language: Language) {
        val textViewLanguage = view.findViewById<AppCompatTextView>(R.id.textViewSpinnerItem)
        val imageViewLanguage = view.findViewById<AppCompatImageView>(R.id.imageViewSpinnerItem)

        textViewLanguage.text = language.name
        imageViewLanguage.setImageDrawable(language.drawable)
    }
}