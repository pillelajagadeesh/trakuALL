package com.tresbu.trakeye.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.tresbu.trakeye.domain.TrCase;
import com.tresbu.trakeye.domain.TrNotification;
import com.tresbu.trakeye.service.dto.TrNotificationDTO;
import com.tresbu.trakeye.service.dto.TrNotificationListDTO;
import com.tresbu.trakeye.service.dto.TrNotificationUpdateDTO;

/**
 * Mapper for the entity TrNotification and its DTO TrNotificationDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class },unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrNotificationMapper {

    @Mapping(source = "fromUser.id", target = "fromUserId")
    @Mapping(source = "toUser.id", target = "toUserId")
    @Mapping(source = "trCase.id", target = "trCaseId")
    @Mapping(source = "fromUser.login", target = "fromUserName")
    @Mapping(source = "toUser.login", target = "toUserName")
    TrNotificationDTO trNotificationToTrNotificationDTO(TrNotification trNotification);
    
    @Mapping(source = "fromUser.id", target = "fromUserId")
    @Mapping(source = "toUser.id", target = "toUserId")
    @Mapping(source = "trCase.id", target = "trCaseId")
    @Mapping(source = "fromUser.login", target = "fromUserName")
    @Mapping(source = "toUser.login", target = "toUserName")
    TrNotificationListDTO trNotificationToTrNotificationListDTO(TrNotification trNotification);

    List<TrNotificationDTO> trNotificationsToTrNotificationDTOs(List<TrNotification> trNotifications);

    @Mapping(source = "fromUserId", target = "fromUser")
    @Mapping(source = "toUserId", target = "toUser")
    @Mapping(source = "trCaseId", target = "trCase")
    TrNotification trNotificationDTOToTrNotification(TrNotificationDTO trNotificationDTO);

    List<TrNotification> trNotificationDTOsToTrNotifications(List<TrNotificationDTO> trNotificationDTOs);

    void updateTrNotificationFromTrNotificationUpdateDTO(TrNotificationUpdateDTO trNotificationUpdateDTO,@MappingTarget TrNotification trNotification);
    
    default TrCase trCaseFromId(Long id) {
        if (id == null) {
            return null;
        }
        TrCase trCase = new TrCase();
        trCase.setId(id);
        return trCase;
    }
}
