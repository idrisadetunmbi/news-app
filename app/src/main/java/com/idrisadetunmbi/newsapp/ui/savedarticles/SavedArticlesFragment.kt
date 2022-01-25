package com.idrisadetunmbi.newsapp.ui.savedarticles

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.idrisadetunmbi.newsapp.R
import com.idrisadetunmbi.newsapp.databinding.FragmentBrowseArticlesBinding
import com.idrisadetunmbi.newsapp.ui.ArticlesList
import com.idrisadetunmbi.newsapp.utils.navController
import com.idrisadetunmbi.newsapp.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedArticlesFragment : Fragment(R.layout.fragment_browse_articles) {

    private val binding by viewBinding(FragmentBrowseArticlesBinding::bind)
    private val viewModel by viewModels<SavedArticlesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.toolbar.setupWithNavController(
            navController,
            AppBarConfiguration(setOf(R.id.browse_articles_nav, R.id.saved_articles_nav))
        )
        binding.rootComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val state by viewModel.state.collectAsState()
                ArticlesList(
                    articles = state.articles,
                    onClickBookmarkItem = viewModel::handleBookmarkArticle,
                    onClickItem = {
                        val dir = SavedArticlesFragmentDirections
                            .actionSavedArticlesNavToArticleDetailsFragment(it.id)
                        findNavController().navigate(dir)
                    },
                )
            }
        }
    }
}
