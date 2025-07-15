package com.course_catalog.course_catalog_service.repository

import com.course_catalog.course_catalog_service.entity.Instructor
import org.springframework.data.repository.CrudRepository

interface InstructorRepository : CrudRepository<Instructor, Int> {

}
