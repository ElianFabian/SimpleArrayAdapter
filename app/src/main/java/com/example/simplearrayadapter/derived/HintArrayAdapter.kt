package com.example.simplearrayadapter.derived

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.core.view.descendants
import androidx.core.view.isGone
import androidx.viewbinding.ViewBinding
import com.example.simplearrayadapter.adapter.SingleItemArrayAdapter

abstract class HintArrayAdapter<VB : ViewBinding, DropDownVB : ViewBinding, ItemT : Any>(
	context: Context,
	inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	inflateDropDownView: (LayoutInflater, ViewGroup, Boolean) -> DropDownVB,
	private val hint: ItemT,
) : SingleItemArrayAdapter<VB, DropDownVB, SpinnerSelection<ItemT?>>(
	context = context,
	inflateView = inflateView,
	inflateDropDownView = inflateDropDownView,
) {
	private val hiddenView by lazy { View(context).apply { isGone = true } }


	override fun onBindView(binding: VB, position: Int) {

		val item = super.getItem(position)

		val textView: TextView = when (val view = binding.root) {
			is TextView  -> view
			is ViewGroup -> {
				view.descendants
					.filterIsInstance<TextView>()
					.firstOrNull()
					?: error("Couldn't find any TextView at position $position")
			}
			else         -> view as TextView
		}

		textView.text = item.value?.let { value ->
			"$value"
		} ?: "$hint"
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

		val inflater = LayoutInflater.from(context)

		val view = when (convertView) {
			null, hiddenView -> inflateView(inflater, parent, false).root
			else             -> convertView
		}

		@Suppress("UNCHECKED_CAST")
		val binding = view.tag as? VB
			?: inflateView(inflater, parent, false).apply { root.tag = this }

		onBindView(binding, position)

		return binding.root
	}

	@CallSuper
	override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
		if (position == count) return hiddenView

		return getView(position, convertView, parent)
	}

	open fun getItemAt(position: Int): ItemT = super.getItemOrNull(position)?.value ?: hint

	open fun getItemOrNullAt(position: Int): ItemT? = super.getItemOrNull(position)?.value ?: hint


	@JvmName("addItem")
	fun add(item: ItemT) {
		super.remove(hintItem)
		super.add(SpinnerSelection(item))
		super.add(hintItem)
	}

	@JvmName("addAllItems")
	fun addAll(items: Collection<ItemT>) {
		super.remove(hintItem)
		super.addAll(items.map { SpinnerSelection(it) })
		super.add(hintItem)
	}

	@JvmName("addAllItems")
	fun addAll(vararg items: ItemT) {
		super.remove(hintItem)
		super.addAll(*items.map { SpinnerSelection(it) }.toTypedArray())
		super.add(hintItem)
	}

	@JvmName("insertItem")
	fun insert(position: Int, item: ItemT) {
		super.remove(hintItem)
		super.insert(SpinnerSelection(item), position)
		super.add(hintItem)
	}

	override fun clear() {
		super.clear()
		super.insert(hintItem, 0)
	}

	fun setList(items: List<ItemT>) {
		super.clear()
		addAll(items)
		super.add(hintItem)
	}

	override fun getCount() = super.getCount() - 1


	@Deprecated("", level = DeprecationLevel.HIDDEN)
	override fun add(`object`: SpinnerSelection<ItemT?>?) = super.add(`object`)

	@Deprecated("", level = DeprecationLevel.HIDDEN)
	override fun addAll(collection: MutableCollection<out SpinnerSelection<ItemT?>>) = super.addAll(collection)

	@Deprecated("", level = DeprecationLevel.HIDDEN)
	override fun addAll(vararg items: SpinnerSelection<ItemT?>?) = super.addAll(*items)

	@Deprecated("", level = DeprecationLevel.HIDDEN)
	override fun insert(`object`: SpinnerSelection<ItemT?>?, index: Int) = super.insert(`object`, index)

	@Deprecated("", level = DeprecationLevel.HIDDEN)
	override fun remove(`object`: SpinnerSelection<ItemT?>?) = super.remove(`object`)

	@Deprecated("", level = DeprecationLevel.HIDDEN)
	override fun getItem(position: Int): SpinnerSelection<ItemT?> {
		return super.getItem(position)
	}

	@Deprecated("", level = DeprecationLevel.HIDDEN)
	override fun getItemOrNull(position: Int): SpinnerSelection<ItemT?>? {
		return super.getItemOrNull(position)
	}


	protected companion object {
		val hintItem = SpinnerSelection(null)
	}
}

abstract class SimpleHintArrayAdapter<VB : ViewBinding, ItemT : Any>(
	context: Context,
	inflateView: (LayoutInflater, ViewGroup, Boolean) -> VB,
	hint: ItemT,
) : HintArrayAdapter<VB, VB, ItemT>(
	context = context,
	inflateView = inflateView,
	inflateDropDownView = inflateView,
	hint = hint,
) {
	override fun onBindDropDownView(binding: VB, position: Int) {
		onBindView(binding, position)
	}
}


fun <T : Any> Spinner.setHintAdapter(hintAdapter: HintArrayAdapter<*, *, T>) {
	adapter = hintAdapter
	clearSelectionForHintAdapter()
}

fun Spinner.clearSelectionForHintAdapter() {
	if (adapter != null && adapter !is HintArrayAdapter<*, *, *>) error("Adapter must be of type ${HintArrayAdapter::class.qualifiedName}")
	setSelection(adapter.count)
}

fun Spinner.setSelectionForHintAdapter(position: Int?) {
	if (position == null || position == -1) clearSelectionForHintAdapter()
	else setSelection(position)
}

inline fun <T> Spinner.setOnItemSelectedListenerForHintAdapter(
	crossinline onNothingSelected: () -> Unit = { },
	crossinline onItemSelected: (item: T, position: Int) -> Unit = { _, _ -> },
) {
	if (adapter != null && adapter !is HintArrayAdapter<*, *, *>) error("Adapter must be of type ${HintArrayAdapter::class.qualifiedName}")

	onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
		override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
			@Suppress("UNCHECKED_CAST")
			val item = parent.getItemAtPosition(position) as SpinnerSelection<T>

			if (item.value == null) return

			onItemSelected(item.value, position)
		}

		override fun onNothingSelected(parent: AdapterView<*>?) {
			onNothingSelected()
		}
	}
}


class SpinnerSelection<out T>(val value: T?) {
	override fun toString(): String = value.toString()

	override fun equals(other: Any?) = other == value

	override fun hashCode() = value.hashCode()
}