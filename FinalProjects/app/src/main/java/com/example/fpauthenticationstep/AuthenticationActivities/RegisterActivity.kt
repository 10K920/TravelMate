package com.example.fpauthenticationstep.AuthenticationActivities

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.fpauthenticationstep.R
import com.example.fpauthenticationstep.PathActivities.StartPageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

// Users can register to Firebase Authentication using email and pw
// User must provide their username, nationality and profile picture as well
// These data are stored in the Firestore database
// Place collection and others collection are also added to the document so they can be used later
class RegisterActivity : AppCompatActivity() {


    var firebaseAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()

        register_btn.setOnClickListener {
            registerUser()


        }

        picture_btn.setOnClickListener {
            Log.d("StartPageActivity", "Select photo")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var photoURI: Uri? = null
    var photoStorageURI: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("StartPageActivity", "Photo selected")
            photoURI = data.data
            val photoBitmap = MediaStore.Images.Media.getBitmap(contentResolver, photoURI)
            val photoBitmapDrawable = BitmapDrawable(photoBitmap)
            picture_btn.setBackgroundDrawable(photoBitmapDrawable)
            picture_btn.text = ""
            uploadPictureToStorage()
        }
    }

    private fun registerUser() {
        val username = usernameIn.text.toString()
        val email = emailIn.text.toString()
        val password = pwIn.text.toString()
        val nationality = countrySelect.selectedCountryEnglishName

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || nationality.isEmpty()) {
            Toast.makeText(this, "Please fill out the field", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("StartPageActivity", "Username is: " + username)
        Log.d("StartPageActivity", "Email is: " + email)
        Log.d("StartPageActivity", "Password is: " + password)
        Log.d("StartPageActivity", "Nationality is:" + nationality)



        firebaseAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Main", "Successfully registered with uid: ${it.result?.user?.uid}")

                val user = HashMap<String, Any>()
                val psURL = photoStorageURI
                Log.d("MainPhoto", "PhotoURL: ${psURL.toString()}")

                user["username"] = username
                user["uid"] = it.result!!.user.uid
                user["email"] = email
                user["nationality"] = nationality
                user["picUri"] = psURL.toString()
                user["place"] = "none"
                user["others"] = arrayListOf(it.result!!.user.uid)
                Log.d("Registration", "PhotoURI:${photoStorageURI.toString()}")


                FirebaseFirestore.getInstance().document("users/${firebaseAuth?.currentUser?.uid}")
                    .set(user)
                    .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written") }
                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

                val intent = Intent(this, StartPageActivity::class.java)
                startActivity(intent)


            }
            ?.addOnFailureListener {
                Log.d("Main", "Failed to register user: ${it.message}")
                Toast.makeText(this, "Failed to register user: ${it.message}", Toast.LENGTH_SHORT).show()
            }


    }

    private fun uploadPictureToStorage(){
        if (photoURI == null) return
        val file = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$file")
        ref.putFile(photoURI!!)
            .addOnSuccessListener {
                Log.d("StartPageActivity", "Upload Successful: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("StartPageActivity", "File Loc: $it")
                    photoStorageURI = it;
                    Log.d("StartPageActivityUpload", "PhotoStorageURL: $photoStorageURI")
                }
            }
    }
}
