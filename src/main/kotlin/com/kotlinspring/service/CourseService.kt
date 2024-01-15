package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.exception.CourseNotFoundException
import com.kotlinspring.exception.InstructorNotValidException
import com.kotlinspring.repository.CourseRepository
import com.kotlinspring.repository.InstructorRepository
import mu.KLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class CourseService(val courseRepository: CourseRepository, val instructorService: InstructorService) {
    companion object : KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO {

        val instructor = instructorService.findById(courseDTO.instructorId!!)

        if (!instructor.isPresent) {
            throw InstructorNotValidException("instructor id not found")
        }

        val courseEntity: Course = courseDTO.let {
            Course(null, it.name, it.category,instructor.get())
        }
        courseRepository.save(courseEntity)
        //logger.info("saves course is $courseEntity")
        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category,it.instructor!!.id)
        }
    }

    fun retrieveCourses(courseName: String?): List<CourseDTO> {
        var courses = courseName?.let {
            courseRepository.findCoursesByName(it)
        } ?: courseRepository.findAll()

        return courses.map {
            CourseDTO(it.id, it.name, it.category)
        }
    }

    fun retrieveCourse(id: Int): Optional<CourseDTO>? {
        return courseRepository.findById(id).map {
            CourseDTO(it.id, it.name, it.category)
        }
    }

    fun updateCourse(courseDTO: CourseDTO, id: Int): CourseDTO {
        val existingCourse = courseRepository.findById(id)
        return if (existingCourse.isPresent) {
            existingCourse.get().let {
                it.name = courseDTO.name
                it.category = courseDTO.category
                courseRepository.save(it)
                CourseDTO(id, courseDTO.name, courseDTO.category)
            }
        } else {
            throw CourseNotFoundException("course not found with id ${id}")
        }
    }

    fun deleteCourse(id: Int) {
        val existingCourse = courseRepository.findById(id)
        return if (existingCourse.isPresent) {
            existingCourse.get().let {
                courseRepository.deleteById(id)
            }
        } else {
            throw CourseNotFoundException("course not found with id ${id}")
        }
    }

    fun getCoursesByName(name: String): List<CourseDTO>? {
        return courseRepository.findCoursesByName(name).map {
            CourseDTO(it.id, it.name, it.category)
        }
    }
}
