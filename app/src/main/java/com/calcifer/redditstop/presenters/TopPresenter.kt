package com.calcifer.redditstop.presenters

import com.calcifer.redditstop.api.BASE_URL
import com.calcifer.redditstop.contracts.TopContract
import com.calcifer.redditstop.contracts.TopContract.Presenter
import com.calcifer.redditstop.models.Post
import com.calcifer.redditstop.repository.RepositoryProvider
import com.calcifer.redditstop.repository.TopRepository

class TopPresenter : Presenter, TopRepository.OnGetTopPostsComplete {

    private var view: TopContract.View? = null
    private val repository = RepositoryProvider.provideTopRepository()
    private val posts = ArrayList<Post>()

    override fun onAttachView(view: TopContract.View) {
        this.view = view
        this.view?.initPostAdapter(posts)

        makeGetTopPostsRequest()
    }

    override fun onDetachView() {
        this.view = null
    }

    private fun makeGetTopPostsRequest(){
        repository.getNextTopPosts(this)
    }

    override fun onGetTopPostsSuccess(result: ArrayList<Post>) {
        if (posts.isEmpty()){
            view?.hideMainLoading()
        } else {
            view?.hidePaginationLoading()
        }
        val startPosition = posts.size
        posts.addAll(result)
        view?.onPostsInsertion(startPosition, result.size)
    }

    override fun onGetTopPostsFailure(error: String) {
        if (posts.isEmpty()){
            view?.showToast(error)
        } else {
            view?.showRetryWithError(error)
        }

    }

    override fun onPostClick(post: Post) {
        view?.openChromeTab("$BASE_URL${post.postData?.permalink!!}")
    }

    override fun onRetryClick() {
        makeGetTopPostsRequest()
    }

    override fun loadNextPage() {
        makeGetTopPostsRequest()
    }
}