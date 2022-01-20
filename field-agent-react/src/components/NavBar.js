import { Link } from "react-router-dom";
import { useState, useEffect } from 'react';





function NavBar(props) {

 
  return (
    <nav>
    <ul>
      <li>
        <Link to="/">Home</Link>
      </li>
      <li>
        <Link to="/agents/add">Add an agent</Link>
      </li>


    </ul>
  </nav>
  );
}

export default NavBar;
