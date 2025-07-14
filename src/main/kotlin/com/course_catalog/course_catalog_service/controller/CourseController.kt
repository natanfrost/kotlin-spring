package com.course_catalog.course_catalog_service.controller

import com.course_catalog.course_catalog_service.dto.CourseDTO
import com.course_catalog.course_catalog_service.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/courses")
class CourseController(val courseService: CourseService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody courseDTO: CourseDTO): CourseDTO {
        return courseService.addCourse(courseDTO)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCourses() : List<CourseDTO> {
        return courseService.getAll();
    }
}