package learn.field_agent.data;

import learn.field_agent.models.Alias;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasJdbcTemplateRepositoryTest {
    private final int NEXT_ID = 3;

    @Autowired
    AliasJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void findAliasesAttachedToAnAgent() {
        // can't predict order
        // if delete is first, we're down to 1 alias
        // if add is first, we may go as high as 4 aliases
        List<Alias> agentExists = repository.findAliasesAttachedToAnAgent(1);
        assertTrue(agentExists.size() >= 1 && agentExists.size() <= 4); //confirming that agentId 1 has aliases attached

        List<Alias> agentDoesNotExistOrHasNoAlias = repository.findAliasesAttachedToAnAgent(4);
        assertEquals(0, agentDoesNotExistOrHasNoAlias.size());

    }

    @Test
    void shouldFindAll(){
       List<Alias> aliases = repository.findAll();
       assertNotNull(aliases);

       assertTrue(aliases.size() >= 1 && aliases.size() <= 4);
    }

    @Test
    void shouldAdd() {
        // all fields
        Alias alias1 = makeAlias1();
        Alias actual = repository.add(alias1);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getAliasId());

        //Persona null
        Alias alias2 = makeAlias2();
        Alias actual2 = repository.add(alias2);
        assertNotNull(actual2);
        assertEquals(NEXT_ID + 1, actual2.getAliasId());
    }

    @Test
    void shouldUpdate() {
        Alias alias1 = makeAlias1();
        alias1.setAliasId(1); //updating alias 1
        assertTrue(repository.update(alias1));
        alias1.setAliasId(13); //should not update a non-existing alias
        assertFalse(repository.update(alias1));
    }

    //delete alias 2, as alias 1 is used for updates
    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(2));
        assertFalse(repository.deleteById(100));
    }

    private Alias makeAlias1() {
        Alias alias = new Alias();
        alias.setAgentId(1);
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
}