package com.calcifer.redditstop.ui

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.calcifer.redditstop.R
import com.calcifer.redditstop.adapters.PaginationScrollListener
import com.calcifer.redditstop.adapters.PostsAdapter
import com.calcifer.redditstop.contracts.TopContract
import com.calcifer.redditstop.models.Post
import com.calcifer.redditstop.presenters.TopPresenter
import kotlinx.android.synthetic.main.activity_top.*

private const val TOTAL_PAGES = 2

class TopActivity : AppCompatActivity(), TopContract.View {

    private lateinit var postsAdapter: PostsAdapter
    private lateinit var presenter: TopContract.Presenter
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var currentPage: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)
        presenter = TopPresenter()
        presenter.onAttachView(this)
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun hideMainLoading() {
        main_progress.visibility = View.GONE
    }

    override fun initPostAdapter(posts: ArrayList<Post>) {
        val layoutManager = LinearLayoutManager(this)
        top_posts.layoutManager = layoutManager
        top_posts.itemAnimator = DefaultItemAnimator()

        postsAdapter = PostsAdapter(posts, { post ->
            presenter.onPostClick(post)
        }, {
            presenter.onRetryClick()
        })

        top_posts.adapter = postsAdapter
        top_posts.addOnScrollListener(object : PaginationScrollListener(layoutManager) {

            override val totalPageCount: Int
                get() = TOTAL_PAGES

            override var isLastPage: Boolean = false
                get() = this@TopActivity.isLastPage

            override var isLoading: Boolean = false
                get() = this@TopActivity.isLoading

            override fun loadMoreItems() {
                this@TopActivity.isLoading = true
                currentPage += 1

                postsAdapter.addLoadingFooter()
                presenter.loadNextPage()
            }
        })
    }

    override fun openChromeTab(url: String) {
        CustomTabsIntent.Builder().build().launchUrl(this, Uri.parse(url))
    }

    override fun onPostsInsertion(startPosition: Int, size: Int) {
        postsAdapter.notifyItemRangeInserted(startPosition, size)
    }

    override fun hidePaginationLoading() {
        postsAdapter.removeLoadingFooter()
    }

    override fun showRetryWithError(error: String) {
        postsAdapter.showRetry(true, error)
    }
}
