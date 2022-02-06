package com.fivepoints.spring.services;


import com.fivepoints.spring.entities.Book;
import com.fivepoints.spring.entities.Download;
import com.fivepoints.spring.entities.User;
import com.fivepoints.spring.payload.responses.MessageResponse;
import com.fivepoints.spring.repositories.DownloadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class DownloadService {

    @Autowired
    DownloadRepository downloadRepository;

    public List<Download> getAllBookings(){return (List<Download>) this.downloadRepository.findAll();}

    public MessageResponse downloadBook(Book book, User user){
        Download download = new Download();
        AtomicBoolean isDownloaded = new AtomicBoolean(false);
        this.downloadRepository.findAll().forEach(download1 ->{
            if((download1.getB_id()+"").equals(book.getId()+"") && (download1.getU_id()+"").equals(user.getId()+"")){
                isDownloaded.set(true);
            };
        });
        if(!isDownloaded.get()){
            download.setB_id(book.getId());
            download.setU_id(user.getId());
            user.setDownloadsNumber(user.getDownloadsNumber()+1);
                this.downloadRepository.save(download);
                return new MessageResponse("Book downloaded successfully!");
        }
            return new MessageResponse("Book already downloaded!");

    }
}
