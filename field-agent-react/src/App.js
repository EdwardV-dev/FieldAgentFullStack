import DisplayAgents from "./components/DisplayAgents";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import NavBar from "./components/NavBar";
import AddAgentForm from "./components/AddAgentForm";
import Home from "./components/Home";
import EditAgentForm from "./components/EditAgentForm";
import DeleteAgentForm from "./components/DeleteAgentForm";
import NotFound from "./components/NotFound";

function App() {
   
  
  return (
    <Router>
    <NavBar  />
  
    <Switch>
    <Route exact path="/">
        <Home />
    </Route>

    <Route exact path="/agents">
      <DisplayAgents />
    </Route>

    <Route path="/agents/add">
      <AddAgentForm />
    </Route>

    <Route path="/agents/edit/:id">
      <EditAgentForm />
    </Route>

    <Route path="/agents/delete/:id">
      <DeleteAgentForm />
    </Route>

    <Route>
      <NotFound />
    </Route>

    </Switch>

   </Router>
   );
 }
 
 export default App;