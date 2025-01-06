package com.ptitB22DCCN539.todoList.Service.Authentication.Board;

import com.ptitB22DCCN539.todoList.Modal.Request.Board.BoardRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.BoardResponse;

public interface IBoardService {
    BoardResponse save(BoardRequest boardRequest);
}
