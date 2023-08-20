package com.mailplug.homework.domain.controller;

import com.mailplug.homework.domain.entity.Board;
import com.mailplug.homework.domain.service.BoardService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.LocalDateTimeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/board")
@RequiredArgsConstructor
@RestController
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> boardListGet(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> response = new HashMap<>();

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Board> boardPage = boardService.getPaginatedBoardList(pageable);

            List<Board> boardList = boardPage.getContent();
            response.put("state", "true");
            response.put("message", "Board list retrieved successfully.");
            response.put("boardList", boardList);
            response.put("currentPage", boardPage.getNumber());
            response.put("totalItems", boardPage.getTotalElements());
            response.put("totalPages", boardPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("state", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @GetMapping("/detail/{id}")
    public ResponseEntity<Map<String, Object>> boardDetailGet(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Board board = boardService.detailBoard(id);
            if (board != null) {
                boardService.incrementViewCount(id);
                response.put("state", "true");
                response.put("message", "Board details retrieved successfully.");
                response.put("board", board);
                return ResponseEntity.ok(response);
            } else {
                response.put("state", String.valueOf(HttpStatus.NOT_FOUND.value()));
                response.put("message", "Board not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("state", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity registerPost(@RequestBody Board board, @RequestHeader(value = "X-USERID") String username){
        Map<String, String> response = new HashMap<>();
        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
        String date = simpleDateFormat.format(nowDate);
        String name = board.getName();
        try {
            board.setUsername(username);
            board.setUpdateAt(date);
            board.setCreateAt(date);
            if (name != null && name.length() > 100) {
                response.put("state", String.valueOf(HttpStatus.BAD_REQUEST.value()));
                response.put("message", "Category cannot exceed 100 characters");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            boardService.registerBoard(board);
            response.put("state", "true");
            response.put("message", "Board updated successfully.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (NullPointerException e){
            response.put("state", String.valueOf(HttpStatus.BAD_REQUEST.value()));
            response.put("message", "board is not null");
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updatePost(@PathVariable Long id, @RequestBody Board board, @RequestHeader(value = "X-USERID") String username) {
        Map<String, Object> response = new HashMap<>();
        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
        String date = simpleDateFormat.format(nowDate);

        try {
            Board existingBoard = boardService.getBoardById(id);
            if (existingBoard == null) {
                response.put("state", String.valueOf(HttpStatus.NOT_FOUND.value()));
                response.put("message", "Board not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            if (!existingBoard.getUsername().equals(username)) {
                response.put("state", String.valueOf(HttpStatus.FORBIDDEN.value()));
                response.put("message", "Permission denied: You are not the author of this post.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            existingBoard.setName(board.getName());
            existingBoard.setUpdateAt(date);
            existingBoard.setCategory(board.getCategory());
            existingBoard.setContent(board.getContent());
            boardService.updateBoard(existingBoard);
            response.put("state", "true");
            response.put("message", "Board updated successfully.");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("state", String.valueOf(HttpStatus.BAD_REQUEST.value()));
            response.put("message", "Invalid input: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.put("state", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable Long id, @RequestHeader(value = "X-USERID") String username) {
        Map<String, Object> response = new HashMap<>();

        try {
            Board board = boardService.getBoardById(id);
            if (board == null) {
                response.put("state", String.valueOf(HttpStatus.NOT_FOUND.value()));
                response.put("message", "Board not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            if (!board.getUsername().equals(username)) {
                response.put("state", String.valueOf(HttpStatus.FORBIDDEN.value()));
                response.put("message", "Permission denied: You are not the author of this post.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            boardService.deleteBoard(id);
            response.put("state", "true");
            response.put("message", "Board deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("state", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchPost(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Board> boardPage = boardService.findByNamePaged(name, pageable);

            List<Board> boardList = boardPage.getContent();
            response.put("state", "true");
            response.put("message", "Search results retrieved successfully.");
            response.put("boardList", boardList);
            response.put("currentPage", boardPage.getNumber());
            response.put("totalItems", boardPage.getTotalElements());
            response.put("totalPages", boardPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("state", "false");
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



}
