package com.tresbu.trakeye.domain;


import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Geofence.
 */
@Entity
@Table(name = "trakeye_geofence" ,uniqueConstraints=
@UniqueConstraint(name="usergeofence",columnNames={"tenant_id","user_id", "name"}))
@SqlResultSetMappings({
@SqlResultSetMapping(
	    name="geofenceReport",
	    classes={
	        @ConstructorResult(
	            targetClass=com.tresbu.trakeye.service.dto.ReportDTO.class,
	            columns={
	            		@ColumnResult(name = "id", type = Long.class),
	            		@ColumnResult(name = "name", type = String.class),
	                    @ColumnResult(name = "cases", type = Long.class),
	                    @ColumnResult(name = "services", type = Long.class),
	            }
	        )
	    }
	),
@SqlResultSetMapping(
	    name="geofenceDetailedReport",
	    classes={
	        @ConstructorResult(
	            targetClass=com.tresbu.trakeye.service.dto.ReportDTO.class,
	            columns={
	            		@ColumnResult(name = "id", type = Long.class),
	            		@ColumnResult(name = "name", type = String.class),
	            		@ColumnResult(name = "coordinates", type = String.class),
	                    @ColumnResult(name = "casesCriticalNew", type = Long.class),
	                    @ColumnResult(name = "casesCriticalInProgress", type = Long.class),
	                    @ColumnResult(name = "casesCriticalPending", type = Long.class),
	                    @ColumnResult(name = "casesCriticalAssigned", type = Long.class),
	                    @ColumnResult(name = "casesCriticalResolved", type = Long.class),
	                    @ColumnResult(name = "casesCriticalCancelled", type = Long.class),
	                    @ColumnResult(name = "casesHighNew", type = Long.class),
	                    @ColumnResult(name = "casesHighInProgress", type = Long.class),
	                    @ColumnResult(name = "casesHighPending", type = Long.class),
	                    @ColumnResult(name = "casesHighAssigned", type = Long.class),
	                    @ColumnResult(name = "casesHighResolved", type = Long.class),
	                    @ColumnResult(name = "casesHighCancelled", type = Long.class),
	                    @ColumnResult(name = "casesMediumNew", type = Long.class),
	                    @ColumnResult(name = "casesMediumInProgress", type = Long.class),
	                    @ColumnResult(name = "casesMediumPending", type = Long.class),
	                    @ColumnResult(name = "casesMediumAssigned", type = Long.class),
	                    @ColumnResult(name = "casesMediumResolved", type = Long.class),
	                    @ColumnResult(name = "casesMediumCancelled", type = Long.class),
	                    @ColumnResult(name = "casesLowNew", type = Long.class),
	                    @ColumnResult(name = "casesLowInProgress", type = Long.class),
	                    @ColumnResult(name = "casesLowPending", type = Long.class),
	                    @ColumnResult(name = "casesLowAssigned", type = Long.class),
	                    @ColumnResult(name = "casesLowResolved", type = Long.class),
	                    @ColumnResult(name = "casesLowCancelled", type = Long.class),
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
	                    @ColumnResult(name = "servicesLowCancelled", type = Long.class),
	                    
	                   
	                   
	            }
	        )
	    }
	)
})
	
@NamedNativeQueries({
@NamedNativeQuery(name="Geofence.geofencesReport", query="select geofence.id as id, geofence.name as name , "+
					"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime) as cases,"+
					"(select count(distinct trservice.id)  from trakeye_trservice trservice where trservice.tr_case_id in (select trcase.id from trakeye_trcase  trcase where trcase.geofence_id = geofence.id) and trservice.created_date between :fromTime and :toTime) as services from trakeye_geofence geofence where geofence.user_id=:userId", resultSetMapping="geofenceReport"),
@NamedNativeQuery(name="Geofence.geofenceReport", query="select geofence.id as id, geofence.name as name , "+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime) as cases,"+
		"(select count(distinct trservice.id)  from trakeye_trservice trservice where  trservice.tr_case_id in (select trcase.id from trakeye_trcase  trcase where trcase.geofence_id = geofence.id) and trservice.created_date between :fromTime and :toTime) as services from trakeye_geofence geofence where geofence.id=:geofenceId", resultSetMapping="geofenceReport"),
@NamedNativeQuery(name="Geofence.geofenceDetailedReport", query="select geofence.id as id, geofence.name as name , geofence.coordinates as coordinates,"+
		
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime and trcase.status='NEW' and trcase.priority='CRIRICAL') as casesCriticalNew,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime and trcase.status='INPROGRESS' and trcase.priority='CRIRICAL') as casesCriticalInProgress,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime and trcase.status='PENDING' and trcase.priority='CRIRICAL') as casesCriticalPending,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime and trcase.status='ASSIGNED' and trcase.priority='CRIRICAL') as casesCriticalAssigned,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime and trcase.status='RESOLVED' and trcase.priority='CRIRICAL') as casesCriticalResolved,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime and trcase.status='CANCELLED' and trcase.priority='CRIRICAL') as casesCriticalCancelled,"+
		
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='NEW' and trcase.priority='HIGH') as CasesHighNew,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='INPROGRESS' and trcase.priority='HIGH') as CasesHighInProgress,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='PENDING' and trcase.priority='HIGH') as CasesHighPending,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='ASSIGNED' and trcase.priority='HIGH') as CasesHighAssigned,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='RESOLVED' and trcase.priority='HIGH') as CasesHighResolved,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='CANCELLED' and trcase.priority='HIGH') as CasesHighCancelled,"+
		
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='NEW' and trcase.priority='MEDIUM') as CasesMediumNew,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='INPROGRESS' and trcase.priority='MEDIUM') as CasesMediumInProgress,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='PENDING' and trcase.priority='MEDIUM') as CasesMediumPending,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='ASSIGNED' and trcase.priority='MEDIUM') as CasesMediumAssigned,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='RESOLVED' and trcase.priority='MEDIUM') as CasesMediumResolved,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='CANCELLED' and trcase.priority='MEDIUM') as CasesMediumCancelled,"+
		
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='NEW' and trcase.priority='LOW') as CasesLowNew,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='INPROGRESS' and trcase.priority='LOW') as CasesLowInProgress,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='PENDING' and trcase.priority='LOW') as CasesLowPending,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='ASSIGNED' and trcase.priority='LOW') as CasesLowAssigned,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='RESOLVED' and trcase.priority='LOW') as CasesLowResolved,"+
		"(select count(distinct trcase.id) from trakeye_trcase  trcase where trcase.geofence_id = geofence.id and trcase.create_date between :fromTime and :toTime  and trcase.status='CANCELLED' and trcase.priority='LOW') as CasesLowCancelled,"+
		
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime  and  trcase.geofence_id = geofence.id  and trcase.priority='CRITICAL' and trservice.status='INPROGRESS') as servicesCriticalInprogress,"+
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='CRITICAL' and trservice.status='PENDING') as servicesCriticalPending,"+
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='CRITICAL' and trservice.status='CLOSED') as servicesCriticalClosed,"+
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='CRITICAL' and trservice.status='CANCELLED') as servicesCriticalCancelled,"+
		
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='HIGH' and trservice.status='INPROGRESS') as servicesHighInprogress,"+
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='HIGH' and trservice.status='PENDING') as servicesHighPending,"+
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='HIGH' and trservice.status='CLOSED') as servicesHighClosed,"+
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='HIGH' and trservice.status='CANCELLED') as servicesHighCancelled,"+
		
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='MEDIUM' and trservice.status='INPROGRESS') as servicesMediumInprogress,"+
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='MEDIUM' and trservice.status='PENDING') as servicesMediumPending,"+
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='MEDIUM' and trservice.status='CLOSED') as servicesMediumClosed,"+
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='MEDIUM' and trservice.status='CANCELLED') as servicesMediumCancelled,"+
		
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='LOW' and trservice.status='INPROGRESS') as servicesLowInprogress,"+
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='LOW' and trservice.status='PENDING') as servicesLowPending,"+
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='LOW' and trservice.status='CLOSED') as servicesLowClosed,"+
		"(select count(distinct trservice.id)  from trakeye_trservice trservice , trakeye_trcase trcase where  trservice.tr_case_id = trcase.id  and  trservice.created_date between :fromTime and :toTime    and  trcase.geofence_id = geofence.id  and trcase.priority='LOW' and trservice.status='CANCELLED') as servicesLowCancelled"+
	
		 " from trakeye_geofence geofence where geofence.id=:geofenceId", resultSetMapping="geofenceDetailedReport")})
public class Geofence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "coordinates",  nullable = false,columnDefinition="TEXT")
    private String coordinates;

    @Column(name = "created_date")
    private long createdDate;

    @Column(name = "modified_date")
    private long modifiedDate;

    @ManyToOne
    @JoinColumn(nullable=false)
    private User user;
    
    @JsonIgnore
	@ManyToOne
	private Tenant tenant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Geofence name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Geofence description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public Geofence coordinates(String coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public Geofence createdDate(long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public Geofence modifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public User getUser() {
        return user;
    }

    public Geofence user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    

    public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Geofence geofence = (Geofence) o;
        if(geofence.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, geofence.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Geofence{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", coordinates='" + coordinates + "'" +
            ", createdDate='" + createdDate + "'" +
            ", modifiedDate='" + modifiedDate + "'" +
            '}';
    }
}
