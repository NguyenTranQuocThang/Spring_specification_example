package com.study.specification.controller;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.specification.entity.Room;
import com.study.specification.enums.QueryOperator;
import com.study.specification.request.RoomRequest;
import com.study.specification.service.RoomService;
import com.study.specification.specification.Filter;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RoomController {
    private final RoomService roomService;

//    @PostMapping("/find")
//    public ResponseEntity<?> findRoomNameAndHasPersonName(@RequestBody RoomRequest request) {
//        List<Filter> filters = createFilter(request);
//        return ResponseEntity.ok().body(roomService.findByRoomNameAndPersonName(filters));
//    }

    @PostMapping("/find")
    public ResponseEntity<?> findRoomNameAndHasPersonName(@RequestBody RoomRequest request) {
        return ResponseEntity.ok().body(roomService.findByRoomNameAndPersonName(request));
    }

    private List<Filter> createFilter(RoomRequest request) {
        List<Filter> filters = new ArrayList<>();

        if (Strings.isNotBlank(request.getNameRoom())) {
            Filter personName = Filter.builder()
                    .field("name")
                    .operator(QueryOperator.LIKE)
                    .value(request.getNameRoom())
                    .build();
            filters.add(personName);
        }

        return filters;
    }
}
