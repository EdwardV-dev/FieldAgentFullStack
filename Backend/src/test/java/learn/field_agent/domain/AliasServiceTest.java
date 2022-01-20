package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.Alias;
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
class AliasServiceTest {

    @Autowired
    AliasService service;

    @MockBean
    AliasRepository repository;


    @Test
    void shouldNotAddWhenInvalid() {
        Alias alias1 = makeAlias1(); //not allowed to manually add alias ID
        Result<Alias> result = service.add(alias1);
        assertEquals(ResultType.INVALID, result.getType());

        alias1.setAliasId(0);
        alias1.setName(null); //alias name is required
        result = service.add(alias1);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenInvalidViaDuplicateNameAndMissingPersona() {
        Alias nullAliasPersonaAndNonNullName = makeAlias2(); //null persona
        List<Alias> populate = new ArrayList<>(Arrays.asList(nullAliasPersonaAndNonNullName));
        when(repository.findAll()).thenReturn(populate); //The only repo method required is findAll (used to detect duplicates)

        Alias nullAliasPersonaAndNonNullNameAddition = makeAlias2();
        Result<Alias> result = service.add(nullAliasPersonaAndNonNullNameAddition); //triggers findAll method in repository
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldAddWhenValid() {
        Alias expected = makeAliasAllFieldsNonNull(); //if alias id is not set, the default is zero (because integer type)
        Alias arg = makeAliasAllFieldsNonNull();
        arg.setAliasId(0);

        when(repository.add(arg)).thenReturn(expected);

        Result<Alias> result = service.add(arg);//should trigger the "when" clause above,
                                               // as a repo call is required if validations are successful
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotUpdateWhenInvalidViaDuplicateNameAndMissingPersona() {
        Alias nonNullAlias = makeAliasAllFieldsNonNull(); //null persona
        List<Alias> populate = new ArrayList<>(Arrays.asList(nonNullAlias));
        when(repository.findAll()).thenReturn(populate); //The only repo method required is findAll (used to detect duplicates)
                                                          //compare against this non-null alias

        Alias nullPersonaAndNonNullName = makeAlias2();
        Result<Alias> result = service.update(nullPersonaAndNonNullName); //triggers findAll method in repository. Comparing against
                                                                          //nonNullAlias (same name: 004)
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldUpdate() {
        Alias alias = makeAliasAllFieldsNonNull();
        alias.setAliasId(1);

        when(repository.update(alias)).thenReturn(true);

        Result<Alias> actual = service.update(alias); //if the "when" clause is triggered, then all validations successful
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    /* Delete Tests */
    @Test
    void shouldNotDeleteMissingAlias() {
        when(repository.deleteById(10)).thenReturn(false);
        assertFalse(service.deleteById(10)); //should trigger repository delete method
    }

    @Test
    void shouldDeleteExistingAlias() {
        when(repository.deleteById(2)).thenReturn(true);
        assertTrue(service.deleteById(2));
    }

    private Alias makeAlias1() { //Not allowed to manually add an alias id
        Alias alias = new Alias();
        alias.setAgentId(1);
        alias.setAliasId(1);
        alias.setPersona("Mr. Wacky");
        alias.setName("Arnold");
        return alias;
    }

    private Alias makeAlias2() {
        Alias alias = new Alias();
        alias.setAgentId(1);
        alias.setPersona(null);
        alias.setName("004");
        return alias;
    }

    private Alias makeAliasAllFieldsNonNull() {
        Alias alias = new Alias();
        alias.setAgentId(1);
        alias.setPersona("Hawkeye");
        alias.setName("004");
        return alias;
    }
}