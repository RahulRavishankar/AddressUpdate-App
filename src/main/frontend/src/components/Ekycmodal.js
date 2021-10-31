import React, { Component } from "react";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import tick from "./tick.png";
import axios from "axios";

export class Ekycmodal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      imageDisplay: false,
    };
    this.saved = this.props.saved;
  }

  handleEkyc = () => {
    // axios request to handle ekyc request here
    const options = {
      url:
        "http://localhost:8080/introducer/fetchAddress/" +
        this.saved.uid +
        "/" +
        this.saved.txnid +
        "/" +
        this.saved.otp +
        "/" +
        this.saved.requesterUid,
      method: "GET",
    };

    axios(options).then(
      (response) => {
        console.log(response);
        this.setState({ imageDisplay: true });
      },
      (error) => {
        console.log(error);
      }
    );
  };

  render() {
    return (
      <div>
        <Modal
          show={this.props.show}
          // onHide={false}
          dialogClassName="modal-90w"
          aria-labelledby="example-custom-modal-styling-title"
        >
          <Modal.Header>
            <Modal.Title id="example-custom-modal-styling-title">
              E-KYC Verification
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {this.state.imageDisplay ? (
              <div>
                <p>Ekyc done!</p>
                <img style={{ width: 50, height: 50 }} src={tick} />
              </div>
            ) : (
              <p>Get your Ekyc done!</p>
            )}
          </Modal.Body>
          <Modal.Footer>
            <Button
              variant="primary"
              onClick={() => {
                this.handleEkyc();
              }}
            >
              Click here
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    );
  }
}

export default Ekycmodal;
