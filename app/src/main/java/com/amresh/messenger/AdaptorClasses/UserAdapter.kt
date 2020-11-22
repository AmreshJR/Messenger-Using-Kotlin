package com.amresh.messenger.AdaptorClasses

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amresh.messenger.MessageChatActivity
import com.amresh.messenger.ModelClasses.Chat
import com.amresh.messenger.R
import com.amresh.messenger.ModelClasses.Users
import com.amresh.messenger.VisitUserProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdaptor(mContext: Context,
                  mUsers: List<Users>,
                  isChatCheck: Boolean ): RecyclerView.Adapter<UserAdaptor.ViewHolder?>()
{
    private val mContext: Context
    private val mUsers: List<Users>
    private var isChatCheck: Boolean
    var lastMsg: String = ""

    init{
        this.mUsers = mUsers
        this.mContext = mContext
        this.isChatCheck = isChatCheck
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view: View = LayoutInflater.from(mContext).inflate(R.layout.user_search_item_layout,viewGroup,false)
        return UserAdaptor.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int)
    {

        val user: Users =mUsers[i]
        holder.usernameTxt.text = user.getUsername()
        Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile).into(holder.profileImageView)

        if(isChatCheck)
        {
           retrieveLastMessage(user.getUID(), holder.lastMessageTxt)

        }
        else
        {
            holder.lastMessageTxt.visibility= View.GONE
        }

        if (isChatCheck)
        {
            if(user.getStatus() == "Online")
            {
                holder.onlineTxt.visibility = View.VISIBLE
                holder.offlineTxt.visibility = View.GONE
            }
            else
            {
                holder.onlineTxt.visibility = View.GONE
                holder.offlineTxt.visibility = View.VISIBLE
            }
        }
        else
        {

            holder.onlineTxt.visibility = View.GONE
            holder.offlineTxt.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            val options = arrayOf<CharSequence>(
                "send Message",
                "Visit Profile"

            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(mContext)
            builder.setTitle("Select an option")
            builder.setItems(options, DialogInterface.OnClickListener{dialog, position ->
                if(position == 0)
                {

                    val intent = Intent(mContext, MessageChatActivity::class.java)
                    intent.putExtra("Visit_id",user.getUID())
                    mContext.startActivity(intent)
                }
                if(position == 1)
                {

                    val intent = Intent(mContext, VisitUserProfileActivity::class.java)
                    intent.putExtra("Visit_id",user.getUID())
                    mContext.startActivity(intent)
                }
            })
            builder.show()
        }
    }


    override fun getItemCount(): Int {
        return mUsers.size

    }


    class ViewHolder(itemsView: View) : RecyclerView.ViewHolder(itemsView)
    {
        var usernameTxt: TextView
        var profileImageView: CircleImageView
        var onlineTxt: CircleImageView
        var offlineTxt: CircleImageView
        var lastMessageTxt: TextView

        init
        {
                usernameTxt = itemsView.findViewById(R.id.username)
                profileImageView = itemsView.findViewById(R.id.profile_image)
                onlineTxt = itemsView.findViewById(R.id.image_online)
                offlineTxt = itemsView.findViewById(R.id.image_offline)
               lastMessageTxt = itemsView.findViewById(R.id.message_last)
    }

    }

    private fun retrieveLastMessage(chatUserId: String?, lastMessageTxt: TextView)
    {
        lastMsg = "defaultMsg"

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().reference.child("chats")

        reference.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {
                for(dataSnapshot in p0.children)
                {
                    val chat: Chat? = dataSnapshot.getValue(Chat::class.java)

                    if(firebaseUser != null && chat!=null)
                    {
                        if(chat.getReceiver() == firebaseUser!!.uid &&
                            chat.getSender() == chatUserId ||
                                chat.getReceiver() == chatUserId &&
                                chat.getSender() == firebaseUser!!.uid)
                        {
                            lastMsg = chat.getMessage()!!
                        }

                    }
                }
                when(lastMsg)
                {
                    "defaultMsg" -> lastMessageTxt.text = "No Message."
                    "Sent you an image." -> lastMessageTxt.text = "Image Sent."
                    else -> lastMessageTxt.text = lastMsg
                }
                lastMsg = "defaultMsg"
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })


    }

}