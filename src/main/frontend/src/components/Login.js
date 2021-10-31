import React, { Component } from "react";
import { LoginNavbar } from "./Navbar.js";
import Form from "./Form.js";

export class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      logintype: "Requester",
    };
  }

  handleCallback = (childData) => {
    this.setState({ logintype: childData });
  };

  render() {
    return (
      <div>
        <LoginNavbar logintype={this.handleCallback} />
        <div style={{ "margin-top": "30px", "font-size": "30px" }}>
          Login as
        </div>
        <div style={{ marginBottom: "30px", fontSize: "40px" }}>
          {this.state.logintype}
        </div>
        <Form logintype={this.state.logintype} />
      </div>
    );
  }
}

export default Login;
