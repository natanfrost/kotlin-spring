import com.coursecatalog.CourseCatalogServiceApplication
import com.coursecatalog.dto.CourseDTO
import com.coursecatalog.entity.Course
import com.coursecatalog.entity.Instructor
import com.coursecatalog.repository.CourseRepository
import com.coursecatalog.repository.InstructorRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest(
    classes = [CourseCatalogServiceApplication::class], // Add this
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@AutoConfigureWebClient
@Testcontainers
class CourseControllerIntgTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var couseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    companion object {
        @Container
        val postgresDB = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:10.5")).apply {
            withDatabaseName("testdb")
            withUsername("postgres")
            withPassword("secret")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresDB::getJdbcUrl)
            registry.add("spring.datasource.username", postgresDB::getUsername)
            registry.add("spring.datasource.password", postgresDB::getPassword)
        }
    }

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

        webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", course.id)
            .exchange()
            .expectStatus().isNoContent

        val deletedCourse = couseRepository.findByIdOrNull(course.id!!)

        Assertions.assertEquals(deletedCourse, null)
    }
}