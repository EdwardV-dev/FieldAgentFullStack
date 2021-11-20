package learn.field_agent.domain;

import learn.field_agent.data.AgentRepository;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Location;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceServiceTest {

    @Autowired
    SecurityClearanceService service;

    @MockBean
    SecurityClearanceRepository repository;

    @Test
    void shouldNotAddWhenInvalid() {
        SecurityClearance securityClearance = makeSecurityClearance();
        Result<SecurityClearance> result = service.add(securityClearance);
        assertEquals(ResultType.INVALID, result.getType());

        securityClearance.setSecurityClearanceId(2);
        securityClearance.setName(null);
        result = service.add(securityClearance);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenInvalidViaDuplicate() {
        SecurityClearance securityClearance = makeSecurityClearance();
        SecurityClearance securityClearance2 = makeSecurityClearance();
        Result<SecurityClearance> result = service.add(securityClearance);
        assertEquals(ResultType.INVALID, result.getType());

    }

    @Test
    void shouldAddWhenValid() {
        SecurityClearance expected = makeSecurityClearance();
        SecurityClearance arg = makeSecurityClearance();
        arg.setName("Spooky");
        arg.setSecurityClearanceId(0);

        when(repository.add(arg)).thenReturn(expected);

        Result<SecurityClearance> result = service.add(arg);//should trigger the "when" clause above, as a repo call is required
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotUpdateWhenInvalidViaDuplicate() {
        SecurityClearance securityClearance1 = makeSecurityClearance();
        List<SecurityClearance> populate = new ArrayList<>(Arrays.asList(securityClearance1));
        when(repository.findAll()).thenReturn(populate); //The only repo method required is findAll (used to detect duplicates)


        SecurityClearance securityClearance2 = makeSecurityClearance();
        Result<SecurityClearance> result = service.update(securityClearance2);
        assertEquals(ResultType.INVALID, result.getType());

    }

    @Test
    void shouldUpdate() {
        SecurityClearance securityClearance1 = makeSecurityClearance();
        securityClearance1.setSecurityClearanceId(1);

        when(repository.update(securityClearance1)).thenReturn(true);

        Result<SecurityClearance> actual = service.update(securityClearance1);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }


    /* Delete Tests */
    @Test
    void shouldNotDeleteMissingClearance() {
        when(repository.deleteById(10)).thenReturn(false);
        assertFalse(service.deleteById(10));
    }

    @Test
    void shouldDeleteTopSecretClearance() {
        when(repository.deleteById(2)).thenReturn(true);
        assertTrue(service.deleteById(2));
    }


    private SecurityClearance makeSecurityClearance() {
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setSecurityClearanceId(1);
        securityClearance.setName("Spooky");
        return securityClearance;
    }
}