package com.kotlinspring.dto

import jakarta.validation.constraints.NotBlank

data class InstructorDTO ( val id:Int?,
                      @get:NotBlank(message = "instructor-name should not be blank")
                      var name:String)
