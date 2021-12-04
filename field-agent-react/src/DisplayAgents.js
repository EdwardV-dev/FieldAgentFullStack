import AddAgentForm from "./AddAgentForm";
import EditAgentForm from "./EditAgentForm";
import DeleteAgentForm from "./DeleteAgentForm";
import React, {useState, useEffect} from "react";
function DisplayAgents() {
 
   
  const [editing, setEditing] = useState(false); //Indicates whether edting a to do
  const [deleting, setDeleting] = useState(false);
  const [adding, setAdding] = useState(false);
  const [agents, setAgents] = useState([]);

  const initialFormState = { id: null, description: '' };
  const [currentAgent, setCurrentAgent] = useState(initialFormState)

 //Grab the initial state. That is, populate todos  

 useEffect(() => {

  // 2. fetch initial todos
  fetch("http://localhost:8080/api/agent")
    .then(response => {
      if (response.status !== 200) {
        return Promise.reject("todos fetch failed")
      }
      return response.json();
    })
    .then(json => setAgents(json))
    .catch(console.log);

}, []); // 3. only run once

return (
  <div className="agent-list">
    <div className="col">
      {editing ? (
        <>
          {/* Edit todo form is sent the current todo and the update todo function */}
          <h2>Edit agent</h2>
          <EditAgentForm
            // updateTodo={updateToDo}
            currentAgent={currentAgent}
            setEditing={setEditing}
          />
        </>
      ) : deleting ? (
        <>
          <h2>Delete agent</h2>
          <DeleteAgentForm  />
        </>
      ) : adding ? (
        <>
        <h2>Add agent</h2>
        <AddAgentForm  />
      </>
       
      ) : <ul>{agents.map((element) => (  //Curly brackets break into javascript

        <li key={element.agentId}>{element.agentId} {element.lastName} <button> Delete </button>
          <button> Edit </button>
        </li>
      ))}
      </ul>
}
</div>
</div>
   
 
 
 )
 
}


 export default DisplayAgents;