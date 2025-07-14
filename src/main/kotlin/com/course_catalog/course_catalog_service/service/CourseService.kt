package com.course_catalog.course_catalog_service.service

import com.course_catalog.course_catalog_service.dto.CourseDTO
import com.course_catalog.course_catalog_service.entity.Course
import com.course_catalog.course_catalog_service.exception.CourseNotFoundException
import com.course_catalog.course_catalog_service.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(val courseRepository: CourseRepository) {
    companion object : KLogging()
    fun addCourse(courseDTO: CourseDTO) : CourseDTO {
        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category, )
        }
        courseRepository.save(courseEntity)
        logger.info("Saved course id: $courseEntity")
        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category)
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
}
