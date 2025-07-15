package com.course_catalog.course_catalog_service.dto;

import com.course_catalog.course_catalog_service.entity.Instructor
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.NotBlank

data class CourseDTO(
    val id: Int?,
    @get:NotBlank(message = "courseDTO.name must not be blank")
    val name: String,
    @get:NotBlank(message = "courseDTO.category must not be blank")
    val category: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="INSTRUCTOR_ID", nullable = false)
    val instructor: Instructor? =null
);

