package com.example.genericarrayadapter.adapter

import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.descendants
import androidx.viewbinding.ViewBinding

internal fun <ItemT : Any, VB : ViewBinding> defaultBind(item: ItemT, binding: VB)
{
	val textView: TextView? = when (val root = binding.root)
	{
		is TextView  -> root
		is ViewGroup ->
		{
			val descendants = root.descendants

			descendants.find { it is TextView && it.id != -1 } as? TextView ?: descendants.find { it is TextView } as? TextView
		}

		else         -> null
	}

	textView?.text = when (item)
	{
		is CharSequence -> item
		else            -> "$item"
	}
}