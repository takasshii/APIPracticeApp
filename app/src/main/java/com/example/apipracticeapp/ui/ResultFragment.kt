package com.example.apipracticeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.apipracticeapp.R
import com.example.apipracticeapp.databinding.FragmentResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)

        // 値を受け取る
        val item = args.item

        binding.imageView.load(item.ownerIconUrl)
        binding.repositoryNameText.text = item.name
        binding.languageRightText.text = item.language
        binding.starsRightText.text = getString(R.string.star_number, item.stargazersCount)
        binding.watchersRightText.text = getString(R.string.watchers_number, item.watchersCount)
        binding.forkRightText.text = getString(R.string.forks_number, item.forksCount)
        binding.issueRightText.text = getString(R.string.open_issue, item.openIssuesCount)


        return binding.root
    }

    //bindingの解放
    override fun onDestroyView() {
        super.onDestroyView()
        //bindingの解放
        _binding = null
    }
}