package com.coursecatalog.service

import com.coursecatalog.dto.CourseDTO
import com.coursecatalog.entity.Course
import com.coursecatalog.exception.CourseNotFoundException
import com.coursecatalog.exception.InstructorNotValidException
import com.coursecatalog.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(
    val courseRepository: CourseRepository,
    val instructorService: InstructorService
) {
    companion object : KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO {

        val instructorOptional = instructorService.findByInstructorId(courseDTO.instructorId)

        if (!instructorOptional.isPresent) {
            throw InstructorNotValidException("Instructor not valid : ${courseDTO.instructorId}")
        }

        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category, instructorOptional.get())
        }
        courseRepository.save(courseEntity)
        logger.info("Saved course id: $courseEntity")
        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category, it.instructor!!.id)
        }
    }

    fun getAll(): List<CourseDTO> {
        return courseRepository.findAll()
            .map {
                CourseDTO(it.id, it.name, it.category)
            }
    }

    fun updateCourse(courseDTO: CourseDTO, id: Int): CourseDTO {
        val existingCourse = courseRepository.findById((id))
        return if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    it.name = courseDTO.name
                    it.category = courseDTO.category
                    courseRepository.save(it)
                    CourseDTO(it.id, it.name, it.category)
                }
        } else {
            throw CourseNotFoundException("No course found for the passed ")
        }
    }

    fun deleteCourse(courseId: Int) {
        val existingCourse = courseRepository.findById(courseId)
        if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    courseRepository.deleteById(courseId)
                }
        } else {
            throw CourseNotFoundException("No course found for the passed courseId : $courseId")
        }
    }

    fun getAllByName(courseName: String?): List<CourseDTO> {
        val courses = courseName?.let {
            courseRepository.findByNameContaining(courseName)
        } ?: courseRepository.findAll()

        return courses.map {
            CourseDTO(it.id, it.name, it.category)
        }
    }
}
