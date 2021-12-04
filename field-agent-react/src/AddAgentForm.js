import React, { useState, useEffect } from "react";

const INITIAL_FORM_STATE = {
  agentId: null,
  firstName: "",
  middleName: "",
  lastName: "",
  dob: null,
  heightInInches: null

}

//max date is the following. I.e. user must enter earlier date to represent an older agent. (agent must be older than 12)

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

function AddAgentForm({ onSubmit, setAdding }) {
  // Track an object (array in this case), not properties
  const [agent, setAgent] = useState(INITIAL_FORM_STATE);

  const handleInputChange = function (evt) {
    //Create a shallow copy. Don't modify existing state
    let nextAgent = { ...agent };

    nextAgent[evt.target.name] = evt.target.value;

    setAgent(nextAgent);

    console.log(nextAgent);

  }


  function handleSubmit(evt) {
    evt.preventDefault();
    
    console.log("submitted!", agent);

    onSubmit(agent); //nextAgent became just agent

    setAgent(INITIAL_FORM_STATE); //clears the form
  }

  //agent must be truthy for rendering to occur
  return (
     agent &&
    <form onSubmit={handleSubmit}>
      <div>
      <label htmlFor="firstName">Agent first name</label>
      <input
       id="firstName"
        type="text"
        name="firstName"
        onChange={handleInputChange}
        required
      />
      </div>

      <div>
      <label htmlFor="middleName">Agent middle name</label>
      <input
       id="middleName"
        type="text"
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
      max = {maxDateToEnter}
      onChange={handleInputChange}/>
      </div>

      <button type="submit" className="btn btn-primary">Add todo</button>
      <button className="btn btn-danger" onClick={() => setAdding(false)}>Cancel</button> 
    </form>
  )
}

export default AddAgentForm