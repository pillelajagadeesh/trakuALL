package com.tresbu.trakeye.domain;

import com.tresbu.trakeye.config.Constants;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Table(name = "trakeye_user")

@SqlResultSetMappings({ @SqlResultSetMapping(name = "userAgentReport", classes = {
		@ConstructorResult(targetClass = com.tresbu.trakeye.service.dto.ReportDTO.class, columns = {
				@ColumnResult(name = "id", type = Long.class), @ColumnResult(name = "login", type = String.class),
				@ColumnResult(name = "name", type = String.class),
				@ColumnResult(name = "distance", type = Double.class), @ColumnResult(name = "cases", type = Long.class),
				@ColumnResult(name = "services", type = Long.class), }) }),
		@SqlResultSetMapping(name = "userAgentDetailedReport", classes = {
				@ConstructorResult(targetClass = com.tresbu.trakeye.service.dto.ReportDTO.class, columns = {
						@ColumnResult(name = "id", type = Long.class),
						@ColumnResult(name = "login", type = String.class),
						@ColumnResult(name = "name", type = String.class),
						@ColumnResult(name = "distance", type = Double.class),
						@ColumnResult(name = "casesCreatedCriticalNew", type = Long.class),
						@ColumnResult(name = "casesCreatedCriticalInProgress", type = Long.class),
						@ColumnResult(name = "casesCreatedCriticalPending", type = Long.class),
						@ColumnResult(name = "casesCreatedCriticalAssigned", type = Long.class),
						@ColumnResult(name = "casesCreatedCriticalResolved", type = Long.class),
						@ColumnResult(name = "casesCreatedCriticalCancelled", type = Long.class),
						@ColumnResult(name = "casesCreatedHighNew", type = Long.class),
						@ColumnResult(name = "casesCreatedHighInProgress", type = Long.class),
						@ColumnResult(name = "casesCreatedHighPending", type = Long.class),
						@ColumnResult(name = "casesCreatedHighAssigned", type = Long.class),
						@ColumnResult(name = "casesCreatedHighResolved", type = Long.class),
						@ColumnResult(name = "casesCreatedHighCancelled", type = Long.class),
						@ColumnResult(name = "casesCreatedMediumNew", type = Long.class),
						@ColumnResult(name = "casesCreatedMediumInProgress", type = Long.class),
						@ColumnResult(name = "casesCreatedMediumPending", type = Long.class),
						@ColumnResult(name = "casesCreatedMediumAssigned", type = Long.class),
						@ColumnResult(name = "casesCreatedMediumResolved", type = Long.class),
						@ColumnResult(name = "casesCreatedMediumCancelled", type = Long.class),
						@ColumnResult(name = "casesCreatedLowNew", type = Long.class),
						@ColumnResult(name = "casesCreatedLowInProgress", type = Long.class),
						@ColumnResult(name = "casesCreatedLowPending", type = Long.class),
						@ColumnResult(name = "casesCreatedLowAssigned", type = Long.class),
						@ColumnResult(name = "casesCreatedLowResolved", type = Long.class),
						@ColumnResult(name = "casesCreatedLowCancelled", type = Long.class),
						@ColumnResult(name = "casesAssignedCriticalNew", type = Long.class),
						@ColumnResult(name = "casesAssignedCriticalInProgress", type = Long.class),
						@ColumnResult(name = "casesAssignedCriticalPending", type = Long.class),
						@ColumnResult(name = "casesAssignedCriticalAssigned", type = Long.class),
						@ColumnResult(name = "casesAssignedCriticalResolved", type = Long.class),
						@ColumnResult(name = "casesAssignedCriticalCancelled", type = Long.class),
						@ColumnResult(name = "casesAssignedHighNew", type = Long.class),
						@ColumnResult(name = "casesAssignedHighInProgress", type = Long.class),
						@ColumnResult(name = "casesAssignedHighPending", type = Long.class),
						@ColumnResult(name = "casesAssignedHighAssigned", type = Long.class),
						@ColumnResult(name = "casesAssignedHighResolved", type = Long.class),
						@ColumnResult(name = "casesAssignedHighCancelled", type = Long.class),
						@ColumnResult(name = "casesAssignedMediumNew", type = Long.class),
						@ColumnResult(name = "casesAssignedMediumInProgress", type = Long.class),
						@ColumnResult(name = "casesAssignedMediumPending", type = Long.class),
						@ColumnResult(name = "casesAssignedMediumAssigned", type = Long.class),
						@ColumnResult(name = "casesAssignedMediumResolved", type = Long.class),
						@ColumnResult(name = "casesAssignedMediumCancelled", type = Long.class),
						@ColumnResult(name = "casesAssignedLowNew", type = Long.class),
						@ColumnResult(name = "casesAssignedLowInProgress", type = Long.class),
						@ColumnResult(name = "casesAssignedLowPending", type = Long.class),
						@ColumnResult(name = "casesAssignedLowAssigned", type = Long.class),
						@ColumnResult(name = "casesAssignedLowResolved", type = Long.class),
						@ColumnResult(name = "casesAssignedLowCancelled", type = Long.class),
						@ColumnResult(name = "servicesCriticalInprogress", type = Long.class),
						@ColumnResult(name = "servicesCriticalPending", type = Long.class),
						@ColumnResult(name = "servicesCriticalClosed", type = Long.class),
						@ColumnResult(name = "servicesCriticalCancelled", type = Long.class),
						@ColumnResult(name = "servicesHighInprogress", type = Long.class),
						@ColumnResult(name = "servicesHighPending", type = Long.class),
						@ColumnResult(name = "servicesHighClosed", type = Long.class),
						@ColumnResult(name = "servicesHighCancelled", type = Long.class),
						@ColumnResult(name = "servicesMediumInprogress", type = Long.class),
						@ColumnResult(name = "servicesMediumPending", type = Long.class),
						@ColumnResult(name = "servicesMediumClosed", type = Long.class),
						@ColumnResult(name = "servicesMediumCancelled", type = Long.class),
						@ColumnResult(name = "servicesLowInprogress", type = Long.class),
						@ColumnResult(name = "servicesLowPending", type = Long.class),
						@ColumnResult(name = "servicesLowClosed", type = Long.class),
						@ColumnResult(name = "servicesLowCancelled", type = Long.class), }) }) })

@NamedNativeQueries({
		@NamedNativeQuery(name = "User.userAgentsReport", query = "select tuser.id as id,tuser.login as login ,CONCAT(tuser.first_name,CONCAT(' ', tuser.last_name)) as name, "
				+ " (select COALESCE(sum(log.distance_travelled) , 0)  from trakeye_location_log log where  log.created_date_time between :fromTime and :toTime and user_id=tuser.id) as distance ,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime) as cases,"
				+ "(select count(distinct trservice.id)  from trakeye_trservice trservice where  trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime) as services from trakeye_user tuser where tuser.created_by=:createdBy", resultSetMapping = "userAgentReport"),
		@NamedNativeQuery(name = "User.userAgentReport", query = "select  tuser.id as id, tuser.login as login , CONCAT(tuser.first_name,CONCAT(' ', tuser.last_name)) as name, "
				+ " (select COALESCE(sum(log.distance_travelled) , 0) from trakeye_location_log log where  log.created_date_time between :fromTime and :toTime and user_id=tuser.id) as distance ,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime) as cases,"
				+ "(select count(distinct trservice.id)  from trakeye_trservice trservice where  trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime) as services from trakeye_user tuser where tuser.id=:userId", resultSetMapping = "userAgentReport"),
		@NamedNativeQuery(name = "User.userAgentDetailedReport", query = "select tuser.id as id,tuser.login as login  ,CONCAT(tuser.first_name,CONCAT(' ', tuser.last_name)) as name, "
				+ "(select COALESCE(sum(log.distance_travelled) , 0)   from trakeye_location_log log where  log.created_date_time between :fromTime and :toTime and user_id=tuser.id) as distance , "
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='NEW' and trcase.priority='CRITICAL') as casesCreatedCriticalNew, "
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='INPROGRESS' and trcase.priority='CRITICAL') as casesCreatedCriticalInProgress, "
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='PENDING' and trcase.priority='CRITICAL') as casesCreatedCriticalPending, "
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='ASSIGNED' and trcase.priority='CRITICAL') as casesCreatedCriticalAssigned, "
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='RESOLVED' and trcase.priority='CRITICAL') as casesCreatedCriticalResolved, "
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='CANCELLED' and trcase.priority='CRITICAL') as casesCreatedCriticalCancelled, "
				+

				"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='NEW' and trcase.priority='HIGH') as casesCreatedHighNew,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='INPROGRESS' and trcase.priority='HIGH') as casesCreatedHighInProgress,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='PENDING' and trcase.priority='HIGH') as casesCreatedHighPending,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='ASSIGNED' and trcase.priority='HIGH') as casesCreatedHighAssigned,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='RESOLVED' and trcase.priority='HIGH') as casesCreatedHighResolved,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='CANCELLED' and trcase.priority='HIGH') as casesCreatedHighCancelled,"
				+

				"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='NEW' and trcase.priority='MEDIUM') as casesCreatedMediumNew,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='INPROGRESS' and trcase.priority='MEDIUM') as casesCreatedMediumInProgress,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='PENDING' and trcase.priority='MEDIUM') as casesCreatedMediumPending,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='ASSIGNED' and trcase.priority='MEDIUM') as casesCreatedMediumAssigned,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='RESOLVED' and trcase.priority='MEDIUM') as casesCreatedMediumResolved,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='CANCELLED' and trcase.priority='MEDIUM') as casesCreatedMediumCancelled,"
				+

				"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='NEW' and trcase.priority='LOW') as casesCreatedLowNew,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='INPROGRESS' and trcase.priority='LOW') as casesCreatedLowInProgress,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='PENDING' and trcase.priority='LOW') as casesCreatedLowPending,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='ASSIGNED' and trcase.priority='LOW') as casesCreatedLowAssigned,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='RESOLVED' and trcase.priority='LOW') as casesCreatedLowResolved,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.reported_by_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='CANCELLED' and trcase.priority='LOW') as casesCreatedLowCancelled,"
				+

				"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='NEW' and trcase.priority='CRITICAL') as casesAssignedCriticalNew,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='INPROGRESS' and trcase.priority='CRITICAL') as casesAssignedCriticalInProgress,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='PENDING' and trcase.priority='CRITICAL') as casesAssignedCriticalPending,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='ASSIGNED' and trcase.priority='CRITICAL') as casesAssignedCriticalAssigned,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='RESOLVED' and trcase.priority='CRITICAL') as casesAssignedCriticalResolved,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='CANCELLED' and trcase.priority='CRITICAL') as casesAssignedCriticalCancelled,"
				+

				"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='NEW' and trcase.priority='HIGH') as casesAssignedHighNew,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='INPROGRESS' and trcase.priority='HIGH') as casesAssignedHighInProgress,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='PENDING' and trcase.priority='HIGH') as casesAssignedHighPending,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='ASSIGNED' and trcase.priority='HIGH') as casesAssignedHighAssigned,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='RESOLVED' and trcase.priority='HIGH') as casesAssignedHighResolved,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='CANCELLED' and trcase.priority='HIGH') as casesAssignedHighCancelled,"
				+

				"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='NEW' and trcase.priority='MEDIUM') as casesAssignedMediumNew,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='INPROGRESS' and trcase.priority='MEDIUM') as casesAssignedMediumInProgress,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='PENDING' and trcase.priority='MEDIUM') as casesAssignedMediumPending,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='ASSIGNED' and trcase.priority='MEDIUM') as casesAssignedMediumAssigned,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='RESOLVED' and trcase.priority='MEDIUM') as casesAssignedMediumResolved,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='CANCELLED' and trcase.priority='MEDIUM') as casesAssignedMediumCancelled,"
				+

				"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='NEW' and trcase.priority='LOW') as casesAssignedLowNew,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='INPROGRESS' and trcase.priority='LOW') as casesAssignedLowInProgress,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='PENDING' and trcase.priority='LOW') as casesAssignedLowPending,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='ASSIGNED' and trcase.priority='LOW') as casesAssignedLowAssigned,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='RESOLVED' and trcase.priority='LOW') as casesAssignedLowResolved,"
				+ "(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.assigned_to_id = tuser.id and trcase.create_date between :fromTime and :toTime and trcase.status='CANCELLED' and trcase.priority='LOW') as casesAssignedLowCancelled,"
				+

				"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='INPROGRESS' and trcase.priority='CRITICAL') as servicesCriticalInprogress ,"
				+ "(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='PENDING' and trcase.priority='CRITICAL') as servicesCriticalPending ,"
				+ "(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='CLOSED' and trcase.priority='CRITICAL') as servicesCriticalClosed ,"
				+ "(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='CANCELLED' and trcase.priority='CRITICAL') as servicesCriticalCancelled , "
				+

				"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='INPROGRESS' and trcase.priority='HIGH') as servicesHighInprogress ,"
				+ "(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='PENDING' and trcase.priority='HIGH') as servicesHighPending ,"
				+ "(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='CLOSED' and trcase.priority='HIGH') as servicesHighClosed ,"
				+ "(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='CANCELLED' and trcase.priority='HIGH') as servicesHighCancelled , "
				+

				"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='INPROGRESS' and trcase.priority='MEDIUM') as servicesMediumInprogress ,"
				+ "(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='PENDING' and trcase.priority='MEDIUM') as servicesMediumPending ,"
				+ "(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='CLOSED' and trcase.priority='MEDIUM') as servicesMediumClosed ,"
				+ "(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='CANCELLED' and trcase.priority='MEDIUM') as servicesMediumCancelled , "
				+

				"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='INPROGRESS' and trcase.priority='LOW') as servicesLowInprogress ,"
				+ "(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='PENDING' and trcase.priority='LOW') as servicesLowPending ,"
				+ "(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='CLOSED' and trcase.priority='LOW') as servicesLowClosed ,"
				+ "(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase  trcase  where trservice.tr_case_id= trcase.id and trservice.user_id =tuser.id and trservice.created_date between :fromTime and :toTime  and trservice.status='CANCELLED' and trcase.priority='LOW') as servicesLowCancelled  "
				+ "from trakeye_user tuser where tuser.id=:userId", resultSetMapping = "userAgentDetailedReport"), })
public class User extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	@Column(length = 50, unique = true, nullable = false)
	private String login;

	@JsonIgnore
	@NotNull
	@Size(min = 60, max = 60)
	@Column(name = "password_hash", length = 60)
	private String password;

	@Size(max = 50)
	@Column(name = "first_name", length = 50)
	private String firstName;

	@Size(max = 50)
	@Column(name = "last_name", length = 50)
	private String lastName;

	@Email
	@Size(max = 100)
	@Column(length = 100)
	private String email;

	@Size(max = 100)
	@Column(name = "phone", length = 100)
	private String phone;

	@Size(max = 100)
	@Column(name = "imei", length = 100)
	private String imei;

	@Size(max = 100)
	@Column(name = "operating_system", length = 100)
	private String operatingSystem;

	@Size(max = 100)
	@Column(name = "application_version", length = 100)
	private String applicationVersion;

	@NotNull
	@Column(nullable = false)
	private boolean activated = false;

	@Size(min = 2, max = 5)
	@Column(name = "lang_key", length = 5)
	private String langKey;

	@Size(max = 20)
	@Column(name = "activation_key", length = 20)
	@JsonIgnore
	private String activationKey;

	@Size(max = 150)
	@Column(name = "reset_key", length = 150)
	private String resetKey;

	@Column(name = "reset_date", nullable = true)
	private long resetDate;

	@ColumnDefault("1")
	@Column(name = "from_time")
	private int fromTime = 1;

	@ColumnDefault("1")
	@Column(name = "to_time")
	private int toTime = 1;

	@Column(name = "valid_Location", nullable = true)
	private String isValidLocation;

	@NotNull
	@Column(name = "gps_status")
	private Boolean gpsStatus = true;

	@Column(name = "fcm_token")
	private String fcmToken;
	
	@Type(type="org.hibernate.type.BinaryType") 
	@Column(columnDefinition = "LONGBLOB")
	private byte[] userImage;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "trakeye_user_authority", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "authority_name", referencedColumnName = "name") })
	private Set<Authority> authorities = new HashSet<>();

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	private TrakeyeType trakeyeType;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "trakeye_user_geofence", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "geofence_id", referencedColumnName = "id") })
	private Set<Geofence> geofences = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "trakeye_type_trakeye_attributevalue", joinColumns = {
			@JoinColumn(name = "trakeye_type_id") }, inverseJoinColumns = {
					@JoinColumn(name = "trakeye_type_attribute_value_id") })
	private Set<TrakeyeTypeAttributeValue> trakeyeTypeAttributeValues = new HashSet<>();
	
	@Size(max = 200)
	@Column(name = "collab_jwttoken")
	private String collabJwtToken;

	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public String getIsValidLocation() {
		return isValidLocation;
	}

	public void setIsValidLocation(String isValidLocation) {
		this.isValidLocation = isValidLocation;
	}

	public Set<TrakeyeTypeAttributeValue> getTrakeyeTypeAttributeValues() {
		return trakeyeTypeAttributeValues;
	}

	public void setTrakeyeTypeAttributeValues(Set<TrakeyeTypeAttributeValue> trakeyeTypeAttributeValues) {
		this.trakeyeTypeAttributeValues = trakeyeTypeAttributeValues;
	}

	public Set<Geofence> getGeofences() {
		return geofences;
	}

	public void setGeofences(Set<Geofence> geofences) {
		this.geofences = geofences;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public TrakeyeType getTrakeyeType() {
		return trakeyeType;
	}

	public void setTrakeyeType(TrakeyeType trakeyeType) {
		this.trakeyeType = trakeyeType;
	}
	@JsonIgnore
	@ManyToOne
	private Tenant tenant;
	

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	// Lowercase the login before saving it in database
	public void setLogin(String login) {
		if (login != null)
			this.login = login.toLowerCase(Locale.ENGLISH);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getActivationKey() {
		return activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	public String getResetKey() {
		return resetKey;
	}

	public void setResetKey(String resetKey) {
		this.resetKey = resetKey;
	}

	public long getResetDate() {
		return resetDate;
	}

	public void setResetDate(long resetDate) {
		this.resetDate = resetDate;
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	public int getFromTime() {
		return fromTime;
	}

	public void setFromTime(int fromTime) {
		this.fromTime = fromTime;
	}

	public int getToTime() {
		return toTime;
	}

	public void setToTime(int toTime) {
		this.toTime = toTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	public Boolean getGpsStatus() {
		return gpsStatus;
	}

	public void setGpsStatus(Boolean gpsStatus) {
		
		this.gpsStatus = gpsStatus;
	}
	
	

	public byte[] getUserImage() {
		return userImage;
	}

	public void setUserImage(byte[] userImage) {
		this.userImage = userImage;
	}
	
	

	

	public String getCollabJwtToken() {
		return collabJwtToken;
	}

	public void setCollabJwtToken(String collabJwtToken) {
		this.collabJwtToken = collabJwtToken;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		User user = (User) o;

		if (!login.equals(user.login)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return login.hashCode();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email + ", activated=" + activated + ", langKey=" + langKey
				+ ", activationKey=" + activationKey + ", resetKey=" + resetKey + ", resetDate=" + resetDate
				+ ", fromTime=" + fromTime + ", toTime=" + toTime + ", isValidLocation=" + isValidLocation
				 + ", phone=" + phone + ", imei=" + imei
				+ ", operatingSystem=" + operatingSystem + ", applicationVersion=" + applicationVersion + ", gpsStatus="
				+ gpsStatus + "]";
	}

}
