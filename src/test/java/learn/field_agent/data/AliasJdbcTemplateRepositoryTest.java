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


        List<Alias> actual = repository.findAliasesAttachedToAnAgent(1);
        assertEquals(2, actual.size()); //confirming that agentId 1 has two aliases



    }

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
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
        alias.setPersona("Mr. Cornball");
        alias.setName("004");
        return alias;
    }
}