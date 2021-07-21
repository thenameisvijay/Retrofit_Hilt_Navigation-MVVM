package com.vj.navigationcomponent.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vj.navigationcomponent.R
import com.vj.navigationcomponent.databinding.FragmentRepoListBinding
import com.vj.navigationcomponent.helper.NetworkHelper
import com.vj.navigationcomponent.model.RepoResponse
import com.vj.navigationcomponent.network.Status
import com.vj.navigationcomponent.viewmodel.RepoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepoListFragment : Fragment(), RepoAdapter.RepoItemClickListener {

    private val repoViewModel: RepoViewModel by activityViewModels()
    private lateinit var mProgress: ProgressBar
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RepoAdapter

    private var fragmentRepoListBinding: FragmentRepoListBinding? = null
    private val binding get() = fragmentRepoListBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentRepoListBinding = FragmentRepoListBinding.inflate(inflater, container, false)
        setupUI()
        setupObservers()
        return binding.root
    }

    private fun setupUI() {
        mRecyclerView = binding.repoList
        mProgress = binding.progress
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = RepoAdapter(this)
        mRecyclerView.addItemDecoration(
            DividerItemDecoration(
                mRecyclerView.context,
                (mRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        mRecyclerView.adapter = mAdapter
    }

    private fun setupObservers() {
        if (NetworkHelper.hasInternet(requireContext())) {
            repoViewModel.getRepoList().observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            mRecyclerView.visibility = View.VISIBLE
                            mProgress.visibility = View.GONE
                            resource.data?.let { gitUsers ->
                                setToAdapter(gitUsers)
                            }
                        }
                        Status.ERROR -> {
                            mRecyclerView.visibility = View.VISIBLE
                            mProgress.visibility = View.GONE
                            Log.e("ERROR", "error msg: " + it.message)
                        }
                        Status.LOADING -> {
                            mProgress.visibility = View.VISIBLE
                            mRecyclerView.visibility = View.GONE
                        }
                    }
                }
            })
        } else {
            mProgress.visibility = View.GONE
            mRecyclerView.visibility = View.GONE
            binding.errorMesg.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setToAdapter(gitUsers: ArrayList<RepoResponse>) {
        mAdapter.apply {
            addGitUsers(gitUsers)
            notifyDataSetChanged()
        }
    }

    override fun onRepoItemClicked(name: String, avatar: String) {
        val action = RepoListFragmentDirections.actionRepoListFragmentToRepoDetailsFragment(name, avatar)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentRepoListBinding = null
    }
}