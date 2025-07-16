package com.coursecatalog.controller

import com.coursecatalog.dto.InstructorDTO
import com.coursecatalog.service.InstructorService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/instructors")
@Validated
class InstructorController(val instructorService: InstructorService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addInstructor(@RequestBody @Valid instructorDTO: InstructorDTO): InstructorDTO {
        return instructorService.addInstructor(instructorDTO)
    }
}