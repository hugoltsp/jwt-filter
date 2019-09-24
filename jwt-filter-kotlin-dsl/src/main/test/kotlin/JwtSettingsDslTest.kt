import com.hugoltsp.spring.boot.starter.jwt.filter.JwtAuthenticationSettings
import com.hugoltsp.spring.boot.starter.jwt.filter.dsl.settings
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class JwtSettingsDslTest {

    @Test
    fun `JwtSettingsDsl should create an instance of JwtAuthenticationSettings`() {

        val settings = settings {
            secretKey = "mySecretKey"
            publicResources {
                publicResource {
                    method = "GET"
                    urls = "/public,/test"
                }
                publicResource {
                    method = "POST"
                    urls = "/user"
                }
            }
        }

        assertThat(settings).isNotNull;
        assertThat(settings).isInstanceOf(JwtAuthenticationSettings::class.java)
        assertThat(settings.secretKey).isEqualTo("mySecretKey")
        assertThat(settings.publicResources.size).isEqualTo(2)
    }

}