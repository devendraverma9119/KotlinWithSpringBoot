package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.service.CourseService
import com.kotlinspring.util.courseDTO
import com.kotlinspring.util.courseEntityList
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

@Autowired
lateinit var webTestClient: WebTestClient

@MockkBean
lateinit var courseServiceMockk : CourseService



@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {


    @Test
    fun addCourse() {

        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin"
            ,"Dilip Sundarraj")

        every { courseServiceMockk.addCourse(any()) } returns courseDTO(id = 1)

        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            savedCourseDTO!!.id != null
        }
    }

//    @Test
//    fun addCourse_validation() {
//
//        val courseDTO = CourseDTO(null, ""
//            ,"")
//
//        every { courseServiceMockk.addCourse(any()) } throws RuntimeException("unexpected error occured")
//
//        val savedCourseDTO = webTestClient
//            .post()
//            .uri("/v1/courses")
//            .bodyValue(courseDTO)
//            .exchange()
//            .expectStatus().is5xxServerError
//    }

    @Test
    fun retrieveCourses() {

        every { courseServiceMockk.retrieveCourses(any()) } returns courseEntityList().map{
            CourseDTO(it.id,it.name,it.category,1)}

        val uri = UriComponentsBuilder.fromUriString("/v1/courses").queryParam("course_name","SpringBoot").toUriString()

        val savedCourseDTO = webTestClient
            .get()
            .uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(2, savedCourseDTO!!.size)

    }

//    @Test
//    fun retrieveCourses_exception() {
//
//        every { courseServiceMockk.retrieveCourses() } throws RuntimeException("unexpected error occured")
//
//
//        val savedCourseDTO = webTestClient
//            .get()
//            .uri("/v1/courses")
//            .exchange()
//            .expectStatus().is5xxServerError
//
//
//
//    }

    @Test
    fun updateCourse() {

        val course = CourseDTO(1, "Build Restful APIs using SpringBoot and Kotlin"
            ,"dev",1)

        val updatedcourse = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin1"
            ,"dev",1)

        every { courseServiceMockk.updateCourse(any(),any()) } returns updatedcourse

        val savedCourseDTO = webTestClient
            .put()
            .uri("/v1/courses/{courseId}",course.id)
            .bodyValue(updatedcourse)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Build Restful APIs using SpringBoot and Kotlin1", savedCourseDTO!!.name)
    }



    @Test
    fun deleteCourse() {

        val course = CourseDTO(1, "Build Restful APIs using SpringBoot and Kotlin"
            ,"dev",1)

        justRun { courseServiceMockk.deleteCourse(any()) }

        val savedCourseDTO = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}",course.id)
            .exchange()
            .expectStatus().isNoContent
    }

}
