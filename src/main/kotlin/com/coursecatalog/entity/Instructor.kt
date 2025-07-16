package com.coursecatalog.entity

import jakarta.persistence.*

@Entity
@Table(name = "Instructors")
data class Instructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    val name: String,
    @OneToMany(
        mappedBy = "instructor",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val courses: List<Course> = mutableListOf()
)