package learn.field_agent.data;

import learn.field_agent.data.mappers.AgencyAgentMapper;
import learn.field_agent.data.mappers.AgencyMapper;
import learn.field_agent.data.mappers.SecurityClearanceMapper;
import learn.field_agent.models.Agency;
import learn.field_agent.models.AgencyAgent;
import learn.field_agent.models.SecurityClearance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class SecurityClearanceJdbcTemplateRepository implements SecurityClearanceRepository {

    private final JdbcTemplate jdbcTemplate; //auto-injected, provided by the configuration file

    public SecurityClearanceJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SecurityClearance findById(int securityClearanceId) {

        final String sql = "select security_clearance_id, name security_clearance_name "
                + "from security_clearance "
                + "where security_clearance_id = ?;";

        SecurityClearance result = jdbcTemplate.query(sql, new SecurityClearanceMapper(), securityClearanceId)
                .stream()
                .findFirst().orElse(null);

        if(result != null){
            addAgencyAgent(result);
        }

        return result;
    }

    @Override
    public List<SecurityClearance> findAll() {
        final String sql = "select security_clearance_id, name security_clearance_name from security_clearance limit 1000;";
        return jdbcTemplate.query(sql, new SecurityClearanceMapper());
    }

    @Override
    public SecurityClearance add(SecurityClearance securityClearance) {

        final String sql = "insert into security_clearance (name) values (?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, securityClearance.getName());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        securityClearance.setSecurityClearanceId(keyHolder.getKey().intValue());
        return securityClearance;
    }

    //For testing out http requests in the test table
    public void setKnownGoodState(){
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Override
    public boolean update(SecurityClearance securityClearance) {
        final String sql = "update security_clearance set "
                + "name = ? "
                + "where security_clearance_id = ?;";

        return jdbcTemplate.update(sql,
                securityClearance.getName(),
                securityClearance.getSecurityClearanceId()) > 0;
    }

    //Service layer will prevent deletion if the security clearance id is present in the agencyAgent table
    @Override
    @Transactional
    public boolean deleteById(int securityClearanceId) {
        //Makes sure security clearance id
        return jdbcTemplate.update("delete from security_clearance where security_clearance_id = ? and " +
                "security_clearance_id NOT IN " +
                "(Select security_clearance_id from agency_agent " +
                "where security_clearance_id = ? " +
                "group by security_clearance_id " +
                "Having count(security_clearance_id) > 0);", securityClearanceId, securityClearanceId) > 0;
//returns the security clearance id where the said security clearance id pops up 1 or more times in agency agent. Else, returns null
        //
    }


    private void addAgencyAgent(SecurityClearance securityClearance) {

        //selecting a specific agency that aligns with the security clearance id
        final String sql = "select aa.agency_id, aa.agent_id, aa.identifier, aa.activation_date, aa.is_active, "
                + "sc.security_clearance_id, sc.name security_clearance_name, "
                + "a.first_name, a.middle_name, a.last_name, a.dob, a.height_in_inches "
                + "from agency_agent aa "
                + "inner join agent a on aa.agent_id = a.agent_id "
                + "inner join security_clearance sc on aa.security_clearance_id = sc.security_clearance_id "
                + "where aa.security_clearance_id = ?";

        var agencyAgents = jdbcTemplate.query(sql, new AgencyAgentMapper(),
                securityClearance.getSecurityClearanceId());

        securityClearance.setAgencyAgent(agencyAgents);
    }
}
