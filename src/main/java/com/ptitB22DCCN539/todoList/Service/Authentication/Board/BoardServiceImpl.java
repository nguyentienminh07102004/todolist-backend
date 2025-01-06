package com.ptitB22DCCN539.todoList.Service.Authentication.Board;

import com.ptitB22DCCN539.todoList.Mapper.Board.BoardConvertor;
import com.ptitB22DCCN539.todoList.Modal.Entity.BoardEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Board.BoardRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.BoardResponse;
import com.ptitB22DCCN539.todoList.Repository.IBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImpl implements IBoardService {
    private final IBoardRepository boardRepository;
    private final BoardConvertor boardConvertor;

    @Autowired
    public BoardServiceImpl(IBoardRepository boardRepository, BoardConvertor boardConvertor) {
        this.boardRepository = boardRepository;
        this.boardConvertor = boardConvertor;
    }

    @Override
    public BoardResponse save(BoardRequest boardRequest) {
        BoardEntity boardEntity = boardConvertor.RequestToEntity(boardRequest);
        boardRepository.save(boardEntity);
        return boardConvertor.EntityToResponse(boardEntity);
    }
}
