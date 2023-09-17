package com.example.logbooker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.logbooker.ui.theme.LogBookerTheme
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.gson.Gson
import org.w3c.dom.Text


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        //loadBooks()

        val addBookButton = findViewById<Button>(R.id.buttonAddBook)
        addBookButton.setOnClickListener {
            addBook()
        }

        val dune = Book(1, "Dune", "Frank Herbert", 500, false)
        val bookRecycler = findViewById<RecyclerView>(R.id.bookRecycler)
    }

    @Composable
    private fun showBook(book:Book){
        Text(book.title)
    }

    private fun addBook(){
        // Get Text Fields Input
        val bookTitle = findViewById<EditText>(R.id.nameText).text.toString()
        val bookAuthor = findViewById<EditText>(R.id.authorText).toString()

        // Create new book object with input values
        val newBook = Book(title=bookTitle, author=bookAuthor)

        // Convert book to Json using Gson
        val gson = Gson()
        val jsonBook = gson.toJson(newBook)

        // Ready up SharedPreferences
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            editor.putString("newBook", jsonBook)
            editor.commit()
        }.apply()

        Toast.makeText(this, "Book Added!", Toast.LENGTH_SHORT).show()
    }

    private fun loadBooks(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    }
}