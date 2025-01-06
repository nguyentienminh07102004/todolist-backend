package com.ptitB22DCCN539.todoList.Mapper.Board;

import com.ptitB22DCCN539.todoList.Modal.Entity.BoardEntity;
import com.ptitB22DCCN539.todoList.Modal.Request.Board.BoardRequest;
import com.ptitB22DCCN539.todoList.Modal.Response.BoardResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    @Mappings(value = {
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "modifiedBy", ignore = true),
            @Mapping(target = "modifiedDate", ignore = true),
            @Mapping(target = "tasks", ignore = true),
    })
    BoardEntity RequestToEntity(BoardRequest boardRequest);
    BoardResponse EntityToResponse(BoardEntity boardEntity);
}
