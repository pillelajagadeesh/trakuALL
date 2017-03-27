package com.tresbu.trakeye.service.mapper;

import com.tresbu.trakeye.domain.*;
import com.tresbu.trakeye.service.dto.TenantDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Tenant and its DTO TenantDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TenantMapper {

    TenantDTO tenantToTenantDTO(Tenant tenant);

    List<TenantDTO> tenantsToTenantDTOs(List<Tenant> tenants);

    Tenant tenantDTOToTenant(TenantDTO tenantDTO);

    List<Tenant> tenantDTOsToTenants(List<TenantDTO> tenantDTOs);
}
