package com.tresbu.trakeye.domain;


import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tresbu.trakeye.domain.enumeration.LogSource;

/**
 * A LocationLog.
 */
@Entity
@Table(name = "trakeye_location_log",uniqueConstraints=
@UniqueConstraint(name="location_log_unique",columnNames={"user_id", "created_date_time","updated_date_time"}))
@SqlResultSetMappings({ @SqlResultSetMapping(
	    name="liveLogMapping",
	    classes={
	        @ConstructorResult(
	            targetClass=com.tresbu.trakeye.domain.LiveLogs.class,
	            columns={
	            		@ColumnResult(name = "latitude", type = Double.class),
	            		@ColumnResult(name = "longitude", type = Double.class),
	                    @ColumnResult(name = "address", type = String.class),
	                    @ColumnResult(name = "login", type = String.class),
	                    @ColumnResult(name = "STATUS", type = String.class),
	            		@ColumnResult(name = "userid", type = Long.class),
	            		@ColumnResult(name = "userImage", type = byte[].class),
	            		@ColumnResult(name = "phone", type = String.class),
	            		@ColumnResult(name = "battery_percentage", type = int.class),
	            }
	        )
	    }
	),

@SqlResultSetMapping(
	    name="batterydataMapping",
	    classes={
	        @ConstructorResult(
	            targetClass=com.tresbu.trakeye.service.dto.BatteryReportDTO.class,
	            columns={
	            		@ColumnResult(name = "battery_percentage", type = int.class),
	            		@ColumnResult(name = "created_date_time", type = Long.class),
	            }
	        )
	    }
	),

@SqlResultSetMapping(
	    name="distancedataMapping",
	    classes={
	        @ConstructorResult(
	            targetClass=com.tresbu.trakeye.service.dto.DistanceReportDTO.class,
	            columns={
	            		@ColumnResult(name = "distance", type = Double.class),
	            		@ColumnResult(name = "date", type = String.class),
	            }
	        )
	    }
	),

})



	
@NamedNativeQueries({
@NamedNativeQuery(name="LocationLog.getLiveLogs", query="select log.*,usr.login,usr.id as userid, usr.user_image as userImage,usr.phone, "
						+"case "
						+" when log.created_date_time >=:dateTime and log.updated_date_time = 0 then 'ACTIVE'"
						+" when  log.updated_date_time >=:dateTime then 'IDLE' "
						+" else  'INACTIVE' "
						+" END as status "
						+" from trakeye_location_log log , trakeye_user usr ,"
						+" (select log1.user_id uid,max(GREATEST(log1.updated_date_time,log1.created_date_time)) mtime from trakeye_location_log log1, trakeye_user usr1"
						+" where log1.user_id = usr1.id and usr1.id in (select id from get_allusers(:userName,:tenantID)) and "
						+ " ( (:hour >= from_time and :hour <= to_time and from_time<=to_time) or (NOT (:hour < from_time and :hour > to_time) and from_time>=to_time) )  group by log1.user_id ) res "
						+" where log.user_id = usr.id and res.mtime=GREATEST(log.updated_date_time,log.created_date_time) and uid=log.user_id and usr.activated=true ", resultSetMapping="liveLogMapping"),
@NamedNativeQuery(name="LocationLog.findBatteryDetails", query="select log.*"
                 +" FROM trakeye_location_log log where log.user_id = :userid and log.created_date_time between :fromDate and :toDate order by log.created_date_time asc", resultSetMapping="batterydataMapping"),
@NamedNativeQuery(name="LocationLog.getDistanceReport", query=" select a.distance as distance,  date from (select sum(distance_travelled)"
		+ " as distance,CAST(TO_TIMESTAMP(created_date_time/1000) AS DATE) as date "
        +" FROM trakeye_location_log log where log.user_id = :userid and log.created_date_time between :fromDate and :toDate "
        + " group by CAST(TO_TIMESTAMP(created_date_time/1000) AS DATE) order by CAST(TO_TIMESTAMP(created_date_time/1000) AS DATE) asc) as a", resultSetMapping="distancedataMapping")
})

public class LocationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "latitude", nullable = false,precision=15, scale=3)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false,precision=15, scale=3)
    private Double longitude;

    @Column(name = "address")
    private String address;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "log_source", nullable = false)
    private LogSource logSource;
    
    @ColumnDefault("0")
    @NotNull
    @Column(name = "distance_travelled", nullable = false,precision=15, scale=3)
    private Double distanceTravelled;
    
    @ColumnDefault("0")
    @Column(name = "battery_percentage")
	private int batteryPercentage;
    
    @JsonIgnore
	@ManyToOne
	private Tenant tenant;

    public long getCreatedDateTime() {
		return createdDateTime;
	}



	public void setCreatedDateTime(long createdDateTime) {
		this.createdDateTime = createdDateTime;
	}



	public long getUpdatedDateTime() {
		return updatedDateTime;
	}



	public void setUpdatedDateTime(long updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

    @Column(name = "created_date_time", nullable = false)
    private long createdDateTime;
    
    
    @Column(name = "updated_date_time", nullable = false)
    private long updatedDateTime;

	@ManyToOne
	private User user;

    public Long getId() {
        return id;
    }
    
  

    public void setId(Long id) {
        this.id = id;
    }

   

    public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
        return address;
    }

    public LocationLog address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LogSource getLogSource() {
        return logSource;
    }

    public LocationLog logSource(LogSource logSource) {
        this.logSource = logSource;
        return this;
    }

    public void setLogSource(LogSource logSource) {
        this.logSource = logSource;
    }

   

	public Double getDistanceTravelled() {
		return distanceTravelled;
	}



	public void setDistanceTravelled(Double distanceTravelled) {
		this.distanceTravelled = distanceTravelled;
	}


	public int getBatteryPercentage() {
		return batteryPercentage;
	}



	public void setBatteryPercentage(int batteryPercentage) {
		this.batteryPercentage = batteryPercentage;
	}



	public User getUser() {
        return user;
    }

    public LocationLog user(User user) {
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
        LocationLog locationLog = (LocationLog) o;
        if(locationLog.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, locationLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LocationLogs{" +
            "id=" + id +
            ", latitude='" + latitude + "'" +
            ", longitude='" + longitude + "'" +
            ", address='" + address + "'" +
            ", logSource='" + logSource + "'" +
            ", createdDateTime='" + createdDateTime + "'" +
            ", userName='"+user.getLogin()+"'"+
            ", userId='"+user.getId()+"'"+
            ", distanceTravelled='"+distanceTravelled+"'"+
            ", batteryPercentage='"+batteryPercentage+"'"+
            '}';
    }
}
