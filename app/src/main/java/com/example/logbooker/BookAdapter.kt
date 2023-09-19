package com.example.logbooker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(private val bookList: ArrayList<Book>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>(){

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        // Image View
        // val imageView : ImageView = itemView.findViewById(R.id.imageView)
        // Text View
        val textView : TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item , parent , false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        //holder.imageView.setImageResource(book.image)
        holder.textView.text = book.title
    }
}