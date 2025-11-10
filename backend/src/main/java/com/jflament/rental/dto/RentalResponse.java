package com.jflament.rental.dto;

import com.jflament.rental.entity.Rental;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
public class RentalResponse {
    private Long id;
    private String name;
    private BigDecimal surface;
    private BigDecimal price;
    private String picture;
    private String description;
    private Long ownerId;
    private String createdAt;
    private String updatedAt;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public RentalResponse(Rental rental) {
        this.id = rental.getId();
        this.name = rental.getName();
        this.surface = rental.getSurface();
        this.price = rental.getPrice();
        this.picture = rental.getPicture();
        this.description = rental.getDescription();
        this.ownerId = rental.getOwner().getId();
        this.createdAt = rental.getCreatedAt() != null ? rental.getCreatedAt().format(FORMATTER) : null;
        this.updatedAt = rental.getUpdatedAt() != null ? rental.getUpdatedAt().format(FORMATTER) : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSurface() {
        return surface;
    }

    public void setSurface(BigDecimal surface) {
        this.surface = surface;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

