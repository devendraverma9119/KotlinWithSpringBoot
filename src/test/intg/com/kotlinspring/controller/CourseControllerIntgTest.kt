package com.kotlinspring.controller


import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.repository.CourseRepository
import com.kotlinspring.repository.InstructorRepository
import com.kotlinspring.util.PostgreSQLContainerInitializer
import com.kotlinspring.util.courseEntityList
import com.kotlinspring.util.instructorEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
//@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseControllerIntgTest : PostgreSQLContainerInitializer() {


    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

//    companion object {
//
//        @Container
//        val postgresDB = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13-alpine")).apply {
//            withDatabaseName("testdb")
//            withUsername("postgres")
//            withPassword("secret")
//        }
//
//        @JvmStatic
//        @DynamicPropertySource
//        fun properties(registry: DynamicPropertyRegistry) {
//            registry.add("spring.datasource.url", postgresDB::getJdbcUrl)
//            registry.add("spring.datasource.username", postgresDB::getUsername)
//            registry.add("spring.datasource.password", postgresDB::getPassword)
//        }
//    }


    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()
        var instructor = instructorEntity()
        instructorRepository.save(instructor)
        var courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    fun addCourse() {

        var instructor = instructorRepository.findAll().first()

        val courseDTO = CourseDTO(
            null, "Build Restful APIs using SpringBoot and Kotlin", "dev", instructor.id
        )

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

    @Test
    fun retrieveCourses() {

        val savedCourseDTO = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(3, savedCourseDTO!!.size)
    }

    @Test
    fun retrieveCoursesByName() {

        val uri =
            UriComponentsBuilder.fromUriString("/v1/courses").queryParam("course_name", "SpringBoot").toUriString()

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
//    fun updateCourse() {
//
//        var instructor = instructorRepository.findAll().first()
//
//        val course = Course(null, "Build Restful APIs using SpringBoot and Kotlin"
//            ,"dev",instructor)
//
//        val updatedcourse = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin1"
//            ,"dev",course.instructor!!.id)
//
//        val savedCourseDTO = webTestClient
//            .put()
//            .uri("/v1/courses/{courseId}",course.id)
//            .bodyValue(updatedcourse)
//            .exchange()
//            .expectStatus().isOk
//            .expectBody(CourseDTO::class.java)
//            .returnResult()
//            .responseBody
//
//        Assertions.assertEquals("Build Restful APIs using SpringBoot and Kotlin1", savedCourseDTO!!.name)
//    }

    @Test
    fun deleteCourse() {

        var instructor = instructorRepository.findAll().first()

        val course = Course(
            1, "Build Restful APIs using SpringBoot and Kotlin", "dev", instructor
        )

        val savedCourseDTO = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", course.id)
            .exchange()
            .expectStatus().isNoContent
    }
}
