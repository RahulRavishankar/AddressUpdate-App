import React, { Component } from "react";
import axios from "axios";
import tick from "../tick.png";

class AddressVerification extends Component {
  constructor(props) {
    super(props);
    this.saved = this.props.saved;
    this.state = {
      done: false,
    };
  }

  handleGeoCode = (src_, dst_) => {
    // ...
    const options = {
      url: "http://localhost:8080/requester/verifyAddress/" + this.saved.uid,
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      data: {
        src: src_,
        dst: dst_,
      },
    };

    axios(options).then(
      (response) => {
        console.log(response.data);
        if (response.data == "Y") {
          this.setState({ done: true });
        }
      },
      (error) => {
        console.log(error);
      }
    );
  };

  render() {
    const box = {
      width: "600px",
      padding: "20px",
      marginTop: "20px",
    };

    return (
      <div className="card" style={box}>
        <div style={{ fontSize: 20, marginBottom: 10 }}>
          {this.props.index}. Address
        </div>
        <div className="text-secondary" style={{ marginBottom: 10 }}>
          {this.props.address}
        </div>
        <div style={{ display: "block" }}>
          <div
            className="btn btn-outline-secondary"
            style={{ width: "200", display: "inline-block" }}
            onClick={() => {
              this.props.showeditcallback();
            }}
          >
            Edit
          </div>
          <div
            className="btn btn-outline-secondary"
            style={{ width: "200", display: "inline-block", marginLeft: 20 }}
            onClick={() => {
              this.handleGeoCode(this.props.address, this.props.orgAddress);
            }}
          >
            Geographic verification
          </div>
          {this.state.done ? (
            <div style={{ display: "inline-block" }}>
              <img style={{ width: 50, height: 50 }} src={tick} />
            </div>
          ) : null}
        </div>
      </div>
    );
  }
}

export default AddressVerification;
