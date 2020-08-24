package com.fightpandemics.inbox.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.inbox.R
import com.fightpandemics.inbox.dagger.inject
import com.fightpandemics.utils.ViewModelFactory
import javax.inject.Inject

class InboxFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        fun newInstance() = InboxFragment()
    }

    private lateinit var viewModel: InboxViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.inbox_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InboxViewModel::class.java)
    }
}
