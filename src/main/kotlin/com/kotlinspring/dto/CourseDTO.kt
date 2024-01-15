package com.kotlinspring.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CourseDTO(
    val id:Int?,
    @get:NotBlank(message = "name should not be blank")
    val name:String,
    @get:NotBlank(message = "category should not be blank")
    val category:String,
    @get:NotNull(message = "category should not be null")
    val instructorId: Int? =null
)
