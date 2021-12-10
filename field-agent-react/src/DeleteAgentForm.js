import React, { useState, useEffect } from "react";


function DeleteAgentForm(props) {
  const [agent, setAgent] = useState(props.currentAgent); //currentAgent is used to pre-fill summary info

  const handleSubmit = (event) => {
    event.preventDefault();
    props.deleteAgent(agent.agentId); //sending the agent-to-be-deleted's id
  };

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
          required
          disabled
        />
      </div>

      <div>
        <label htmlFor="middleName">Agent middle name</label>
        <input
          id="middleName"
          type="text"
          value={agent.middleName}
          name="middleName"
          disabled
        />
      </div>

      <div>
        <label htmlFor="lastName">Agent last name</label>
        <input
          id="lastName"
          type="text"
          name="lastName"
          value={agent.lastName}
          required
          disabled
        />
      </div>

      <div>
        <label htmlFor="heightInInches">Agent height in inches</label>
        <input
          id="heightInInches"
          type="number"
          name="heightInInches"
          value={agent.heightInInches}
          required
          min="36"
          max="96"
          disabled
        />
      </div>

      <div>
        <label htmlFor="dob">Agent date of birth (must be 12 or older)</label>
        <input
          id="dob"
          type="date"
          name="dob"
          value={agent.dob}
          disabled />

      </div>

      <button type="submit" className="btn btn-primary">Delete Agent</button>
      <button className="btn btn-danger" onClick={() => props.setDeleting(false)}>Cancel</button>
    </form>
  )
}

export default DeleteAgentForm