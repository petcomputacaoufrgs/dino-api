package br.ufrgs.inf.pet.dinoapi;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DinoapiApplicationTests.class)
@TestPropertySource(locations="classpath:test.properties")
public class DinoapiApplicationTests {

    @Test
    public void contextLoads() {
    }
}
