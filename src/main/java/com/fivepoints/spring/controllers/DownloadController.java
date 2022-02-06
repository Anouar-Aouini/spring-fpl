package com.fivepoints.spring.controllers;

import com.fivepoints.spring.entities.Book;
import com.fivepoints.spring.entities.Download;
import com.fivepoints.spring.entities.User;
import com.fivepoints.spring.payload.responses.MessageResponse;
import com.fivepoints.spring.repositories.BookRepository;
import com.fivepoints.spring.repositories.DownloadRepository;
import com.fivepoints.spring.repositories.UserRepository;
import com.fivepoints.spring.services.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("download")
@CrossOrigin(origins = "http://localhost:4200")
public class DownloadController {

    @Autowired
    DownloadService downloadService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public List<Download> getAllDownloads(){
        return this.downloadService.getAllBookings();
    }

    @PostMapping("/{b_id}")
    public ResponseEntity<MessageResponse> downloadABook(
            @PathVariable("b_id") long b_id,
            @CurrentSecurityContext(expression="authentication") Authentication authentication)
    {
        User user = this.userRepository.findByEmail(authentication.getName());
        Book book = this.bookRepository.findByIdTwo(b_id);
        return new ResponseEntity<>(this.downloadService.downloadBook(book,user), HttpStatus.OK);
    }

}
