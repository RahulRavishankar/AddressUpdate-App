import React, { Component } from "react";
import Popup from "reactjs-popup";
import "reactjs-popup/dist/index.css";
import axios from "axios";
import { Redirect } from "react-router";

export class Form extends Component {
  constructor(props) {
    super(props);
    this.state = {
      otpStatus: "",
      uid: "",
      txnid: "",
      redirect: false,
    };
  }

  input = {
    display: "inline-block",
    border: "0px",
    borderBottom: "1px solid black",
    outline: "none",
  };

  redirect = () => {
    this.setState({ redirect: true });
  };

  otp() {
    const box = {
      width: "100%",
      paddingTop: "10px",
      paddingBottom: "10px",
      textAlign: "center",
    };
    return (
      <form style={box}>
        <div>OTP</div>
        <div style={{ marginBottom: 10 }}>
          <input style={this.input} id="actual-otp" />
        </div>
        <div>
          <div
            className="btn btn-secondary"
            style={{ width: 200 }}
            onClick={() => {
              this.submitOTP();
            }}
          >
            Submit
          </div>
        </div>
      </form>
    );
  }

  getOTP = () => {
    // axios POST request
    // generate txnID on the fly later
    const uid = document.getElementById("Aadhar").value;
    console.log(uid);
    const options = {
      url: "http://localhost:8080/requester/generateOtp/" + uid,
      method: "GET",
      // headers: {
      //  'Content-Type': 'application/json;charset=UTF-8',
      //  'Access-Control-Allow-Origin': '*'
      // },
      //  data: {
      //    "uid": uid,
      //    "txnId": "0acbaa8b-b3ae-433d-a5d2-51250ea8e970"
      //  }
    };

    axios(options).then(
      (response) => {
        console.log(response.data);
        // const obj = JSON.parse(response.data);
        if (response.data.success === "Y") {
          this.setState({
            otpStatus: "true",
            uid: response.data.uid,
            txnid: response.data.txnId,
          });
        } else {
          this.setState({
            otpStatus: "false",
          });
        }
      },
      (error) => {
        console.log(error);
        this.setState({ otpStatus: "error" });
      }
    );
    console.log(this.state.otpStatus);
  };

  submitOTP = () => {
    const otpinput = document.getElementById("actual-otp").value;
    console.log(this.state.uid);
    console.log(this.state.txnid);
    console.log(otpinput);
    const options = {
      url:
        "http://localhost:8080/requester/verifyOtp/" +
        this.state.uid +
        "/" +
        this.state.txnid +
        "/" +
        otpinput,
      method: "GET",
      headers: {
        //  'Content-Type': 'application/json;charset=UTF-8',
        //  'Access-Control-Allow-Origin': '*'
      },
      //  data: {
      //    "uid": uid,
      //    "txnId": "0acbaa8b-b3ae-433d-a5d2-51250ea8e970"
      //  }
    };

    axios(options).then(
      (response) => {
        console.log(response);

        if (response.data === "Authentication Successful") {
          console.log("auth complete");
          this.redirect();
        } else {
          console.log("auth NOT complete");
        }
      },
      (error) => {
        console.log(error);
      }
    );
    // console.log(this.state.otpStatus);
  };

  showOtpResult = () => {
    if (this.state.otpStatus === "true")
      return (
        <div className="text-success" style={{ fontSize: 12 }}>
          OTP sent successfully!
        </div>
      );
    else if (this.state.otpStatus === "false")
      return (
        <div className="text-danger" style={{ fontSize: 12 }}>
          Please check your credentials and try again!
        </div>
      );
    else if (this.state.otpStatus == "error") {
      return (
        <div className="text-danger" style={{ fontSize: 12 }}>
          Error has occured while sending OTP
        </div>
      );
    }
    return null;
  };

  redirectComponent = () => {
    if (!this.state.redirect) return;
    const name = document.getElementById("Name").value;
    const otp = document.getElementById("actual-otp").value;
    if (this.props.logintype === "Requester") {
      return (
        <Redirect
          to={{
            pathname: "/requester-dashboard",
            state: {
              logintype: this.props.logintype,
              name: name,
              uid: this.state.uid,
              txnid: this.state.txnid,
              otp: otp,
            },
          }}
        />
      );
    } else {
      return (
        <Redirect
          to={{
            pathname: "/Introducer-dashboard",
            state: {
              logintype: this.props.logintype,
              name: name,
              uid: this.state.uid,
              txnid: this.state.txnid,
              otp: otp,
            },
          }}
        />
      );
    }
  };

  render() {
    const box = {
      margin: "auto",
      width: "400px",
      padding: "20px",
      border: "5px solid grey",
      borderRadius: 20,
    };

    const topic = {
      display: "inline-block",
      width: 75,
      textAlign: "left",
      fontSize: "20px",
    };

    const row = {
      marginTop: "20px",
    };

    return (
      <div className="card" style={box}>
        {this.redirectComponent()}
        <div>
          <span style={topic}>Name</span> :{" "}
          <input
            style={this.input}
            id="Name"
            placeholder="Ramesh"
            pattern="[\w\s]+"
            autoComplete="off"
            required
          />
        </div>
        <div style={row}>
          <span style={topic}>Aadhar</span> :{" "}
          <input
            id="Aadhar"
            style={this.input}
            placeholder="000000000000"
            pattern="\d{4}\d{4}\d{4}"
            autoComplete="off"
            required
          />
        </div>

        <div style={row}>
          {" "}
          <div
            className="btn btn btn-secondary"
            style={{ width: 300 }}
            onClick={() => {
              this.getOTP();
            }}
          >
            Generate OTP
          </div>
        </div>

        {this.showOtpResult()}

        <div style={row}>
          <Popup
            trigger={
              <div className="btn btn-secondary" style={{ width: 300 }}>
                Submit OTP
              </div>
            }
            modal
          >
            {this.otp()}
          </Popup>
        </div>
      </div>
    );
  }
}

export default Form;
