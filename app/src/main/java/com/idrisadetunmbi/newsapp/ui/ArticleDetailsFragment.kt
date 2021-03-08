package com.idrisadetunmbi.newsapp.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.idrisadetunmbi.newsapp.R
import com.idrisadetunmbi.newsapp.databinding.FragmentArticleDetailsBinding
import com.idrisadetunmbi.newsapp.models.sourceDateText
import com.idrisadetunmbi.newsapp.utils.navController
import com.idrisadetunmbi.newsapp.utils.viewBinding

class ArticleDetailsFragment : Fragment(R.layout.fragment_article_details) {

    private val binding by viewBinding(FragmentArticleDetailsBinding::bind)
    private val args by navArgs<ArticleDetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.collapsibleToolbar.setupWithNavController(
            binding.toolbar, navController, AppBarConfiguration(navController.graph)
        )
        binding.btnReadFullArticle.setOnClickListener {
            CustomTabsIntent.Builder()
                .build()
                .launchUrl(requireContext(), Uri.parse(args.article.url))
        }

        with(args.article) {
            binding.tvTitle.text = title
            binding.tvDescription.text = description
            binding.tvContent.text = content
            binding.ivArticleImage.load(imageUrl)
            binding.tvAuthorSourceDate.text = sourceDateText
        }
    }
}
