package com.idrisadetunmbi.newsapp.ui.articledetails

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.idrisadetunmbi.newsapp.R
import com.idrisadetunmbi.newsapp.core.models.sourceDateText
import com.idrisadetunmbi.newsapp.databinding.FragmentArticleDetailsBinding
import com.idrisadetunmbi.newsapp.utils.navController
import com.idrisadetunmbi.newsapp.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ArticleDetailsFragment : Fragment(R.layout.fragment_article_details) {

    private val articleDetailsViewModel: ArticleDetailsViewModel by viewModels()
    private val binding by viewBinding(FragmentArticleDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.collapsibleToolbar.setupWithNavController(
            binding.toolbar, navController, AppBarConfiguration(navController.graph)
        )
        binding.btnReadFullArticle.setOnClickListener {
            articleDetailsViewModel.state.value.articleRes()
                ?.let {
                    CustomTabsIntent.Builder()
                        .build()
                        .launchUrl(requireContext(), Uri.parse(it.url))
                }
        }
        articleDetailsViewModel.state
            .onEach {
                it.articleRes()
                    ?.let { article ->
                        with(article) {
                            binding.tvTitle.text = title
                            binding.tvDescription.text = description
                            binding.tvContent.text = content
                            binding.ivArticleImage.load(imageUrl)
                            binding.tvAuthorSourceDate.text = sourceDateText
                        }
                    }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
