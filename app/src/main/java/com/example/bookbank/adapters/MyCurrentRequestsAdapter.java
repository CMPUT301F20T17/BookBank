package com.example.bookbank.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookbank.R;
import com.example.bookbank.activities.ViewBookPhotoActivity;
import com.example.bookbank.models.Book;
import com.example.bookbank.models.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyCurrentRequestsAdapter extends ArrayAdapter {
    private ArrayList<Request> requestList;
    private Context context;
    private FirebaseFirestore firestore;

    public MyCurrentRequestsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Request> requestList) {
        super(context, 0, requestList);
        this.requestList = requestList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // custom array adapter for formatting each item in our list
        // inflate our custom layout (R.layout.gear_list_view) instead of the default view
        // LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // View view = inflater.inflate(R.layout.list_item, null);
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.my_current_requests_item,parent,false);
        }

        firestore  = FirebaseFirestore.getInstance();

        /** Get the position of book in the ArrayList<Book> */
        final Request request = requestList.get(position);

        /** Get references to the objects in the layout */
        final TextView bookTitle = view.findViewById(R.id.book_title);
        final TextView bookAuthor = view.findViewById(R.id.book_author);
        final TextView bookISBN = view.findViewById(R.id.book_isbn);
        final TextView bookStatus = view.findViewById(R.id.book_status);
        final ImageView bookImage = view.findViewById(R.id.book_image);
        Log.d("debug", request.getBookId());
        firestore.collection("Book").document(request.getBookId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Book book = documentSnapshot.toObject(Book.class);
                if (book != null) {
                    /** Set references to the book object data */
                    bookTitle.setText(book.getTitle());
                    bookAuthor.setText("By " + book.getAuthor());
                    bookISBN.setText("ISBN: " + book.getIsbn().toString());
                    bookStatus.setText("Status: " + book.getStatus());
                    //set book imageto ImageView
                    ViewBookPhotoActivity.setImage(book.getId(), bookImage);
                }
            }
        });

        return view;
    }
}
