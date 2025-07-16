package com.coursecatalog.service

import com.coursecatalog.dto.InstructorDTO
import com.coursecatalog.entity.Instructor
import com.coursecatalog.repository.InstructorRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InstructorService(val instructorRepository: InstructorRepository) {
    fun addInstructor(instructorDTO: InstructorDTO): InstructorDTO {
        val instructorEntity = instructorDTO.let {
            Instructor(it.id, it.name)
        }

        instructorRepository.save(instructorEntity)

        return instructorEntity.let {
            InstructorDTO(it.id, it.name)
        }
    }

    fun findByInstructorId(id: Int?): Optional<Instructor?> {
        return instructorRepository.findById(id!!);
    }
}
