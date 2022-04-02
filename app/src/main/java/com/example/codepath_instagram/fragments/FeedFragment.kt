package com.example.codepath_instagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codepath_instagram.Post
import com.example.codepath_instagram.PostAdapter
import com.example.codepath_instagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class FeedFragment : Fragment() {

    lateinit var postsRecyclerView: RecyclerView

    lateinit var adapter: PostAdapter

    var allPosts: MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set up view and click listener

        postsRecyclerView = view.findViewById(R.id.postRecyclerView)

        // set adapter
        adapter = PostAdapter(requireContext(), allPosts)
        postsRecyclerView.adapter = adapter

        // set layout manage on RecycleViewer
        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }

    // QUery for all posts in our server
    open fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // Find all post objects
        query.include(Post.KEY_USER)
        // return post in desscending order
        query.addDescendingOrder("createdAt")

        // only return most recent 20 posts

        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    // something went wrong
                    Log.e("DK_ERROR", "Error fetching posts")
                } else {
                    if (posts != null) {
                        for (post in posts) {
                            Log.i("Success_post", "Post is: " + post.getDescription() + post.getUser()?.username)
                        }

                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        })
    }
}