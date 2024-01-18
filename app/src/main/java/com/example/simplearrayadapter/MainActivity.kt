package com.example.simplearrayadapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.simplearrayadapter.adapter.SimpleSingleItemArrayAdapter
import com.example.simplearrayadapter.adapter_example.PhoneCodeArrayAdapter2
import com.example.simplearrayadapter.databinding.ActivityMainBinding
import com.example.simplearrayadapter.databinding.ItemPhoneCodeBinding
import com.example.simplearrayadapter.derived.SimpleHintArrayAdapter
import com.example.simplearrayadapter.derived.setHintAdapter
import com.example.simplearrayadapter.derived.setOnItemSelectedListenerForHintAdapter
import com.example.simplearrayadapter.model.PhoneCodeInfo

class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)

		setContentView(binding.root)

		initUi()
	}


	private fun initUi() {


		val hintAdapter = HintAdapterExample(
			context = applicationContext,
			inflateView = ItemPhoneCodeBinding::inflate,
			hint = PhoneCodeInfo("uwu", "OwO"),
		).apply {
			addAll(phoneCodeInfoList)
		}

		val arrayAdapter = PhoneCodeArrayAdapter(applicationContext).apply {
			addAll(phoneCodeInfoList)
		}

//		binding.spinner.adapter = arrayAdapter
//
//		binding.spinner.setHintAdapter(hintAdapter)
//
//		binding.spinner.setOnItemSelectedListenerForHintAdapter { phone: PhoneCodeInfo, position ->
//			Toast.makeText(applicationContext, "phone: $phone, position: $position", Toast.LENGTH_SHORT).show()
//		}
		
		binding.actvCountrySelector.apply {
			setAdapter(arrayAdapter)
		}
	}
}

class HintAdapterExample(
	context: Context,
	inflateView: (LayoutInflater, ViewGroup, Boolean) -> ItemPhoneCodeBinding,
	hint: PhoneCodeInfo,
) : SimpleHintArrayAdapter<ItemPhoneCodeBinding, PhoneCodeInfo>(
	context = context,
	inflateView = inflateView,
	hint = hint,
) {
	override fun onBindView(binding: ItemPhoneCodeBinding, position: Int) {
		binding.apply {
			tvPhoneCode.text = getItemAt(position).phoneCode
			tvCountryName.text = getItemAt(position).countryName
		}
	}
}

class PhoneCodeArrayAdapter(
	context: Context,
) : SimpleSingleItemArrayAdapter<ItemPhoneCodeBinding, PhoneCodeInfo>(
	context = context,
	inflateView = ItemPhoneCodeBinding::inflate,
) {
	override fun onBindView(binding: ItemPhoneCodeBinding, position: Int) {
		binding.apply {
			tvPhoneCode.text = getItem(position).phoneCode + "owo"
			tvCountryName.text = getItem(position).countryName
		}
	}
	
	override fun onBindDropDownView(binding: ItemPhoneCodeBinding, position: Int) {
		binding.apply {
			tvPhoneCode.text = getItem(position).phoneCode + "uwu"
			tvCountryName.text = getItem(position).countryName
		}
	}

//	override fun getFilter(): Filter = phoneCodeFilter
//	
//	
//	private val phoneCodeFilter = object : Filter() {
//		override fun performFiltering(constraint: CharSequence?): FilterResults {
//			
//			val filteredList = phoneCodeInfoList.filter { phoneCodeInfo ->
//				phoneCodeInfo.countryName.contains(constraint ?: "", ignoreCase = true)
//					|| phoneCodeInfo.phoneCode.contains(constraint ?: "", ignoreCase = true)
//			}
//			
//			return FilterResults().apply { 
//				values = filteredList
//				count = filteredList.size
//			}
//		}
//		
//		override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//			results?.let { filterResults ->
//				clear()
//				addAll(filterResults.values as List<PhoneCodeInfo>)
//				notifyDataSetChanged()
//			}
//		}
//	}
}


private val phoneCodeInfoList = listOf(
	PhoneCodeInfo("+81", "Japan"),
	PhoneCodeInfo("+86", "China"),
	PhoneCodeInfo("+91", "India"),
	PhoneCodeInfo("+92", "Pakistan"),
	PhoneCodeInfo("+971", "United Arab Emirates"),
	PhoneCodeInfo("+972", "Israel"),
	PhoneCodeInfo("+966", "Saudi Arabia"),
	PhoneCodeInfo("+965", "Kuwait"),
	PhoneCodeInfo("+962", "Jordan"),
	PhoneCodeInfo("+968", "Oman"),
	PhoneCodeInfo("+974", "Qatar"),
	PhoneCodeInfo("+973", "Bahrain"),
	PhoneCodeInfo("+974", "Qatar"),
	PhoneCodeInfo("+963", "Syria"),
	PhoneCodeInfo("+961", "Lebanon"),
	PhoneCodeInfo("+20", "Egypt"),
	PhoneCodeInfo("+212", "Morocco"),
	PhoneCodeInfo("+216", "Tunisia"),
	PhoneCodeInfo("+213", "Algeria"),
	PhoneCodeInfo("+216", "Tunisia"),
	PhoneCodeInfo("+218", "Libya"),
	PhoneCodeInfo("+249", "Sudan"),
	PhoneCodeInfo("+251", "Ethiopia"),
	PhoneCodeInfo("+254", "Kenya"),
	PhoneCodeInfo("+256", "Uganda"),
	PhoneCodeInfo("+260", "Zambia"),
	PhoneCodeInfo("+261", "Madagascar"),
	PhoneCodeInfo("+267", "Botswana"),
	PhoneCodeInfo("+268", "Swaziland"),
	PhoneCodeInfo("+267", "Botswana"),
	PhoneCodeInfo("+266", "Lesotho"),
	PhoneCodeInfo("+212", "Western Sahara"),
	PhoneCodeInfo("+237", "Cameroon"),
	PhoneCodeInfo("+236", "Central African Republic"),
	PhoneCodeInfo("+235", "Chad"),
	PhoneCodeInfo("+243", "Democratic Republic of Congo"),
	PhoneCodeInfo("+242", "Republic of Congo"),
	PhoneCodeInfo("+250", "Rwanda"),
	PhoneCodeInfo("+257", "Burundi"),
	PhoneCodeInfo("+229", "Benin"),
	PhoneCodeInfo("+226", "Burkina Faso"),
	PhoneCodeInfo("+225", "Ivory Coast"),
	PhoneCodeInfo("+220", "Gambia"),
	PhoneCodeInfo("+233", "Ghana"),
	PhoneCodeInfo("+224", "Guinea"),
	PhoneCodeInfo("+245", "Guinea-Bissau"),
	PhoneCodeInfo("+231", "Liberia"),
	PhoneCodeInfo("+223", "Mali"),
	PhoneCodeInfo("+222", "Mauritania"),
	PhoneCodeInfo("+230", "Mauritius"),
	PhoneCodeInfo("+265", "Malawi"),
	PhoneCodeInfo("+212", "Mayotte"),
	PhoneCodeInfo("+227", "Niger"),
	PhoneCodeInfo("+234", "Nigeria"),
	PhoneCodeInfo("+262", "Réunion"),
	PhoneCodeInfo("+250", "Rwanda"),
	PhoneCodeInfo("+221", "Senegal"),
	PhoneCodeInfo("+232", "Sierra Leone"),
	PhoneCodeInfo("+252", "Somalia"),
	PhoneCodeInfo("+239", "Sao Tome and Principe"),
	PhoneCodeInfo("+255", "Tanzania"),
	PhoneCodeInfo("+228", "Togo"),
	PhoneCodeInfo("+216", "Tunisia"),
	PhoneCodeInfo("+256", "Uganda"),
	PhoneCodeInfo("+260", "Zambia"),
	PhoneCodeInfo("+263", "Zimbabwe"),
)
