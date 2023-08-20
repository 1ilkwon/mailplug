package com.mailplug.homework.service;

import com.mailplug.homework.entity.Board;
import com.mailplug.homework.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

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
        boardRepository.save(board);
    }

    public void updateBoard(Board board) {
        boardRepository.save(board);
    }
    public void deleteBoard(Long id){
        boardRepository.deleteById(id);
    }

    public Board getBoardById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    public Page<Board> getPaginatedBoardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> findByNamePaged(String name, Pageable pageable) {
        return boardRepository.findByNameContaining(name, pageable);
    }

}
