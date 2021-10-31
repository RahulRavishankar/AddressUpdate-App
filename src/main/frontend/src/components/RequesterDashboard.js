import React, { Component } from "react";
import { GenericNavbar } from "./Navbar.js";
import AddressEntryForm from "./Requester/AddressEntryForm.js";
import StatusFeed from "./Requester/StatusFeed.js";
import Profile from "./Profile";

class RequesterDashboard extends Component {
  constructor(props) {
    super(props);
    this.state = {
      addressupdate: false,
    };
    this.saved = this.props.location.state;
  }

  enableAddressForm = () => {
    this.setState({ addressupdate: true });
  };

  enableStatus = () => {
    this.setState({ addressupdate: false });
  };

  render() {
    const heading = {
      fontWeight: "500",
      fontSize: "25px",
    };
    return (
      <div>
        <GenericNavbar
          logintype="Requester"
          addrupdtcallback={this.enableAddressForm}
          statuscallback={this.enableStatus}
        />

        <div style={{ display: "block" }}>
          <div
            style={{
              display: "inline-block",
              marginTop: "2%",
              verticalAlign: "top",
            }}
          >
            <Profile />
          </div>

          <div
            style={{
              display: "inline-block",
              verticalAlign: "top",
              marginTop: "2%",
              marginLeft: "10%",
            }}
          >
            {this.state.addressupdate ? (
              <div>
                <p style={heading}>Address Update Form</p>
                <AddressEntryForm
                  addresseditcallback={this.enableAddressEdit}
                  saved={this.saved}
                />
              </div>
            ) : (
              <div>
                <p style={heading}>Status Of Address Update</p>
                <StatusFeed saved={this.saved} />
              </div>
            )}
          </div>
        </div>
      </div>
    );
  }
}

export default RequesterDashboard;
