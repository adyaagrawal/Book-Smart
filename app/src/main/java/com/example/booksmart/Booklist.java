package com.example.booksmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import Model.Book;
import Model.Category;

public class Booklist extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference booklistdat;
    RecyclerView bookrecyclerView;
    RecyclerView.LayoutManager layoutManager;
    String GenreID="";
    FirebaseRecyclerAdapter<Book,Booklist_viewholder> bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklist);

        database=FirebaseDatabase.getInstance();
        booklistdat=database.getReference("Book");

        bookrecyclerView=(RecyclerView)findViewById(R.id.book_recyclerview);
        bookrecyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        bookrecyclerView.setLayoutManager(layoutManager);

        if(getIntent()!=null){
            GenreID=getIntent().getStringExtra("GenreID");
        }
        if(!GenreID.isEmpty() && GenreID!=null){
            loadListBook(GenreID);
        }
    }

    private void loadListBook(String genreID) {
        bookAdapter=new FirebaseRecyclerAdapter<Book, Booklist_viewholder>(Book.class,
                R.layout.book_card,
                Booklist_viewholder.class,
                booklistdat.orderByChild("bgenreid").equalTo(GenreID)
                ) {
            @Override
            protected void populateViewHolder(Booklist_viewholder booklist_viewholder, Book book, int i) {
                booklist_viewholder.bookname.setText(book.getBname());
                booklist_viewholder.bookrating.setText("Rating: " + book.getBrating());
                booklist_viewholder.bookauthor.setText("By " + book.getBauth());
                Picasso.with(getBaseContext()).load(book.getBimg()).into(booklist_viewholder.bookimageView);

                final Book local=book;
                booklist_viewholder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent bookdet=new Intent(Booklist.this,Book_Details.class);
                        bookdet.putExtra("BookID",bookAdapter.getRef(position).getKey());
                        startActivity(bookdet);

                    }
                });
            }
        };
        bookrecyclerView.setAdapter(bookAdapter);
    }

}