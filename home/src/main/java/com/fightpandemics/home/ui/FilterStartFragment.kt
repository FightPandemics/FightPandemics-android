package com.fightpandemics.home.ui

//import com.fightpandemics.home.ui.tabs.requests.HomeRequestViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.home.R

class FilterStartFragment : Fragment() {

    // Expandable / Collapsable clickable items
    private lateinit var filterLocationExpandable: TextView
    private lateinit var filterTypeExpandable: TextView
    private lateinit var filterFromWhomExpandable: TextView
    // Location, from whom and type options Views
    private lateinit var filterLocationOptions: View
    private lateinit var filterFromWhomOptions: View
    private lateinit var filterTypeOptions: View

    //    private lateinit var viewModel: HomeRequestViewModel

    companion object {
        fun newInstance() = FilterStartFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.filter_start_fragment,container, false)

        // get reference to expandable views and give them a onClick function
        filterLocationExpandable = rootView.findViewById(R.id.filter_location_expandable)
        filterFromWhomExpandable = rootView.findViewById(R.id.filter_from_whom_expandable)
        filterTypeExpandable = rootView.findViewById(R.id.filter_type_expandable)

        // get reference and set onclick functions to expands and collapse menus
        filterLocationOptions = rootView.findViewById(R.id.location_options)
        filterFromWhomOptions = rootView.findViewById(R.id.from_whom_options)
        filterTypeOptions = rootView.findViewById(R.id.type_options)
        filterLocationExpandable.setOnClickListener{ toggleContents(filterLocationOptions, filterLocationExpandable)}
        filterFromWhomExpandable.setOnClickListener{ toggleContents(filterFromWhomOptions, filterFromWhomExpandable) }
        filterTypeExpandable.setOnClickListener{ toggleContents(filterTypeOptions, filterTypeExpandable) }

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(HomeRequestViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun toggleContents(optionsView: View, clickableTextView: TextView ){
        if (optionsView.visibility == View.VISIBLE){
            optionsView.visibility = View.GONE
            clickableTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_plus_sign,0)
        } else{
            optionsView.visibility = View.VISIBLE
            clickableTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_minus_sign,0)
        }
    }

}
