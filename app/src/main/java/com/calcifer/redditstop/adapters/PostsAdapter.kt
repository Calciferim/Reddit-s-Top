package com.calcifer.redditstop.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calcifer.redditstop.R
import com.calcifer.redditstop.models.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_loading.view.*
import kotlinx.android.synthetic.main.item_post.view.*
import java.text.DecimalFormat
import java.util.*

private const val ITEM = 0
private const val LOADING = 1

class PostsAdapter(private val posts: MutableList<Post>,
                   private val onPostClick: (post: Post) -> Unit,
                   private val onRetryClick: () -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoadingAdded = false
    private var isRetryPageLoad = false

    private var errorMessage: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            ITEM -> {
                val viewItem = inflater.inflate(R.layout.item_post, parent, false)
                PostViewHolder(viewItem)
            }
            LOADING -> {
                val viewLoading = inflater.inflate(R.layout.item_loading, parent, false)
                LoadingViewHolder(viewLoading)
            }
            else -> {
                val viewItem = inflater.inflate(R.layout.item_post, parent, false)
                return PostViewHolder(viewItem)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        when (getItemViewType(position)) {
            ITEM -> {
                val postData = posts[position].postData
                with(holder.itemView) {
                    title.text = postData?.title
                    subreddit.text = postData?.subredditNamePrefixed
                    author_and_time.text = String.format(context.getString(R.string.author_and_time), postData?.author, calculateHours(postData?.createdUtc!!))
                    Picasso.with(context).load(postData.thumbnail).into(thumbnail)

                    rating.text = if (postData.ups > 1000)
                        String.format(context.getString(R.string.rating_more_thousand), formatToThousand(postData.ups))
                    else
                        postData.ups.toString()

                    comments.text = if (postData.numComments > 1000)
                        String.format(context.getString(R.string.comments_more_thousand), formatToThousand(postData.numComments))
                    else
                        String.format(context.getString(R.string.comments_less_thousand), postData.numComments)

                    setOnClickListener {
                        onPostClick.invoke(posts[position])
                    }
                }
            }

            LOADING -> {
                with(holder.itemView) {
                    if (isRetryPageLoad) {
                        retry.visibility = View.VISIBLE
                        progress.visibility = View.GONE

                        error.text = if (errorMessage != null)
                            errorMessage
                        else
                            context.getString(R.string.error_unknown)

                    } else {
                        retry.visibility = View.GONE
                        progress.visibility = View.VISIBLE
                    }

                    setOnClickListener {
                        showRetry(false, null)
                        onRetryClick.invoke()
                    }
                }
            }
        }
    }

    private fun formatToThousand(number: Int): String {
        val beautyRating = number.toDouble() / 1000
        return if (beautyRating >= 1)
            DecimalFormat("##.#").format(beautyRating)
        else number.toString()

    }

    private fun calculateHours(createdUtc: Long): Int {
        return ((Calendar.getInstance(TimeZone.getDefault()).timeInMillis / 1000L - createdUtc) / 3600).toInt()
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == posts.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun add(post: Post) {
        posts.add(post)
        notifyItemInserted(posts.size - 1)
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Post(null, null))
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = posts.size - 1

        posts.removeAt(position)
        notifyItemRemoved(position)
    }

    fun showRetry(show: Boolean, errorMessage: String?) {
        isRetryPageLoad = show
        notifyItemChanged(posts.size - 1)

        if (errorMessage != null) this.errorMessage = errorMessage
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
