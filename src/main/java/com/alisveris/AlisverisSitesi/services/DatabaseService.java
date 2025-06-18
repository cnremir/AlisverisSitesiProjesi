package com.alisveris.AlisverisSitesi.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseService {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void disableForeignKeyChecks() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0;");
    }

    @Transactional
    public void enableForeignKeyChecks() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1;");
    }

    @Transactional
    public void deleteUserByUserId(Integer userId) {
        // Foreign key checks devre dışı bırak
        disableForeignKeyChecks();

        try {
            // Kullanıcıyı sil
            jdbcTemplate.update("DELETE FROM product WHERE user_id = ?", userId);
            jdbcTemplate.update("DELETE FROM user WHERE user_id = ?", userId);
            jdbcTemplate.update("DELETE FROM offer WHERE user_id =?",userId);

        } finally {
            // Foreign key checks tekrar etkinleştir
            enableForeignKeyChecks();
        }
    }
}
