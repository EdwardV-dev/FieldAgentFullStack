package learn.field_agent.data;

import learn.field_agent.data.mappers.AgentMapper;
import learn.field_agent.data.mappers.AliasMapper;
import learn.field_agent.data.mappers.SecurityClearanceMapper;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SecurityClearance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AliasJdbcTemplateRepository implements AliasRepository{

    private final JdbcTemplate jdbcTemplate; //auto-injected, provided by the configuration file

    public AliasJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    
    @Override
    public List<Alias> findAliasesAttachedToAnAgent(int agentId) {
        List<Alias> allAliases = new ArrayList<>();
        //1. Fetch the specific agen and map themt
        final String sql = "select * from agent where agent_id = ?;";
        Agent result = jdbcTemplate.query(sql, new AgentMapper(), agentId)
                .stream()
                .findFirst().orElse(null);

        if(result != null){
           allAliases = addAliases(result); //updating allAliases
        }
        
        return allAliases;
    }

    @Override
    public Alias add(Alias alias) {
        return null;
    }

    @Override
    public boolean update(Alias alias) {
        return false;
    }

    @Override
    public boolean deleteById(int aliasId) {
        return false;
    }

    private List<Alias> addAliases(Agent agent){
        final String sql = "select alias_id, name alias_name, persona, agent_id from alias where agent_id = ?;";

        var aliases = jdbcTemplate.query(sql, new AliasMapper(), agent.getAgentId());
        agent.setAliases(aliases);
        
        return aliases;

    }
}
