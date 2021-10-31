import React, { Component } from "react";
import Profile from "./Profile";
import { GenericNavbar } from "./Navbar";
import Listofrequests from "./Listofrequests";

export class IntroducerDashboard extends Component {
  constructor(props) {
    super(props);
    this.saved = this.props.location.state;
  }

  render() {
    const listofrequests = {
      marginLeft: "10%",
      marginTop: "2%",
      display: "inline-block",
      verticalAlign: "top",
      height: "100%",
    };

    const profile = {
      marginTop: "2%",
      display: "inline-block",
      verticalAlign: "top",
      height: "100%",
    };

    const heading = {
      fontWeight: "500",
      fontSize: "25px",
    };

    return (
      <div>
        <GenericNavbar logintype="Introducer" />
        <div style={profile}>
          <Profile />
        </div>
        <div style={listofrequests}>
          <p style={heading}>Pending Requests for Address Update</p>
          <Listofrequests saved={this.saved} />
        </div>
      </div>
    );
  }
}
export default IntroducerDashboard;
