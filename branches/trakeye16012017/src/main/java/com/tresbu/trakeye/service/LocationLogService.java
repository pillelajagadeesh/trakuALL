package com.tresbu.trakeye.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.tresbu.trakeye.domain.LiveLogs;
import com.tresbu.trakeye.service.dto.BatteryReportDTO;
import com.tresbu.trakeye.service.dto.DistanceReportDTO;
import com.tresbu.trakeye.service.dto.LocationLogCreateDTO;
import com.tresbu.trakeye.service.dto.LocationLogDTO;
import com.tresbu.trakeye.service.dto.UserPathDTO;

/**
 * Service Interface for managing LocationLog.
 */
public interface LocationLogService {

    /**
     * Save a locationLog.
     *
     * @param locationLogDTO the entity to save
     * @return the persisted entity
     */
    LocationLogDTO save(LocationLogCreateDTO locationLogDTO) throws ParseException;

    /**
     *  Get all the locationLogs.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
   // Page<LocationLogDTO> findAll(long fromDate,long toDate,Pageable pageable);

    /**
     *  Get the "id" locationLog.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    LocationLogDTO findOne(Long id);

    /**
     *  Delete the "id" locationLog.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    
    /**
     * Returns path mapping for user
     */
    
    UserPathDTO listLocationPath(Long userId ,long fromDate,long toDate);
    
    //public  List<LocationLogDTO> listLatestLocations();
    
    public  List<LiveLogs> listLiveLogs(String login,long dateTime,int hour,long tenantId) ;
    /*
     * Updates the location log with updated time
     */
    public  LocationLogDTO update(long id, int batteryPercentage) ;
    
    /*
     * Updates the location log with updated time
     */    
    public void getUserAndInsertGpsLocationLogs();
    
    List<BatteryReportDTO> listBatteryDetails(Long userId ,long fromDate,long toDate);
    List<DistanceReportDTO> getDistanceReport(Long userId ,long fromDate,long toDate);
}
