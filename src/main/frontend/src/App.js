import "./App.css";
import Login from "./components/Login.js";
import IntroducerDashboard from "./components/IntroducerDashboard";
import RequesterDashboard from "./components/RequesterDashboard";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
} from "react-router-dom";

function App() {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route exact path="/" component={Login} />
          <Route
            exact
            path="/Introducer-dashboard"
            component={IntroducerDashboard}
          />
          <Route
            exact
            path="/requester-dashboard"
            component={RequesterDashboard}
          />
        </Switch>
      </Router>
    </div>
  );
}

export default App;
