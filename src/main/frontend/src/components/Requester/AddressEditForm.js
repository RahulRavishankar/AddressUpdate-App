import React, { Component } from "react";
import axios from "axios";

class AddressEditForm extends Component {
  constructor(props) {
    super(props);
  }

  EntryInputRow({ name, placeholder, pattern }) {
    const topic = {
      display: "inline-block",
      width: 75,
      textAlign: "left",
      fontSize: "20px",
    };

    const row = {
      paddingBottom: "20px",
    };

    const input = {
      display: "inline-block",
      border: "0px",
      borderBottom: "1px solid black",
      outline: "none",
    };
    return (
      <div style={row}>
        <span style={topic}>{name}</span> :{" "}
        <input
          style={input}
          id={name}
          placeholder={placeholder}
          pattern={pattern}
          autoComplete="off"
          required
        />
      </div>
    );
  }

  handleSubmit = () => {
    const newaddress = document.getElementById("Address").value;
    this.props.editaddresscallback(newaddress);
  };

  render() {
    const box = {
      width: "600px",
      padding: "20px",
      marginTop: "20px",
    };

    return (
      <div className="card" style={box}>
        <this.EntryInputRow name="Address" placeholder="  ..." pattern=".+" />
        <div style={{ display: "block" }}>
          <div
            className="btn btn-outline-secondary"
            style={{ display: "inline-block", width: 100 }}
            onClick={() => {
              this.handleSubmit();
            }}
          >
            Submit
          </div>
          <div
            className="btn btn-outline-secondary"
            style={{ display: "inline-block", width: 100, marginLeft: 20 }}
            onClick={() => {
              this.props.hideeditcallback();
            }}
          >
            Cancel
          </div>
        </div>
      </div>
    );
  }
}

export default AddressEditForm;
