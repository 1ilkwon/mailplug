package com.mailplug.homework.controller;

import com.mailplug.homework.domain.ResponseMessage;
import com.mailplug.homework.entity.Board;
import com.mailplug.homework.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequestMapping("/boards")
@RequiredArgsConstructor
@RestController
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> postListGet(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Board> boardPage = boardService.getPaginatedBoardList(pageable);
            List<Board> boardList = boardPage.getContent();
            ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK.value(), "Board list retrieved successfully.", boardList, boardPage.getNumber(),
                    boardPage.getTotalElements(), boardPage.getTotalPages());

            return ResponseEntity.ok().body(responseMessage);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred: " + e.getMessage()));
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseMessage> postDetailGet(@PathVariable Long id) {

        try {
            Board board = boardService.detailBoard(id);
            if (board != null) {
                boardService.incrementViewCount(id);
                return ResponseEntity.ok().body(new ResponseMessage(HttpStatus.OK.value(), "Board details retrieved successfully.", board));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Board is not exist", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred: " + e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<ResponseMessage> registerPost(@RequestBody Board board, @RequestHeader(value = "X-USERID") String username){
        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
        String date = simpleDateFormat.format(nowDate);
        String name = board.getName();
        try {
            board.setUsername(username);
            board.setUpdateAt(date);
            board.setCreateAt(date);

            if (name != null && name.length() > 100) {
                return ResponseEntity.badRequest().body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "name cannot exceed 100 characters"));
            }
            boardService.registerBoard(board);
            URI locationUri = new URI("/boards/register");
            return ResponseEntity.created(locationUri).body(new ResponseMessage(HttpStatus.CREATED.value(), "Board updated successfully."));

        }catch (NullPointerException e){
            return ResponseEntity.badRequest().body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Board is not null"));
        } catch (URISyntaxException e) {
            return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "URISyntax Error"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage> updatePost(@PathVariable Long id, @RequestBody Board board, @RequestHeader(value = "X-USERID") String username) {
        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
        String date = simpleDateFormat.format(nowDate);

        try {
            Board existingBoard = boardService.getBoardById(id);
            if (existingBoard == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Board is not exist", null));
            }

            if (!existingBoard.getUsername().equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Permission denied: You are not the author of this post."));
            }

            existingBoard.setName(board.getName());
            existingBoard.setUpdateAt(date);
            existingBoard.setCategory(board.getCategory());
            existingBoard.setContent(board.getContent());
            boardService.updateBoard(existingBoard);
            return ResponseEntity.ok().body(new ResponseMessage(HttpStatus.OK.value(), "Board updated successfully."));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Invalid input: " + e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deletePost(@PathVariable Long id, @RequestHeader(value = "X-USERID") String username) {

        try {
            Board board = boardService.getBoardById(id);
            if (board == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Board is not exist", null));
            }

            if (!board.getUsername().equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Permission denied: You are not the author of this post."));
            }
            boardService.deleteBoard(id);
            return ResponseEntity.ok().body(new ResponseMessage(HttpStatus.OK.value(), "Board deleted successfully."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred: " + e.getMessage()));
        }
    }


    @GetMapping("/search")
    public ResponseEntity<ResponseMessage> searchPost(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Board> boardPage = boardService.findByNamePaged(name, pageable);
            List<Board> boardList = boardPage.getContent();
            ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK.value(), "Search results retrieved successfully.", boardList, boardPage.getNumber(),
                    boardPage.getTotalElements(), boardPage.getTotalPages());
            return ResponseEntity.ok().body(responseMessage);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred: " + e.getMessage()));
        }
    }
}