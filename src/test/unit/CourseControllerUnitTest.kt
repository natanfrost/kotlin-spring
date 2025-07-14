import com.course_catalog.course_catalog_service.CourseCatalogServiceApplication
import com.course_catalog.course_catalog_service.controller.CourseController
import com.course_catalog.course_catalog_service.dto.CourseDTO
import com.course_catalog.course_catalog_service.entity.Course
import com.course_catalog.course_catalog_service.service.CourseService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import jdk.jfr.Category
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [CourseController::class])
@ContextConfiguration(classes = [CourseCatalogServiceApplication::class])
@AutoConfigureWebClient
class CourseControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMock: CourseService

    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(null, "Test", "Cat Test")

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)
    }

    @Test
    fun getAllCourses() {
        every { courseServiceMock.getAll() }.returnsMany(
            listOf(courseDTO(id = 1),
                courseDTO(id = 2, name = "Test"))
        )
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
    fun updateCourse() {
        val course = Course(1, name = "Test 1", category = "Cat Test")

        every { courseServiceMock.updateCourse(any(), any()) } returns courseDTO(id=100)

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", 100)
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
        every { courseServiceMock.deleteCourse(100) } just runs
        val updatedCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", 100)
            .exchange()
            .expectStatus().isNoContent
    }

    fun courseDTO(
        id: Int? = null,
        name: String = "Test",
        category: String = "Cat Test"
    ) = CourseDTO(id, name, category)
}