package com.DBMS.project.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class OfferRowMapper implements RowMapper<Offer> {
    public Offer mapRow(ResultSet rs, int i) throws SQLException {
        Offer offer = new Offer();
        offer.setId(rs.getLong("id"));
        offer.setDiscount(rs.getLong("discount"));
        offer.setPointsRequired(rs.getLong("pointsRequired"));
        offer.setMaintainer(rs.getLong("maintainer"));
        return offer;
    }
}
