package com.course_catalog.course_catalog_service.repository

import com.course_catalog.course_catalog_service.entity.Course
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<Course, Int> {
    fun findByNameContaining(courseName : String) : List<Course>
}