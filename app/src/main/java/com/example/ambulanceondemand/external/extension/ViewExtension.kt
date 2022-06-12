package id.byu.salesagen.external.extension

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

fun TextView.textContent() : String = text.toString()
fun View.visible() { visibility = View.VISIBLE }
const val VISIBLE : Int = View.VISIBLE
fun View.invisible() { visibility = View.INVISIBLE }
const val INVISIBLE : Int = View.INVISIBLE
fun View.gone() { visibility = View.GONE }
const val GONE : Int = View.GONE
fun View.enable() { isEnabled = true }
fun View.disabled() { isEnabled = false }
fun View.isVisible() : Boolean = visibility == VISIBLE
fun View.isNotVisible() : Boolean = !isVisible()
fun View.setColor(resColorId: Int) { setColor(ContextCompat.getColor(context, resColorId)) }
fun TextView.setColor(resColorId: Int) { setTextColor(ContextCompat.getColor(context, resColorId)) }
fun View.setBackground(resDrawableId: Int) {
    background = ContextCompat.getDrawable(context, resDrawableId)
}