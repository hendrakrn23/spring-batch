package com.example.demo.controller;

import com.example.demo.entity.Country;
import com.example.demo.repository.CountryRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@OpenAPIDefinition(
        info = @Info(
                title = "My API",
                version = "v1",
                description = "This is the API documentation"
        )
)
public class WorldController {

    @Autowired
    CountryRepository countryRepository;

    @GetMapping("/inquiry-all-country")
    @Operation(summary = "Get a greeting message", description = "Returns a simple greeting message")
    public ResponseEntity inquiryAllCountry(){

        List<Country> list= countryRepository.findAll();
        return ResponseEntity.ok(list);
    }
}
