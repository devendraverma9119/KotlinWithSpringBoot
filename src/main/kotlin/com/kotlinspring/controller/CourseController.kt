package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.service.CourseService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/courses")
@Validated
class CourseController (val courseService: CourseService){

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody @Valid courseDTO: CourseDTO):CourseDTO{
       return courseService.addCourse(courseDTO)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun retrieveCourses(@RequestParam("course_name", required = false) name:String?): List<CourseDTO> {
        return courseService.retrieveCourses(name)
    }

//    @GetMapping("/{course_id}")
//    @ResponseStatus(HttpStatus.OK)
//    fun retrieveCourse(@PathVariable("course_id") id:Int): Optional<CourseDTO>? {
//        return courseService.retrieveCourse(id)
//    }


    @PutMapping("/{course_id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateCourse(@RequestBody @Valid courseDTO: CourseDTO,@PathVariable("course_id") id:Int): CourseDTO {
        return courseService.updateCourse(courseDTO,id)
    }

    @DeleteMapping("/{course_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourse(@PathVariable("course_id") id:Int) {
        courseService.deleteCourse(id)
    }

}
