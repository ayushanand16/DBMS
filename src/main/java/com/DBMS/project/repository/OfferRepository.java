package com.DBMS.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import com.DBMS.project.model.Offer;
import com.DBMS.project.model.OfferRowMapper;

@Repository
public class OfferRepository {
        private final JdbcTemplate jdbcTemplate;

        public OfferRepository(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        public List<Offer> getAll() {
            return jdbcTemplate.query("SELECT * FROM offers",new BeanPropertyRowMapper<>(Offer.class));
        }
        @Deprecated
        public Optional<Offer> getOffer(Long id) {
            List<Offer> offer = jdbcTemplate.query("SELECT * FROM offers where id = ?", new Object[]{id}, new OfferRowMapper());
            return (offer.isEmpty() ? Optional.empty() : Optional.of(offer.get(0)));
        }

        public void save(Offer offer) {
            jdbcTemplate.update("INSERT INTO offers (pointsRequired, discount, maintainer) values (?,?,?)",offer.getPointsRequired(),offer.getDiscount(),offer.getMaintainer());
        }

        public void deleteOffer(Offer offer) {
            jdbcTemplate.update("DELETE FROM offers where id = ?", offer.getId());
        }

        public void edit(Offer offers) {
        jdbcTemplate.update("update offers set pointsRequired = ?, discount = ? where id = ?",offers.getPointsRequired(),offers.getDiscount(), offers.getId());
    }
}
