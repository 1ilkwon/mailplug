package com.mailplug.homework.service;

import com.mailplug.homework.entity.Board;
import com.mailplug.homework.repository.BoardRepository;
import com.mailplug.homework.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BoardServiceTest {
    private BoardService boardService;
    @MockBean
    private EntityManager entityManager;
    @MockBean
    private BoardRepository boardRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        boardService = new BoardService(boardRepository, entityManager);
    }

    @Test
    void incrementViewCount() {
        Long boardId = 1L;
        Board board = new Board();
        board.setViewCount(0);
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(boardRepository.save(any(Board.class))).thenReturn(board);
        boardService.incrementViewCount(boardId);
        assert board.getViewCount() == 0;
    }

    @Test
    public void detailBoard_ExistingId_ReturnsBoard() {
        Long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        Board result = boardService.detailBoard(boardId);
        assert result != null;
        assert result.getId().equals(boardId);
    }

    @Test
    public void registerBoard_ValidBoard_BoardSaved() {
        Board board = new Board();
        when(boardRepository.save(any(Board.class))).thenReturn(board);
        boardService.registerBoard(board);
    }

    @Test
    public void updateBoard_ValidBoard_BoardUpdated() {
        Long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);
        when(boardRepository.save(any(Board.class))).thenReturn(board);
        boardService.updateBoard(board);
    }

    @Test
    public void deleteBoard_ValidId_BoardDeleted() {
        Long boardId = 1L;
        boardService.deleteBoard(boardId);
    }

    @Test
    public void getPaginatedBoardList_ValidPageable_ReturnsPageOfBoards() {
        List<Board> mockBoardList = new ArrayList<>();
        mockBoardList.add(new Board());
        mockBoardList.add(new Board());
        Pageable pageable = Pageable.unpaged();
        Page<Board> mockPage = new PageImpl<>(mockBoardList, pageable, mockBoardList.size());
        when(boardRepository.findAll(pageable)).thenReturn(mockPage);
        Page<Board> resultPage = boardService.getPaginatedBoardList(pageable);
    }

    @Test
    public void findByNamePaged_ValidNameAndPageable_ReturnsPageOfBoards() {
        List<Board> mockBoardList = new ArrayList<>();
        mockBoardList.add(new Board());
        mockBoardList.add(new Board());
        Pageable pageable = Pageable.unpaged();
        Page<Board> mockPage = new PageImpl<>(mockBoardList, pageable, mockBoardList.size());
        when(boardRepository.findByNameContaining(anyString(), eq(pageable))).thenReturn(mockPage);
        Page<Board> resultPage = boardService.findByNamePaged("exampleName", pageable);
    }
}