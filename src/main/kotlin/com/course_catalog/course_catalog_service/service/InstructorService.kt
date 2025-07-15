package com.course_catalog.course_catalog_service.service

import com.course_catalog.course_catalog_service.dto.InstructorDTO
import com.course_catalog.course_catalog_service.entity.Instructor
import com.course_catalog.course_catalog_service.repository.InstructorRepository
import org.springframework.stereotype.Service

@Service
class InstructorService(val instructorRepository : InstructorRepository) {
    fun addInstructor(instructorDTO: InstructorDTO) : InstructorDTO {
        val instructorEntity = instructorDTO.let {
            Instructor(it.id, it.name)
        }

        instructorRepository.save(instructorEntity)

        return instructorEntity.let {
            InstructorDTO(it.id, it.name)
        }
    }
}
