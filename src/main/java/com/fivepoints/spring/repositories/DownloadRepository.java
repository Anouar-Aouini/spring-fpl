package com.fivepoints.spring.repositories;

import com.fivepoints.spring.entities.Download;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DownloadRepository extends JpaRepository<Download, Long> {
    @Query("SELECT d FROM Download d WHERE d.id = :download_id ")
    Download findByIdTwo(@Param("download_id") Long download_id );
    @Query("SELECT d FROM Download d WHERE d.u_id = :id ")
    List<Download> findDownloadByOwner(@Param("id") Long id );
}
