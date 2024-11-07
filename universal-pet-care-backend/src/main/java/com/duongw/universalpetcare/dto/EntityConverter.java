package com.duongw.universalpetcare.dto;

import com.duongw.universalpetcare.dto.request.UserUpdateRequest;
import com.duongw.universalpetcare.dto.response.UserDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityConverter<T, D> {

    private final ModelMapper modelMapper;
    public D mapEntityToDTO(T entity, Class<D> DTOClass){
        return modelMapper.map(entity, DTOClass);
    }
    public T mapDTOToEntity( D DTO, Class<T> entityClass){
        return modelMapper.map(DTO, entityClass);
    }



}
