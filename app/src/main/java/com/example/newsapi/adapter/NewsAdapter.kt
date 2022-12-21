package com.example.newsapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapi.R
import com.example.newsapi.databinding.ItemArticlePreviewBinding
import com.example.newsapi.model.Article


class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>(){

     class ArticleViewHolder(var binding: ItemArticlePreviewBinding):
         RecyclerView.ViewHolder(binding.root)


     val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
           return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding: ItemArticlePreviewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_article_preview,
            parent,
            false
        )
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(holder.binding.ivArticleImage)
            holder.binding.tvDescription.text = article.description
            holder.binding.tvSource.text = article.source!!.name
            holder.binding.tvTitle.text = article.title
            holder.binding.tvPublishedAt.text = article.publishedAt
            setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener : ((Article) -> Unit)?=null

    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener=listener
    }
}