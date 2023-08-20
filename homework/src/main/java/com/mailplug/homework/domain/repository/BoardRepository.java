package com.mailplug.homework.domain.repository;

import com.mailplug.homework.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAll(Pageable pageable);

    List<Board> findByName(String name);
    Page<Board> findByNameContaining(String name, Pageable pageable);



}
