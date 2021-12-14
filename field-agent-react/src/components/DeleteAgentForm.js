import React, { useState, useEffect } from "react";
import { Link, useHistory, useParams } from "react-router-dom";


function DeleteAgentForm(props) {
  const [agent, setAgent] = useState(null); //currentAgent is used to pre-fill summary info
  const history = useHistory()
  const {id} = useParams()

  // NEW: Call the `useEffect` hook upon clicking the delete button in order to fetch the specific agent's information
  useEffect(
    () => {
      // Only do this if there is an `id`
      if (id) {
        fetch(`http://localhost:8080/api/agent/${id}`)
        .then(response => {
          if (response.status !== 200) {
            return Promise.reject("agent fetch failed")
          }
          return response.json();
        })
        .then(data => setAgent(data))
        .catch(error => console.log(error));
      }
    }, [])

  const handleSubmit = (event) => {
    event.preventDefault();
    if (id) {
      fetch(`http://localhost:8080/api/agent/${id}`, { method: "DELETE" })
      .then((response) => {
        if (response.status === 204) {
          // `filter` new state
          console.log("deletion is a success");
        } else if (response.status === 404) {
          return Promise.reject("Id not found");
        } else {
          return Promise.reject(
            `Delete failed with status: ${response.status}`
          );
        }
      })
      .catch(console.log);

      }

      history.push('/agents')
  };

  return (
    agent &&
    <>
    <h1> Are you sure you want to delete this agent?</h1>
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
      <Link to = "/agents">Cancel</Link>
    </form>
    </>
  )
}

export default DeleteAgentForm