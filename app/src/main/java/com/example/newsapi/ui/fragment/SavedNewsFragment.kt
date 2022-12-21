package com.example.newsapi.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.NewsActivity
import com.example.newsapi.R
import com.example.newsapi.adapter.NewsAdapter
import com.example.newsapi.databinding.FragmentBreakingNewsBinding
import com.example.newsapi.databinding.FragmentSavedNewsBinding
import com.example.newsapi.ui.NewsViewmodel
import com.google.android.material.snackbar.Snackbar


class SavedNewsFragment : Fragment() {


    private lateinit var viewmodel: NewsViewmodel
    private lateinit var binding: FragmentSavedNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = FragmentSavedNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel = (activity as NewsActivity).viewmodel


        setUpRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle= Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment,bundle )
        }

        val itemTouchHelperCallback = object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewmodel.deleteArticle(article)
                Snackbar.make(view,"ITEM DELETED",Snackbar.LENGTH_LONG).apply {
                     setAction("UNDO"){
                         viewmodel.saveArticle(article)
                     }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

        viewmodel.getSavedNews().observe(viewLifecycleOwner, Observer { article->
            newsAdapter.differ.submitList(article)

        })
    }
    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}