package com.course_catalog.course_catalog_service.dto

import com.course_catalog.course_catalog_service.entity.Course
import jakarta.persistence.CascadeType
import jakarta.persistence.OneToMany
import jakarta.validation.constraints.NotBlank

data class InstructorDTO(
    val id: Int?,
    @get:NotBlank(message = "instructorDTO.name must not be blank")
    var name: String,
    @OneToMany(
        mappedBy = "instructor",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var courses : List<Course> = mutableListOf()
)
