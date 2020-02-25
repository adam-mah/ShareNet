package com.adamm.sharenet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.adamm.sharenet.Database.AppDatabase;
import com.adamm.sharenet.R;
import com.adamm.sharenet.ViewModel.PostViewModel;
import com.adamm.sharenet.entities.Post;

public class PostDetailDialog extends DialogFragment {

    TextView bodytxt;
    TextView authortxt;
    Post post;
    private AppDatabase mDatabase;
    private PostViewModel postViewModel;



    public PostDetailDialog(Post post,AppDatabase mDatabase) {//Get current bar progress from host activity
        Bundle b = new Bundle();
        b.putString("username",post.author );
        b.putString("body",post.body );
        b.putString("title",post.title );
        this.mDatabase = mDatabase;
        this.post = post;
        setArguments(b);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = requireActivity().getLayoutInflater().inflate(R.layout.postdetails, null);

        mDatabase = AppDatabase.getAppDatabase(this.getContext());
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);

        bodytxt = v.findViewById(R.id.bodyTxtView);
        authortxt = v.findViewById(R.id.AuthorTxtView);

        bodytxt.setText(getArguments().getString("body"));
        authortxt.setText(getArguments().getString("title"));

        builder.setTitle(getArguments().getString("username")).setView(v);

        //Restore progress


        // Add action buttons
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //
            }
        });
        builder.setNegativeButton("Remove post", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //delete post from DB

                postViewModel.deletePost(
                        new Post(String.valueOf(AppDatabase.curr_user.uid),getArguments().getString("username"),getArguments().getString("title"),getArguments().getString("body"))
                );
               // mDatabase.postDao().DeletePost(new Post(String.valueOf(AppDatabase.curr_user.uid),getArguments().getString("username"),authortxt.getText().toString(),bodytxt.getText().toString()));
            }
        });

        return builder.create();
    }
}