package com.crabgore.pointapp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crabgore.pointapp.R
import com.crabgore.pointapp.common.addDecoration
import com.crabgore.pointapp.databinding.FragmentHomeBinding
import com.crabgore.pointapp.ui.items.UserItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("NotifyDataSetChanged")
class HomeFragment : Fragment() {
    val viewModel: HomeViewModel by viewModel()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var itemAdapter: ItemAdapter<UserItem>

    private var searchText = ""
    private var offset = 10
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        startObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        initSearchAdapter()
        /**
         * сначала дреализовывал поиск сразу после ввода каждой буквы, но у апи гитхаба
         * ограничение по запросам (5 в минуту, как я понял), поэтому сделал только после
         * нажатия на enter
         */
//        binding.searchEt.addTextChangedListener(watcher)

        binding.searchEt.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    search(binding.searchEt.text)
                    return true
                }
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (binding.searchEt.text.isNullOrEmpty()) noItems(true)
    }

    private fun initSearchAdapter() {
        itemAdapter = ItemAdapter()
        val fastAdapter = FastAdapter.with(itemAdapter)

        binding.usersRv.apply {
            val lManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            while (itemDecorationCount > 0) {
                removeItemDecorationAt(0)
            }
            setHasFixedSize(true)
            layoutManager = lManager
            adapter = fastAdapter
            addDecoration(this)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val position = lManager.findLastVisibleItemPosition()
                        if (position >= offset) {
                            viewModel.additionalSearchUsers(searchText, page)
                            offset += 10
                            page++
                        }
                    }
                }
            })
        }
    }

    private fun startObservers() {
        viewModel.apply {
            searchedUsers.observe(viewLifecycleOwner, { data ->
                data?.let {
                    setSearchRV(it)
                }
            })

            additionalUsers.observe(viewLifecycleOwner, { data ->
                data?.let {
                    updateRV(it)
                }
            })

            failures.observe(viewLifecycleOwner, { data ->
                data?.let {
                    if (it) {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.search_limit),
                            Toast.LENGTH_LONG
                        ).show()
                        failures.value = false
                    }
                }
            })
        }
    }

    private fun setSearchRV(list: List<UserItem>) {
        itemAdapter.clear()
        itemAdapter.add(list)
        binding.usersRv.adapter?.notifyDataSetChanged()
        page++
        offset = 10
    }

    private fun updateRV(list: List<UserItem>) {
        itemAdapter.add(list)
        binding.usersRv.adapter?.notifyDataSetChanged()
    }

    private fun search(text: Editable?) {
        if (!text.isNullOrEmpty()) {
            viewModel.searchUser(text.toString())
            searchText = text.toString()
            page = 1
            noItems(false)
        } else {
            itemAdapter.clear()
            binding.usersRv.adapter?.notifyDataSetChanged()
            noItems(true)
        }
    }

    /**
     * был нужен для реализации поиска после ввода каждой буквы
     */
    private val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = search(s)
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun noItems(boolean: Boolean) {
        binding.usersRv.visibility = if (boolean) GONE else VISIBLE
    }
}