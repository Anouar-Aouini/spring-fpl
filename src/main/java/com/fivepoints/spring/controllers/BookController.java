package com.fivepoints.spring.controllers;

import com.fivepoints.spring.entities.Book;
import com.fivepoints.spring.entities.Category;
import com.fivepoints.spring.entities.User;
import com.fivepoints.spring.payload.responses.MessageResponse;
import com.fivepoints.spring.repositories.BookRepository;
import com.fivepoints.spring.repositories.UserRepository;
import com.fivepoints.spring.services.BookService;
import com.fivepoints.spring.services.CategoryService;
import com.fivepoints.spring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("book")
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {

    @Autowired
    BookService bookService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookRepository bookRepository;

    @PostMapping("/{id}")
    public ResponseEntity<Book> saveNewBook(@RequestBody Book book,@PathVariable("id") long id,
                                            @CurrentSecurityContext(expression="authentication") Authentication authentication)
    {
        Category category = this.categoryService.findCategoryById(id);
        User user = this.userRepository.findByEmail(authentication.getName());
        book.getCategories().add(category);
        category.getBooks().add(book);
        book.setUser(user);
        this.bookRepository.save(book);
        Book saveNewPost =  this.bookService.saveNewBook(book);
        return new ResponseEntity<>(saveNewPost, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<Book>> getAllBooks()
    {
        List<Book> listPosts = this.bookService.getAllBooks();
        return new ResponseEntity<>(listPosts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findBookByID(@PathVariable("id") long id)
    {
        Book post = this.bookService.findBookByID(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateUserByID(@PathVariable("id") long id, @RequestBody Book post)
    {
        String message = this.bookService.updateBookByID(id, post);
        return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
    }


    @GetMapping("/search-by-title/{title}")
    public ResponseEntity<List<Book>> findByTitleContaining(@PathVariable("title") String title)
    {
        List<Book> filtredPosts = this.bookService.findByTitleContaining(title);
        return new ResponseEntity<>(filtredPosts, HttpStatus.OK);
    }

    @GetMapping("/mydownloadedbooks")
    public ResponseEntity<ArrayList<Book>> getMyDownloadedBooks(
            @CurrentSecurityContext(expression="authentication") Authentication authentication)
    {
        User user = this.userRepository.findByEmail(authentication.getName());
        ArrayList<Book> myBookedEvents = this.bookService.getMyDownloadedBooks(user.getId());
        return new ResponseEntity<>(myBookedEvents, HttpStatus.OK);
    }

}
