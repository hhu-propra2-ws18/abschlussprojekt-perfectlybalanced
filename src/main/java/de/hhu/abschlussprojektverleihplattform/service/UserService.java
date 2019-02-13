package de.hhu.abschlussprojektverleihplattform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private JdbcTemplate jtm;
    
}