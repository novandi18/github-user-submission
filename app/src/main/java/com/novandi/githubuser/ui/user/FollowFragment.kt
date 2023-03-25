package com.novandi.githubuser.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.novandi.githubuser.api.UserItems
import com.novandi.githubuser.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private var position: Int? = null
    private var username: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager

        userViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        if (position == 1) {
            userViewModel.getUserFollowing(username.toString())
            showResult()
        } else {
            userViewModel.getUserFollower(username.toString())
            showResult()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showResult() {
        userViewModel.userFollow.observe(viewLifecycleOwner) { users ->
            val listFollowAdapter = ListFollowAdapter(users)
            binding.rvFollow.adapter = listFollowAdapter

            listFollowAdapter.setOnItemClickCallback(object : ListFollowAdapter.OnItemClickCallback {
                override fun onItemClicked(data: UserItems) {
                    val intent = Intent(activity, UserActivity::class.java)
                    intent.putExtra("username", data.usernameItem)
                    activity?.startActivity(intent)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION: String = "FollowFragmentPosition"
        const val ARG_USERNAME: String = "FollowFragmentUsername"
    }
}
