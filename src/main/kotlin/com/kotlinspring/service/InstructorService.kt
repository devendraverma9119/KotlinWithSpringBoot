package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.entity.Instructor
import com.kotlinspring.repository.InstructorRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InstructorService(val instructorRepository: InstructorRepository) {

    fun addInstructor(instructorDTO: InstructorDTO): InstructorDTO {
        val instructorEntity: Instructor = instructorDTO.let{
            Instructor(it.id,it.name)
        }
        instructorRepository.save(instructorEntity)


        return instructorEntity.let{
            InstructorDTO(it.id,it.name)
        }
    }

    fun findById(instructorId: Int): Optional<Instructor> {
        return instructorRepository.findById(instructorId)
    }

}
