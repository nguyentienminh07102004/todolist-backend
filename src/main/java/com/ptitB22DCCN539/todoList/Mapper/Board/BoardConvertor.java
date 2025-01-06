package com.ptitB22DCCN539.todoList.Mapper.Board;

import com.ptitB22DCCN539.todoList.Mapper.Task.TaskConvertor;
import com.ptitB22DCCN539.todoList.Modal.Entity.BoardEntity;
import com.ptitB22DCCN539.todoList.Modal.Entity.TaskEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Board.BoardRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.BoardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardConvertor {
    private final BoardMapper boardMapper;
    private final TaskConvertor taskConvertor;

    @Autowired
    public BoardConvertor(BoardMapper boardMapper,
                          TaskConvertor taskConvertor) {
        this.boardMapper = boardMapper;
        this.taskConvertor = taskConvertor;
    }

    public BoardEntity RequestToEntity(BoardRequest boardRequest) {
        BoardEntity boardEntity = boardMapper.RequestToEntity(boardRequest);
        List<TaskEntity> tasks = boardRequest.getTasks().stream()
                .map(taskConvertor::taskRequestToTaskEntity)
                .toList();
        boardEntity.setTasks(tasks);
        return boardEntity;
    }

    public BoardResponse EntityToResponse(BoardEntity boardEntity) {
        return boardMapper.EntityToResponse(boardEntity);
    }
}
