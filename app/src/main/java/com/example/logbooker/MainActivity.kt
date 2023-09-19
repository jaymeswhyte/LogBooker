package com.example.logbooker

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        // Load Books from Shared Preferences
        val bookMap = loadBooks()
        val gson = Gson()
        val bookArray = arrayListOf<Book>()
        for ((key, jsonString) in bookMap){
            val parsedBook = gson.fromJson(jsonString as String, Book::class.java)
            bookArray.add(parsedBook)
        }

        val addBookButton = findViewById<Button>(R.id.buttonAddBook)
        addBookButton.setOnClickListener {
            addBook()
        }

        val dune = Book(1, "Dune", "Frank Herbert", 500, false)
        val bookRecycler = findViewById<RecyclerView>(R.id.bookRecycler)
        val testBox = findViewById<TextView>(R.id.testBox)
        var titlesString = ""
        for(book in bookArray){
            // Get title and add to display string
            titlesString += book.title
        }
        testBox.text = titlesString
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

    private fun loadBooks(): Map<String, *> {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.all
    }
}