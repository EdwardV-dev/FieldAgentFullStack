import AddAgentForm from "./AddAgentForm";
import EditAgentForm from "./EditAgentForm";
import DeleteAgentForm from "./DeleteAgentForm";
import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
function DisplayAgents() {
  
  const [deleting, setDeleting] = useState(false);
 
  const [agents, setAgents] = useState([]);

  const INITIAL_FORM_STATE = {
    agentId: null,
    firstName: "",
    middleName: "",
    lastName: "",
    dob: null,
    heightInInches: null,
  };

  const [currentAgent, setCurrentAgent] = useState(INITIAL_FORM_STATE);

  //Grab the initial state. That is, populate agents

  useEffect(() => {
    // 2. fetch initial agents
    fetch("http://localhost:8080/api/agent")
      .then((response) => {
        if (response.status !== 200) {
          return Promise.reject("agents fetch failed");
        }
        return response.json();
      })
      .then((json) => setAgents(json))
      .catch(console.log);
  }); // 3. Executed upon every render. Whenever useHistory() is called, a re-render occurs in all routes (history is global)

  //State of DisplayAgent component changes and forces a re-draw in react. Return statement is triggered once more



  const deleteRow = (agent) => {
    setDeleting(true); // delete mode active
    setCurrentAgent({ ...agent });
  };

  


  const deleteAgent = (agentId) => {
    setDeleting(false);
    fetch(`http://localhost:8080/api/agent/${agentId}`, { method: "DELETE" })
      .then((response) => {
        if (response.status === 204) {
          // `filter` new state
          console.log("deletion is a success");
          setAgents(agents.filter((a) => a.agentId !== agentId)); //produces a new array without the agent that we sent to the backend to remove
        } else if (response.status === 404) {
          return Promise.reject("Id not found");
        } else {
          return Promise.reject(
            `Delete failed with status: ${response.status}`
          );
        }
      })
      .catch(console.log);
  };

  return (
    <div className="agent-list">
      <div className="col">
        <h2>List of agents</h2>

        <ul>
          {agents.map(
            (
              element //Curly brackets break into javascript
            ) => (
              
              <li key={element.agentId}>
                ID: {element.agentId}, DOB: {element.dob}, Last Name:{" "}
                {element.lastName}
                &nbsp; <Link to ={`/agents/edit/${element.agentId}`} >Edit</Link> &nbsp; 
                <Link to ={`/agents/delete/${element.agentId}`} >Delete</Link>
                
                <br></br> <br></br>
              </li>
            )
          )}
        </ul>
      </div>
    </div>
  );
}

export default DisplayAgents;
