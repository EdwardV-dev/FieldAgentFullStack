package learn.field_agent.data;

import learn.field_agent.data.mappers.AgentMapper;
import learn.field_agent.data.mappers.AliasMapper;
import learn.field_agent.data.mappers.SecurityClearanceMapper;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SecurityClearance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
        //1. Fetch the specific agent and map them
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
    public List<Alias> findAll() {
        final String sql = "select alias_id, name alias_name, persona, agent_id from alias limit 1000;";
        return jdbcTemplate.query(sql, new AliasMapper());
    }

    @Override
    public Alias add(Alias alias) {
        final String sql = "insert into alias (name, persona, agent_id) values (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, alias.getName());
            ps.setString(2, alias.getPersona());
            ps.setInt(3, alias.getAgentId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        alias.setAliasId(keyHolder.getKey().intValue()); //id is set here and not through JSON deserializer (request does not have id in the body)
        return alias;
    }

    @Override
    public boolean update(Alias alias) {
        final String sql = "update alias set "
                + "name = ?, "
                + "persona = ?, "
                + "agent_id = ? "
                + "where alias_id = ?;";

        return jdbcTemplate.update(sql,
                alias.getName(),
                alias.getPersona(),
                alias.getAgentId(),
                alias.getAliasId()) > 0;
    }

    public void setKnownGoodState(){
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Override
    @Transactional
    public boolean deleteById(int aliasId) {
        return jdbcTemplate.update("delete from alias where alias_id = ?", aliasId) > 0;
    }

    private List<Alias> addAliases(Agent agent){
        final String sql = "select alias_id, name alias_name, persona, agent_id from alias where agent_id = ?;";

        var aliases = jdbcTemplate.query(sql, new AliasMapper(), agent.getAgentId());
        agent.setAliases(aliases);
        
        return aliases;

    }
}
