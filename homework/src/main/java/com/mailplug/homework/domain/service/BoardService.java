package com.mailplug.homework.domain.service;

import com.mailplug.homework.domain.entity.Board;
import com.mailplug.homework.domain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final EntityManager entityManager;


    public void incrementViewCount(Long id) {
        Board board = entityManager.find(Board.class, id);
        if (board != null) {
            board.setViewCount(board.getViewCount() + 1);
            entityManager.merge(board);
        }
    }

    public Board detailBoard(Long id){
        return boardRepository.findById(id).orElse(null);
    }

    public void registerBoard(Board board){
        System.out.println("service : " + board);
        boardRepository.save(board);
    }

    public void updateBoard(Board board) {
        boardRepository.save(board);
    }

    public Board getBoardById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    public void deleteBoard(Long id){
        boardRepository.deleteById(id);
    }

    public Page<Board> getPaginatedBoardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

   public List<Board> findByName(String name) {
        return boardRepository.findByName(name);
    }
    public Page<Board> findByNamePaged(String name, Pageable pageable) {
        return boardRepository.findByNameContaining(name, pageable);
    }


}
