import React, { useState, useEffect } from "react";
import { Link, useHistory, useParams } from "react-router-dom";

function EditAgentForm(props) {
  const [agent, setAgent] = useState(null); //currentAgent is used to pre-fill
  const history = useHistory()
  const {id} = useParams()

    // NEW: Call the `useEffect` hook upon clicking the edit button in order to fetch the specific to do's information
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

  const handleInputChange = (event) => {
    const { name, value } = event.target; //object destructuring
    setAgent({ ...agent, [name]: value }); //Same property name that "...agent" contains is replaced by the new value. Id is not one of those values
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    const updatedAgent = {...agent}

    if (id) {
      const init = {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Accept": "application/json"
        },
        body: JSON.stringify(updatedAgent)
      };

      fetch(`http://localhost:8080/api/agent/${id}`, init)
        .then(response => {
          if (response.status !== 204) {
            return Promise.reject("response is not 204 No Content");
          }
          return null;
        })
        .then(data => {
          console.log("successful put request");
        })
        .catch(() => {
          console.log("error with the put request");
        });

      }

      history.push('/agents')
  };

  let today = new Date();
  let dd = today.getDate();
  let mm = today.getMonth() + 1; //January is 0.
  let twelveYearsAgo = today.getFullYear() - 12;

  if (dd < 10) {
    dd = "0" + dd;
  }

  if (mm < 10) {
    mm = "0" + mm;
  }

  let maxDateToEnter = twelveYearsAgo + "-" + mm + "-" + dd;

  //agent has to be truthy for rendering to occur
  return (
  agent &&  (
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
            min="36"
            max="96"
          />
        </div>

        <div>
          <label htmlFor="dob">Agent date of birth (must be 12 or older)</label>
          <input
            id="dob"
            type="date"
            name="dob"
            value={agent.dob}
            max={maxDateToEnter}
            onChange={handleInputChange}
          />
        </div>

        <button type="submit" className="btn btn-primary">
          Edit Agent
        </button>
        <Link to = "/agents">Cancel</Link>
      </form>
    )
  );
}

export default EditAgentForm;
