package com.amresh.messenger

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amresh.messenger.ModelClasses.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_visit_user_profile.*

class VisitUserProfileActivity : AppCompatActivity() {


        private  var userVisitId: String = ""
        var user: Users? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_user_profile)



            userVisitId = intent.getStringExtra("Visit_id")

        val ref = FirebaseDatabase.getInstance().reference.child("Users").child(userVisitId)
        ref.addValueEventListener(object : ValueEventListener
        {

            override fun onDataChange(p0: DataSnapshot)
            {
                if(p0.exists())
                {
                    user = p0.getValue(Users::class.java)

                    username_display.text = user!!.getUsername()
                    Picasso.get().load(user!!.getProfile()).into(profile_display)
                    Picasso.get().load(user!!.getCover()).into(cover_display)

                }

            }

            override fun onCancelled(p0: DatabaseError)
            {


            }
        })

        facebook_display.setOnClickListener {

            val uri = Uri.parse(user!!.getFacebook())
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }


         inst_display.setOnClickListener {

             val uri = Uri.parse(user!!.getFacebook())
             val intent = Intent(Intent.ACTION_VIEW, uri)
             startActivity(intent)

                }
         twitter_display.setOnClickListener {

             val uri = Uri.parse(user!!.getWebsite())
             val intent = Intent(Intent.ACTION_VIEW, uri)
             startActivity(intent)

                }

        send_msg_send.setOnClickListener {

            val intent = Intent(this@VisitUserProfileActivity, MessageChatActivity::class.java)
            intent.putExtra("Visit_id", user!!.getUID())
            startActivity(intent)
                }
    }
}