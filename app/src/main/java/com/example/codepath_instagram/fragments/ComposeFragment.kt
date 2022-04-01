package com.example.codepath_instagram.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.codepath_instagram.LoginActivity
import com.example.codepath_instagram.Post
import com.example.codepath_instagram.R
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File

class ComposeFragment : Fragment() {

    val photoFileName = "photo.jpg"
    var photoFile: File? = null

    lateinit var ivPreview: ImageView

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Photo")

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("DK_PHOTO", "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    // send post to server
    fun submitPost(description: String, user: ParseUser, file: File) {
        // Create post object
        val post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        post.saveInBackground { exception ->
            if (exception != null) {
                Log.e("Error!!", "Error while saving post!")
                exception.printStackTrace()
            } else {
                Log.i("Success", "Successfully saved post!")
                // reset edit text to empty
                val editText = view?.findViewById<EditText>(R.id.description)
                editText?.setText("Description")
                // update image view to empty
                val ivPreview: ImageView = (view?.findViewById(R.id.imageView) ?: ivPreview.setImageBitmap(null)) as ImageView
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivPreview = view.findViewById(R.id.imageView)
        // set onclicklistener and logic
        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener{
            // send post to server without an image
            // get user description
            val description = view.findViewById<EditText>(R.id.description).text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile != null) {
                submitPost(description, user, photoFile!!)
            } else {
                Log.e("Error in photo", "Bad photo")
            }
        }

        view.findViewById<Button>(R.id.btnTakePicture).setOnClickListener{
            // launch camera to let user take pic
            onLaunchCamera()
        }

  /*
        fun goToLoginActivity() {
            val intent = Intent(this@ComposedFragment, LoginActivity::class.java)
            startActivity(intent)
            // close out main activity
            finish()
        }*/

        // log out user if they click logout Dkimbutton
        view.findViewById<Button>(R.id.btnLogout).setOnClickListener{
            ParseUser.logOut()
            // redirect to login activity
           // goToLoginActivity()
        }

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    // by this point we have the camera photo on disk
                    val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                    // RESIZE BITMAP, see section below
                    // Load the taken image into a preview
                    ivPreview.setImageBitmap(takenImage)
                } else { // Result was a failure
                    Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}