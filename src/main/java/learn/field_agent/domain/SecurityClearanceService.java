package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.Agent;
import learn.field_agent.models.SecurityClearance;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class SecurityClearanceService {
    private final SecurityClearanceRepository repository;

    public SecurityClearanceService(SecurityClearanceRepository repository) {
        this.repository = repository;
    }

    public List<SecurityClearance> findAll() {
        return repository.findAll();
    }

    public SecurityClearance findById(int securityClearanceId) {
        return repository.findById(securityClearanceId);
    }

    public void setKnownGoodState(){
        repository.setKnownGoodState();
    }

    public Result<SecurityClearance> add(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }

        //Preventing the user from manually adding the securityClearanceId somehow
        if (securityClearance.getSecurityClearanceId() != 0) {
            result.addMessage("securityClearanceId cannot be set for an `add` operation", ResultType.INVALID);
            return result;
        }

        securityClearance = repository.add(securityClearance);
        result.setPayload(securityClearance); //indicating that a security clearance was added. Can be retrieved later on for ResponseEntity
        return result;
    }

    public Result<SecurityClearance> update(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }

        if (securityClearance.getSecurityClearanceId() <= 0) {
            result.addMessage("security clearance Id must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(securityClearance)) {
            String msg = String.format("security clearance ID: %s, not found", securityClearance.getSecurityClearanceId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int agentId) {

        return repository.deleteById(agentId); //if repository returns false, creates an error 400 in the controller
    }

    private Result<SecurityClearance> validate(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = new Result<>();
        if (securityClearance == null) {
            result.addMessage("Security clearance cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(securityClearance.getName())) {
            result.addMessage("name is required", ResultType.INVALID);
        }


        List<SecurityClearance> clearances = repository.findAll();
        for (SecurityClearance s : clearances) {
            if (s.getName().equalsIgnoreCase(securityClearance.getName())) {
                result.addMessage("duplicates are not allowed", ResultType.INVALID);
                break;
            }
        }
        return result;
    }
}
