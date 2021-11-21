package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SecurityClearance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AliasService {
    private final AliasRepository repository;

    public AliasService(AliasRepository repository) {
        this.repository = repository;
    }

    public List<Alias> findAliasesAttachedToAnAgent(int agentId){
        return repository.findAliasesAttachedToAnAgent(agentId);
    }

    public void setKnownGoodState(){
        repository.setKnownGoodState();
    }

    public Result<Alias> add(Alias alias) {
        Result<Alias> result = validate(alias);
        if (!result.isSuccess()) {
            return result;
        }

        //Preventing the user from manually adding the securityClearanceId somehow
        if (alias.getAliasId() != 0) {
            result.addMessage("aliasId cannot be set for an `add` operation", ResultType.INVALID);
            return result;
        }

        alias = repository.add(alias);
        result.setPayload(alias); //indicating that an alias was added. Can be retrieved later on for ResponseEntity
        return result;
    }

    public Result<Alias> update(Alias alias) {
        Result<Alias> result = validate(alias);
        if (!result.isSuccess()) {
            return result;
        }

        if (alias.getAliasId() <= 0) {
            result.addMessage("alias Id must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(alias)) {
            String msg = String.format("alias ID: %s, not found", alias.getAliasId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int aliasId) {

        return repository.deleteById(aliasId); //Error 400 not needed for deletion
    }

    private Result<Alias> validate(Alias alias) {
        Result<Alias> result = new Result<>();
        if (alias == null) {
            result.addMessage("Alias cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(alias.getName())) {
            result.addMessage("name is required", ResultType.INVALID);
        }

        List<Alias> aliases = repository.findAll();
        for (Alias a : aliases) {
            if (a.getName().equalsIgnoreCase(alias.getName())) { //If names are duplicates and if persona is null or blank...
                if (Validations.isNullOrBlank(alias.getPersona())) {
                    result.addMessage("Duplicate names present. Persona name is required", ResultType.INVALID);
                    break;
                }
            }
        }
        return result;
    }
}

