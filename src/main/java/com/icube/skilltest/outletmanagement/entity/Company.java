/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icube.skilltest.outletmanagement.entity;

import java.util.Date;

/**
 *
 * @author User
 */
public class Company  implements java.io.Serializable {
    private Long id;
    private Long parentBranch;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private java.util.Date created;
    private String operatingHours;
    private String status;

    public Company() {
    }

    public Company(Long id) {
        this.id = id;
    }

    public Company(String name, String address, Date created, String operatingHours, String status) {
        this.name = name;
        this.address = address;
        this.created = created;
        this.operatingHours = operatingHours;
        this.status = status;
    }

    public Company(String name, String address, Double latitude, Double longitude, Date created, String operatingHours, String status) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.created = created;
        this.operatingHours = operatingHours;
        this.status = status;
    }

    public Company(Long parentBranch, String name, String address, Double latitude, Double longitude, Date created, String operatingHours, String status) {
        this.parentBranch = parentBranch;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.created = created;
        this.operatingHours = operatingHours;
        this.status = status;
    }

    public Company(Long id, Long parentBranch, String name, String address, Double latitude, Double longitude, Date created, String operatingHours, String status) {
        this.id = id;
        this.parentBranch = parentBranch;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.created = created;
        this.operatingHours = operatingHours;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentBranch() {
        return parentBranch;
    }

    public void setParentBranch(Long parentBranch) {
        this.parentBranch = parentBranch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
}
