package com.example.codepath_instagram.fragments

import android.util.Log
import com.example.codepath_instagram.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment: FeedFragment() {

    override  fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        // Find all post objects
        query.include(Post.KEY_USER)
        // only return posts from currently signed in user
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
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