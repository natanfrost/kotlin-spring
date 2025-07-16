package com.coursecatalog.repository

import com.coursecatalog.entity.Course
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<Course, Int> {
    fun findByNameContaining(courseName: String): List<Course>
}