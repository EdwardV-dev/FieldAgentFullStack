package learn.field_agent.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

//See LMS testing the repository layer
@Component
public class KnownGoodState {

    @Autowired
    JdbcTemplate jdbcTemplate;

    //static maintains the status of hasRun even after multiple instantiations
    static boolean hasRun = false;

    void set() {
        if (!hasRun) {
            hasRun = true;
            jdbcTemplate.update("call set_known_good_state();");
        }
    }
}
