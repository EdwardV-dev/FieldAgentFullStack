package learn.field_agent.data;

import learn.field_agent.models.Agent;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static learn.field_agent.data.AgentJdbcTemplateRepositoryTest.NEXT_ID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceJdbcTemplateRepositoryTest {

private final int NEXT_ID = 3;

    @Autowired
    SecurityClearanceJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindByIdAndReturnMoreThanOneAgencyAgentWhoseClearanceIsSecret() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        SecurityClearance topSecret = new SecurityClearance(2, "Top Secret");

        SecurityClearance actual = repository.findById(1);
        assertEquals(secret, actual);
        assertTrue(actual.getAgencyAgent().size() > 0); //confirming that one or more agencies are attached

        actual = repository.findById(2);
        assertEquals(topSecret, actual);

        actual = repository.findById(3);
        assertEquals(null, actual);
    }

    @Test
    void shouldFindAll() {
        List<SecurityClearance> securityClearances = repository.findAll();
        assertNotNull(securityClearances);

        // can't predict order
        // if delete is first, we're down to 1
        // if add is first, we may go as high as 3
        assertTrue(securityClearances.size() >= 1 && securityClearances.size() <= 3);
    }

    @Test
    void shouldAdd() {
        // all fields
        SecurityClearance securityClearance = makeSecurityClearance();
        SecurityClearance actual = repository.add(securityClearance);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getSecurityClearanceId());

    }

    //update ID 1 (secret) but never touch ID 2, since that is what we want to delete and its clearance id is never referenced by agencyAgent table
    @Test
    void shouldUpdate() {
        SecurityClearance securityClearance = makeSecurityClearance();
        securityClearance.setSecurityClearanceId(1);
        assertTrue(repository.update(securityClearance));
        securityClearance.setSecurityClearanceId(13);
        assertFalse(repository.update(securityClearance));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(2));
        assertFalse(repository.deleteById(1));
    }

    private SecurityClearance makeSecurityClearance() {
      SecurityClearance securityClearance = new SecurityClearance();
      securityClearance.setName("Spooky");
        return securityClearance;
    }
}