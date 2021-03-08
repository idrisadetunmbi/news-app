package com.idrisadetunmbi.newsapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.idrisadetunmbi.newsapp.databinding.ItemArticleBinding
import com.idrisadetunmbi.newsapp.models.Article
import com.idrisadetunmbi.newsapp.models.sourceDateText

class ArticlesAdapter(private val itemCallback: ItemCallback) :
    ListAdapter<Article, RecyclerView.ViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        LayoutInflater.from(parent.context)
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticlesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ArticlesViewHolder).bind(getItem(position))
    }

    class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    inner class ArticlesViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var article: Article

        init {
            with(binding.tvTitle) {
                viewTreeObserver.addOnGlobalLayoutListener { maxLines = height / lineHeight }
            }
            with(binding.tvDescription) {
                viewTreeObserver.addOnGlobalLayoutListener { maxLines = height / lineHeight }
            }
            binding.root.setOnClickListener { itemCallback.onClickItem(article) }
            binding.btnBookmarkArticle.setOnClickListener {
                itemCallback.onClickBookmarkItem(article)
            }
        }

        fun bind(model: Article) {
            article = model

            binding.tvTitle.text = model.title
            binding.ivArticleImage.load(model.imageUrl)
            binding.tvDescription.text = model.description
            binding.tvAuthorSourceDate.text = model.sourceDateText
            binding.btnBookmarkArticle.isSelected = model.bookmarked
        }
    }

    interface ItemCallback {
        fun onClickItem(article: Article)

        fun onClickBookmarkItem(article: Article)
    }
}

