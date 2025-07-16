package com.coursecatalog.controller

import com.coursecatalog.dto.CourseDTO
import com.coursecatalog.service.CourseService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/courses")
@Validated
class CourseController(val courseService: CourseService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody @Valid courseDTO: CourseDTO): CourseDTO {
        return courseService.addCourse(courseDTO)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCourses(): List<CourseDTO> {
        return courseService.getAll();
    }

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    fun getByName(@RequestParam("course_name", required = false) courseName: String?): List<CourseDTO> {
        return courseService.getAllByName(courseName);
    }

    @PutMapping("/{course_id}")
    fun updateCourse(
        @RequestBody courseDTO: CourseDTO,
        @PathVariable("course_id") courseId: Int
    ) = courseService.updateCourse(courseDTO, courseId)

    @DeleteMapping("/{course_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourse(@PathVariable("course_id") courseId: Int) =
        courseService.deleteCourse(courseId)
}