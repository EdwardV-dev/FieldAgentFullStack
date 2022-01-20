import { Link } from "react-router-dom";

function Home () {

  return (
    <>
   <h2>Welcome to the Agents Application</h2>
   <Link to = "/agents">Click here to view a list of agents</Link>
   </>
  )
}

export default Home;