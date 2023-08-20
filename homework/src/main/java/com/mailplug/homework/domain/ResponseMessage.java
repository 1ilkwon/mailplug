package com.mailplug.homework.domain;

import com.mailplug.homework.entity.Board;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseMessage {
    private Integer status;
    private String message;
    private List<Board> boardList;
    private Board board;
    private Integer currentPage;
    private Long totalItems;
    private Integer totalPages;

    public ResponseMessage(Integer status, String message){
        this.status = status;
        this.message = message;
    }

    public ResponseMessage(Integer status, String message, Board board){
        this.status = status;
        this.message = message;
        this.board = board;
    }

    public ResponseMessage(Integer status, String message, List<Board> boardList, Integer currentPage, Long totalItems, Integer totalPages){
        this.status = status;
        this.message = message;
        this.boardList = boardList;
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

}
