package com.tresbu.trakeye.service.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.tresbu.trakeye.domain.Authority;
import com.tresbu.trakeye.domain.Geofence;
import com.tresbu.trakeye.domain.User;
import com.tresbu.trakeye.service.dto.UserDTO;
import com.tresbu.trakeye.service.dto.UserIdDTO;
import com.tresbu.trakeye.service.dto.UserUIDTO;

/**
 * Mapper for the entity User and its DTO UserDTO.
 */
@Mapper(componentModel = "spring", uses = {TrakeyeTypeMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDTO userToUserDTO(User user);

    
    List<UserDTO> usersToUserDTOs(List<User> users);

    
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activationKey", ignore = true)
    @Mapping(target = "resetKey", ignore = true)
    @Mapping(target = "resetDate", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "geofences", ignore = true)
    @Mapping(target = "trakeyeType", ignore = true)
    @Mapping(target = "login", ignore = true)
    @Mapping(target = "isValidLocation", ignore = true)
    @Mapping(target = "gpsStatus", ignore = true)
    User userDTOToUser(UserDTO userDTO);
    
    User userIdDTOToUser(UserIdDTO userDTO);
    
    

    List<User> userDTOsToUsers(List<UserDTO> userDTOs);

    default UserIdDTO userFromId(Long id) {
        if (id == null) {
            return null;
        }
        UserIdDTO user = new UserIdDTO();
        user.setId(id);
        return user;
    }

    default Set<String> stringsFromAuthorities (Set<Authority> authorities) {
        return authorities.stream().map(Authority::getName)
            .collect(Collectors.toSet());
    }

    default Set<Authority> authoritiesFromStrings(Set<String> strings) {
        return strings.stream().map(string -> {
            Authority auth = new Authority();
            auth.setName(string);
            return auth;
        }).collect(Collectors.toSet());
    }
        
    default Set<String> stringsFromGeofences (Set<Geofence> geofences) {
        return geofences.stream().map(Geofence::getName)
            .collect(Collectors.toSet());
    }

    default Set<Geofence> geofencessFromStrings(Set<String> strings) {
        return strings.stream().map(string -> {
            Geofence auth = new Geofence();
            auth.setName(string);
            return auth;
        }).collect(Collectors.toSet());
    }
    
    List<UserUIDTO> usersToUserUIDTOs(List<User> users);
    
    UserUIDTO userToUserUIDTO(User user);
    
    UserIdDTO userToUserSearchDTO(User user);
    
    List<UserIdDTO> usersToUserSearchDTOs(List<User> users);
}
