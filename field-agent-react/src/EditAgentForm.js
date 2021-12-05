import React, { useState, useEffect } from "react";


function EditAgentForm(props) {
  const [agent, setAgent] = useState(props.currentAgent); //currentAgent is used to pre-fill

  const handleInputChange = (event) => {
    const { name, value } = event.target; //object destructuring
    setAgent({ ...agent, [name]: value }); //Same property name that "...agent" contains is replaced by the new value. Id is not one of those values
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    props.updateAgent(agent.agentId, agent); //sending the newly-set/updated agent to the parent component
  };

let today = new Date();
let dd = today.getDate();
let mm = today.getMonth() + 1; //January is 0.
let twelveYearsAgo = today.getFullYear() - 12;

if (dd < 10) {
   dd = '0' + dd;
}

if (mm < 10) {
   mm = '0' + mm;
} 
    
let maxDateToEnter = twelveYearsAgo + '-' + mm + '-' + dd;

  //agent has to be truthy for rendering to occur
   return (
     agent &&
     <form onSubmit={handleSubmit}>
     <div>
     <label htmlFor="firstName">Agent first name</label>
     <input
      id="firstName"
       type="text"
       name="firstName"
       value={agent.firstName}
       onChange={handleInputChange}
       required
     />
     </div>

     <div>
     <label htmlFor="middleName">Agent middle name</label>
     <input
      id="middleName"
       type="text"
       value={agent.middleName}
       name="middleName"
       onChange={handleInputChange} 
     />
     </div>

     <div>
     <label htmlFor="lastName">Agent last name</label>
      <input
      id="lastName"
       type="text"
       name="lastName"
       value={agent.lastName}
       onChange={handleInputChange} 
       required
     />
     </div>

     <div>
     <label htmlFor="heightInInches">Agent height in inches</label>
       <input
       id="heightInInches"
       type="number"
       name="heightInInches"
       value={agent.heightInInches}
       onChange={handleInputChange} 
       required
       min = "36"
       max = "96"
     />
     </div>
   
     <div>
     <label htmlFor="dob">Agent date of birth (must be 12 or older)</label>
     <input 
     id="dob"
     type="date" 
     name="dob" 
     value={agent.dob}
     max = {maxDateToEnter}
     onChange={handleInputChange}/>
     </div>

     <button type="submit" className="btn btn-primary">Edit Agent</button>
     <button className="btn btn-danger" onClick={() => props.setEditing(false)}>Cancel</button> 
   </form>
  );
}

export default EditAgentForm