import React, { Component } from "react";
import Toast from "react-bootstrap/Toast";

export class ToastCode extends Component {
  render() {
    return (
      <Toast>
        {console.log("here")}
        <Toast.Header>
          <img src="holder.js/20x20?text=%20" className="rounded me-2" alt="" />
        </Toast.Header>
        <Toast.Body>{this.props.body}</Toast.Body>
      </Toast>
    );
  }
}

export default ToastCode;
