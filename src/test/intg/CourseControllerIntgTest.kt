import com.course_catalog.course_catalog_service.CourseCatalogServiceApplication
import com.course_catalog.course_catalog_service.dto.CourseDTO
import com.course_catalog.course_catalog_service.entity.Course
import com.course_catalog.course_catalog_service.entity.Instructor
import com.course_catalog.course_catalog_service.repository.CourseRepository
import com.course_catalog.course_catalog_service.repository.InstructorRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

@SpringBootTest(
    classes = [CourseCatalogServiceApplication::class], // Add this
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@AutoConfigureWebClient
class CourseControllerIntgTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var couseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    fun setUp() {
        couseRepository.deleteAll();
        instructorRepository.deleteAll();
        val instructor = Instructor(id = null, name = "Test Inst")
        instructorRepository.save(instructor)
        val courses = listOf(
            Course(null, name = "Test 1", category = "Cat 1", instructor = instructor),
            Course(null, name = "Test 2", category = "Cat 2", instructor = instructor)
        )
        couseRepository.saveAll(courses)
    }

    @Test
    fun addCourse() {
        val instructor = instructorRepository.findAll().first()
        val courseDTO = CourseDTO(id = null, name = "Test", category = "Cat Test", instructorId = instructor.id)
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
    fun getAllCourses() {
        val savedCourseDTO = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(2, savedCourseDTO!!.size)
    }

    @Test
    fun getAllByName() {
        val uri = UriComponentsBuilder.fromUriString("/v1/courses/name")
            .queryParam("course_name", "Test")
            .toUriString()
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

    @Test
    fun updateCourse() {
        val course = Course(1, name = "Test 1", category = "Cat Test")

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", course.id)
            .bodyValue(course)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Cat Test", course.category)
    }

    @Test
    fun deleteCourse() {
        val instructor = instructorRepository.findAll().first()
        val course = Course(1, name = "Test 1", category = "Cat Test", instructor = instructor)

        val updatedCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", course.id)
            .exchange()
            .expectStatus().isNoContent
    }
}