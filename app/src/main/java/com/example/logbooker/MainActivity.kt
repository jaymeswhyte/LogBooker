package com.example.logbooker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        Log.wtf("Hello", "Hello!")
        //Check if database exists
        dbCheck();
        // Load Books from Shared Preferences
        val bookArray = loadBooks()
        //val gson = Gson()
        //var bookArray = arrayListOf<Book>()
        //for ((key, jsonString) in bookMap){
        //    val parsedBook = gson.fromJson(jsonString as String, Book::class.java)
        //    bookArray = gson.fromJson(jsonString as String, ArrayList<Book>()::class.java)
        //    bookArray.add(parsedBook)
        //}

        val addBookButton = findViewById<Button>(R.id.buttonAddBook)
        addBookButton.setOnClickListener {
            addBook(bookArray)
        }

        //val dune = Book(1, "Dune", "Frank Herbert", 500, 0, false)


        val bookRecycler = findViewById<RecyclerView>(R.id.bookRecycler)
        bookRecycler.setHasFixedSize(true)
        bookRecycler.layoutManager = LinearLayoutManager(this)
        var bookAdapter = BookAdapter(bookArray)
        bookRecycler.adapter = bookAdapter



        val testBox = findViewById<TextView>(R.id.testBox)
        //var titlesString = ""
        for(book in bookArray){
            // Get title and add to display string
            //titlesString += book.title
        }
        testBox.text = bookArray.size.toString()
    }


    private fun dbCheck(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        if (!sharedPreferences.contains("DB_EXISTS")) {
            val myLibrary = openOrCreateDatabase("myLibrary.db", MODE_PRIVATE, null)
            myLibrary.execSQL(
                "CREATE TABLE book (id INT PRIMARY KEY, title VARCHAR(200), author VARCHAR(100), pages INT DEFAULT 0, pagesRead INT DEFAULT 0, complete INT DEFAULT 0);")
            myLibrary.close()
            val editor = sharedPreferences.edit()
            editor.apply {
                editor.putBoolean("DB_EXISTS", true)
                editor.commit()
            }.apply()
        }
    }
    private fun addBook(bookArray:ArrayList<Book>){
        // Get Text Fields Input
        Log.d("AddBook", "Trying to add book")
        val bookTitle = findViewById<EditText>(R.id.nameText).text.toString()
        val bookAuthor = findViewById<EditText>(R.id.authorText).toString()

        // Create new book object with input values
        val newBook = Book(title=bookTitle, author=bookAuthor)
        bookArray.add(newBook)

        // Convert book to Json using Gson
        //val gson = Gson()
        //val jsonBooks = gson.toJson(bookArray)

        // Ready up SharedPreferences
        //val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        //val editor = sharedPreferences.edit()
        //editor.apply {
        //    editor.putString("bookList", jsonBooks)
        //    editor.commit()
        //}.apply()

        // Insert Book into database
        val myLibrary = openOrCreateDatabase("myLibrary.db", MODE_PRIVATE, null)
        var nextIndex = 0
        Log.d("Index", "Default Index Created")
        val testCursor = myLibrary.rawQuery("SELECT EXISTS(SELECT * from book);", null)
        Log.d("Query", "Query W")
        Log.d("Count", testCursor.count.toString())
        if (testCursor.count==1){
            nextIndex=0
            Log.d("Empty", "Table is empty")}
        else{
            val myCursor = myLibrary.rawQuery("SELECT MAX(id) from book;", null)
            Log.d("Got highest id", "Got highest ID")
            nextIndex = myCursor.getInt(0)+1
            myCursor.close()
            Log.d("Closed Cursor", "Closed Cursor")
        }
        val values = ContentValues()
        values.put("id", nextIndex)
        values.put("title", bookTitle)
        values.put("author", bookAuthor)
        myLibrary.insert("book", null, values)
        Log.d("Inserted New Values", "inserted new values")
        myLibrary.close()

        Toast.makeText(this, "Book Added!", Toast.LENGTH_SHORT).show()
    }

    private fun loadBooks(): ArrayList<Book> {
        //val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        //return sharedPreferences.all
        val returnArray = ArrayList<Book>()
        val myLibrary = openOrCreateDatabase("myLibrary.db", MODE_PRIVATE, null)
        val myCursor = myLibrary.rawQuery("SELECT * from book;", null)
        while (myCursor.moveToNext()){
            returnArray.add(Book(myCursor.getInt(0), myCursor.getString(1), myCursor.getString(2),
                myCursor.getInt(3), myCursor.getInt(4), myCursor.getInt(5)>0))
        }
        myCursor.close()
        myLibrary.close()
        return returnArray
    }
}