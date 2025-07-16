package com.coursecatalog.repository

import com.coursecatalog.entity.Instructor
import org.springframework.data.repository.CrudRepository

interface InstructorRepository : CrudRepository<Instructor, Int> {

}
