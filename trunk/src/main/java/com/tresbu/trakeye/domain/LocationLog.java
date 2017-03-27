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
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;

import com.tresbu.trakeye.domain.enumeration.LogSource;

/**
 * A LocationLog.
 */
@Entity
@Table(name = "trakeye_location_log",uniqueConstraints=
@UniqueConstraint(name="location_log_unique",columnNames={"user_id", "created_date_time","updated_date_time"}))
@SqlResultSetMapping(
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
	            }
	        )
	    }
	)

	

@NamedNativeQuery(name="LocationLog.getLiveLogs", query="select log.*,usr.login,usr.id as userid, usr.user_image as userImage,"
						+"case "
						+" when log.created_date_time >=:dateTime and log.updated_date_time = 0 then 'ACTIVE'"
						+" when  log.updated_date_time >=:dateTime then 'IDLE' "
						+" else  'INACTIVE' "
						+" END as status "
						+" from trakeye_location_log log , trakeye_user usr ,"
						+" (select log1.user_id uid,max(GREATEST(log1.updated_date_time,log1.created_date_time)) mtime from trakeye_location_log log1, trakeye_user usr1"
						+" where log1.user_id = usr1.id and usr1.id in (select id from  trakeye_user where ((login = :userName  or created_by= :userName) and "
						+ " ( (:hour >= from_time and :hour <= to_time and from_time<=to_time) or (NOT (:hour < from_time and :hour > to_time) and from_time>=to_time) ) ) ) group by log1.user_id ) res "
						+" where log.user_id = usr.id and res.mtime=GREATEST(log.updated_date_time,log.created_date_time) and uid=log.user_id and usr.activated=1 ", resultSetMapping="liveLogMapping")


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
