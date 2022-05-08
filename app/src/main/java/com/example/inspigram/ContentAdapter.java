package com.example.inspigram;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
    public static final String TAG = ContentAdapter.class.getName();

    private ArrayList<Content> contents;
    private ArrayList<Content> contentsAll;
    private Context context;
    private Content currentContent;

    private int lastPosition = -1;

    ContentAdapter (Context context, ArrayList<Content> contents) {
        this.contents = contents;
        this.contentsAll = contents;
        this.context = context;

    }

    @NonNull
    @Override
    public ContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.content, parent, false));
    }

    @Override
    public void onBindViewHolder(ContentAdapter.ViewHolder holder, int position) {
        Content currentContent = contents.get(position);

        holder.bindTo(currentContent);

        setCurrentContent(currentContent);

        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_animation);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    private void setCurrentContent(Content currentContent) {
        this.currentContent = currentContent;
    }

    private Content getCurrentContent() { return this.currentContent;}

    @Override
    public int getItemCount() { return contents.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView profilePicture;
        private TextView userName;
        private TextView likes;
        private ImageView contentImage;
        private TextView comments;
        private ImageButton like;

        private Boolean buttonOn = false;

        public ViewHolder(View view) {
            super(view);
            profilePicture = view.findViewById(R.id.profile_picture);
            userName = view.findViewById(R.id.user_name);
            likes = view.findViewById(R.id.likes);
            contentImage = view.findViewById(R.id.content_image);
            comments = view.findViewById(R.id.comments);
            like = view.findViewById(R.id.like);

            view.findViewById(R.id.like).setOnClickListener(view1 -> {
                Log.d(TAG, "Like button clicked!");
                if (buttonOn) {
                    buttonOn = false;
                    like.setBackgroundResource(R.drawable.like);
                } else {
                    buttonOn = true;
                    like.setBackgroundResource(R.drawable.like2);
                }
            });

        }

        public void bindTo(Content currentContent) {
            userName.setText(currentContent.getUserName());
            likes.setText(currentContent.getLikes());
            comments.setText(currentContent.getComment());

            Glide.with(context).load(currentContent.getProfilImage()).into(profilePicture);
            Glide.with(context).load(currentContent.getContentImage()).into(contentImage);


            itemView.findViewById(R.id.like).setOnClickListener(view -> {
                Log.d(TAG, "Like button clicked!");
                if (buttonOn) {
                    buttonOn = false;
                    like.setBackgroundResource(R.drawable.like);
                } else {
                    buttonOn = true;
                    like.setBackgroundResource(R.drawable.like2);
                }

                ((HomeActivity)context).liked(buttonOn, currentContent);
            });
        }
    }

}