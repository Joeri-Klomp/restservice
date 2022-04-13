package be.vdab.restservice.services;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@DataJpaTest(showSql = false)
@Import(FiliaalService.class)
@ComponentScan(value = "be.vdab.restservice.repositories", resourcePattern = "FiliaalRepository.class")
@Sql
class FiliaalServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {


}