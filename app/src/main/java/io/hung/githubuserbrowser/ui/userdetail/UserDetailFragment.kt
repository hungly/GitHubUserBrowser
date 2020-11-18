package io.hung.githubuserbrowser.ui.userdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import io.hung.githubuserbrowser.databinding.UserDetailFragmentBinding

class UserDetailFragment : Fragment() {

    private lateinit var binding: UserDetailFragmentBinding
    private val args: UserDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserDetailFragmentBinding.inflate(inflater, container, false)

        binding.user = args.selectedUser

        return binding.root
    }
}