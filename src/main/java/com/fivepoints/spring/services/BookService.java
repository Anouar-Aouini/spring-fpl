package com.fivepoints.spring.services;

import com.fivepoints.spring.entities.Book;
import com.fivepoints.spring.exceptions.ResourceNotFoundException;
import com.fivepoints.spring.repositories.BookRepository;
import com.fivepoints.spring.repositories.DownloadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    DownloadRepository downloadRepository;

    public Book saveNewBook(Book post)
    {
        return this.bookRepository.save(post);
    }

    public List<Book> getAllBooks()
    {
        return this.bookRepository.findAll();
    }

    public Book findBookByID(long id)
    {
        Optional<Book> postData = this.bookRepository.findById(id);
        // Return statement if user exist or null
        return postData.orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

    public String updateBookByID(long id, Book book)
    {
        Optional<Book> bookData = this.bookRepository.findById(id);
        if (bookData.isPresent()) {
            Book existingBook = bookData.orElse(null);
            existingBook.setTitle(book.getTitle());
            existingBook.setDescription(book.getDescription());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setContent(book.getContent());
            // save existingUser in the database
            this.bookRepository.save(existingBook);
            // return statement
            return "Book updated successfully!";
        } else {
            throw new ResourceNotFoundException("Book not found");
        }
    }

    public List<Book> findByTitleContaining(String title)
    {
        return  this.bookRepository.findByTitleContaining(title);
    }

    public ArrayList<Book> getMyDownloadedBooks(long id){
        ArrayList<Book> myDownloadedBooks = new ArrayList<>();
        this.downloadRepository.findDownloadByOwner(id).forEach(download -> {
            this.bookRepository.findAll().forEach(book -> {
                if(download.getB_id() == book.getId()){
                    myDownloadedBooks.add(book);
                }
            });
        });
        return myDownloadedBooks;
    }
}
