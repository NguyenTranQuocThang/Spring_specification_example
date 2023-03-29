package com.study.specification.service;

import java.util.List;

import com.study.specification.entity.Person;
import com.study.specification.request.RoomRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.study.specification.entity.Room;
import com.study.specification.repository.RoomRepository;
import com.study.specification.specification.Filter;
import com.study.specification.specification.SpecificationCommon;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

//    private final SpecificationCommon<Room> specificationCommon;

//    public List<Room> findByRoomNameAndPersonName(List<Filter> filters) {
//        var result = this.roomRepository.findAll(specificationCommon.getSpecificationFromFilters(filters));
//        return result;
//    }

    public List<Room> findByRoomNameAndPersonName(RoomRequest request) {
        return this.roomRepository.findAll(speci(request));
    }

    public Specification<Room> speci(RoomRequest request){
        return  (root, query,builder) ->{
            Join<Room,Person> personRoom = root.join("persons");
            return builder.like(personRoom.get("name"),"%"+request.getNamePerson()+"%");
        };
    }

    public List<Room> findAll (){
        return roomRepository.findAll();
    }
}
